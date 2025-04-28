package cl.edutech.authservice.controller;

import cl.edutech.authservice.controller.Responsive.MessageResponsive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @GetMapping("/ping")
    public ResponseEntity<MessageResponsive> ping() {
        return ResponseEntity.ok(new MessageResponsive("pong"));
    }

}
