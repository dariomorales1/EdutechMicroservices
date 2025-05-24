package cl.edutech.authservice.controller;

import cl.edutech.authservice.DTO.AuthUserDTO;
import cl.edutech.authservice.DTO.LoginRequest;
import cl.edutech.authservice.DTO.UserDTO;
import cl.edutech.authservice.controller.Responsive.MessageResponsive;
import cl.edutech.authservice.model.Token;
import cl.edutech.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponsive> ping() {
        return ResponseEntity.ok(new MessageResponsive("pong"));
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponsive> login(@RequestBody LoginRequest loginRequest) {
        UserDTO userExist = authService.getUser(loginRequest.getEmail());
        UserDTO passExist = authService.getUser(loginRequest.getPassword());
        if (userExist == null ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponsive("Email not found"));
        } else if (passExist == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponsive("Password not correct"));
        } else if (userExist == passExist) {
            String tokenId = authService.generateTokenId();
            AuthUserDTO userDTO = new AuthUserDTO();
            userDTO.setToken(tokenId);
            userDTO.setEmail(userExist.getEmail());
            userDTO.setPassword(userExist.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponsive("Login successful"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponsive("Login failed"));
    }

    @GetMapping("/ping-user")
    public ResponseEntity<?> pingUserService() {
        ResponseEntity<String> response = authService.pingUserService();
        return ResponseEntity.ok(response.getBody());
    }
}
