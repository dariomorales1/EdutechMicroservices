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

        UserDTO user = authService.getUser(loginRequest.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Password not correct");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        AuthUserDTO authUser = new AuthUserDTO();
        authUser.setEmail(user.getEmail());
        authUser.setToken(token);

        return ResponseEntity.ok(authUser);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/ping-user")
    public ResponseEntity<String> pingUserService() {
        String body = authService.pingUserService().getBody();
        return ResponseEntity.ok(body);
    }
}
