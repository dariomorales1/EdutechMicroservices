package cl.edutech.authservice.controller;

import cl.edutech.authservice.DTO.AuthUserDTO;
import cl.edutech.authservice.DTO.LoginRequest;
import cl.edutech.authservice.DTO.UserDTO;
import cl.edutech.authservice.controller.Responsive.MessageResponsive;
import cl.edutech.authservice.model.Token;
import cl.edutech.authservice.service.AuthService;
import cl.edutech.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponsive> ping() {
        return ResponseEntity.ok(new MessageResponsive("pong"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Obtener el usuario desde el servicio (UserDTO debe traer al menos email + hash de contraseña)
        UserDTO user = authService.getUser(loginRequest.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponsive("Email not found"));
        }

        // 2. Comparar la contraseña ingresada (texto plano) con el hash almacenado
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponsive("Password not correct"));
        }

        // 3. Generar el JWT (ahora que la contraseña es válida)
        String tokenId = jwtUtil.generateToken(user.getEmail());

        AuthUserDTO userDTO = new AuthUserDTO();
        userDTO.setToken(tokenId);
        userDTO.setEmail(user.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @GetMapping("/ping-user")
    public ResponseEntity<?> pingUserService() {
        ResponseEntity<String> response = authService.pingUserService();
        return ResponseEntity.ok(response.getBody());
    }
}
