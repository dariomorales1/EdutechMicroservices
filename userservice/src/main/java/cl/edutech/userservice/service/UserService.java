package cl.edutech.userservice.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import cl.edutech.userservice.model.User;
import cl.edutech.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByRut(String rut) {
        return userRepository.findById(rut).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public boolean existsByRut(String rut) {
        return userRepository.existsById(rut);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public User create(User user) {
        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }

    public void update(String rut, User userRequest) {

        User user = userRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setRole(userRequest.getRole());

        userRepository.save(user);
    }

    public void remove(String rut) {
        userRepository.deleteById(rut);
    }

    public void updatePassword(String rut, String newPassword) {
        User user = userRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));

        userRepository.save(user);
    }

}
