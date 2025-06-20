package cl.edutech.userservice.controller;

import cl.edutech.userservice.controller.Response.MessageResponse;
import cl.edutech.userservice.model.User;
import cl.edutech.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.edutech.userservice.dto.PasswordChangeRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() {
        return ResponseEntity.ok(new MessageResponse("PONG"));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAll();

        List<User> usersWithoutPassword = userList.stream()
                .map(u -> {
                    u.setPassword(null);
                    return u;
                })
                .collect(Collectors.toList());

        if (usersWithoutPassword.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usersWithoutPassword);
    }

    @GetMapping("/{rutRequest}")
    public ResponseEntity<User> searchUser(@PathVariable String rutRequest) {
        try {
            User user = userService.findByRut(rutRequest);
            if(user != null) user.setPassword(null);
            return user != null ?
                    ResponseEntity.ok(user) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/email/{emailRequest}")
    public ResponseEntity<User> searchUserByEmail(@PathVariable String emailRequest) {
        try {
            User user = userService.findByEmail(emailRequest);
            return user != null ?
                    ResponseEntity.ok(user) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody User userRequest) {
        if (userService.existsByRut(userRequest.getRut())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("USER ALREADY EXISTS"));
        }
        if (userService.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("EMAIL ALREADY EXISTS"));
        }
        userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("USER CREATED"));
    }

    @PutMapping("/{rutRequest}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable String rutRequest, @RequestBody User userRequest) {
        if (!userService.existsByRut(rutRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        }
        userService.update(rutRequest, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("USER UPDATED"));
    }

    @DeleteMapping("/{rutRequest}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String rutRequest) {
        if (!userService.existsByRut(rutRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER DOES NOT EXIST"));
        }
        userService.remove(rutRequest);
        return ResponseEntity.ok(new MessageResponse("USER DELETED"));
    }

    @PutMapping("/{rut}/password")
    public ResponseEntity<MessageResponse> updatePassword(
            @PathVariable String rut,
            @RequestBody PasswordChangeRequest passwordChangeRequest) {

        try {
            userService.updatePassword(rut, passwordChangeRequest.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("PASSWORD UPDATED"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("USER NOT FOUND"));
        }
    }

    @PatchMapping("/{rut}")
    public ResponseEntity<MessageResponse> patchUser(
            @PathVariable String rut,
            @RequestBody User userRequest) {
        userService.partialUpdate(rut, userRequest);
        return ResponseEntity.ok(new MessageResponse("USER PARTIALLY UPDATED"));
    }
}
