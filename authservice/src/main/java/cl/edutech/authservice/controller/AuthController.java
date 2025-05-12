package cl.edutech.authservice.controller;

import cl.edutech.authservice.controller.Responsive.MessageResponsive;
import cl.edutech.authservice.model.Token;
import cl.edutech.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<MessageResponsive> login(@RequestParam String emailRequest, @RequestParam String passwordRequest) {
            boolean isAutenticated = authService.validateUser(emailRequest, passwordRequest);
            if (isAutenticated) {
                String id = authService.generateTokenId();
                Token token = new Token(id, emailRequest, passwordRequest);
                authService.create(token);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponsive("success"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponsive("error"));
        }

    @GetMapping("/ping-user")
    public ResponseEntity<MessageResponsive> pingUserService() {
        String response = authService.pingUserService();
        return ResponseEntity.ok(new MessageResponsive(response));
    }

}
