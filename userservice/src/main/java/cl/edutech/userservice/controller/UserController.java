package cl.edutech.userservice.controller;

import cl.edutech.userservice.controller.Response.MessageResponse;
import cl.edutech.userservice.model.User;
import cl.edutech.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (userList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{rutRequest}")
    public ResponseEntity<User> searchUser(@PathVariable String rutRequest) {
        try {
            User user = userService.findByRut(rutRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("/email/{emailRequest}")
    public ResponseEntity<User> searchUserByEmail(@PathVariable String emailRequest) {
        try {
            User user = userService.findByEmail(emailRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody User userRequest) {
        List<User> userList = userService.findAll();
        for(User user : userList) {
            if(user.getRut().equals(userRequest.getRut())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("USER WAS EXISTS"));
            }
        }
        userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("USER CREATED"));
    }

    @PutMapping("/{rutRequest}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable String rutRequest, @RequestBody User userRequest) {
        List<User> userList = userService.findAll();
        for(User user : userList) {
            if(user.getRut().equals(rutRequest)) {
                userService.remove(rutRequest);
                userService.create(userRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("USER WAS UPDATED"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
    }


   @DeleteMapping("/{rutRequest}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String rutRequest) {
        List<User> userList = userService.findAll();
        for(User user : userList) {
            if(user.getRut().equals(rutRequest)) {
                userService.remove(rutRequest);
                return ResponseEntity.ok(new MessageResponse("User deleted"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Does Not Exist"));
    }

    // Metodo para validar tokens

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateUser(@RequestParam String emailRequest, @RequestParam String passwordRequest) {
       List<User> userList = userService.findAll();
       for(User user : userList) {
           if(user.getEmail().equals(emailRequest) && user.getPassword().equals(passwordRequest)) {
               return ResponseEntity.ok(true);
           }
       }
       return ResponseEntity.ok(false);
    }

    @PostMapping("/validate/rut")
    public ResponseEntity<Boolean> validateUserRut(@RequestParam String rutRequest) {
        List<User> userList = userService.findAll();
        for(User user : userList) {
            if(user.getRut().equals(rutRequest)) {
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }
}
