package cl.edutech.authservice.controller;

import cl.edutech.authservice.dto.AuthUserDTO;
import cl.edutech.authservice.dto.LoginRequest;
import cl.edutech.authservice.dto.UserDTO;
import cl.edutech.authservice.service.AuthService;
import cl.edutech.authservice.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Llamar a UserService para obtener datos del usuario
        UserDTO user = authService.getUser(loginRequest.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email not found");
        }

        // 2. Validar contraseña usando el hash recibido de UserService
        System.out.println("Password recibido del usuario: " + loginRequest.getPassword());
        System.out.println("Hash recibido desde UserService: " + user.getPassword());
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Password not correct");
        }

        // 3. Generar el JWT
        String token = jwtUtil.generateToken(user.getEmail());

        AuthUserDTO authUser = new AuthUserDTO();
        authUser.setEmail(user.getEmail());
        authUser.setToken(token);

        return ResponseEntity.ok(authUser);
    }

    // Ping para verificar AuthService
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    // Ping para probar conexión con UserService
    @GetMapping("/ping-user")
    public ResponseEntity<String> pingUserService() {
        String body = authService.pingUserService().getBody();
        return ResponseEntity.ok(body);
    }
}
