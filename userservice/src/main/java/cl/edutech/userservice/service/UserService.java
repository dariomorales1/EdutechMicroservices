package cl.edutech.userservice.service;

import cl.edutech.userservice.model.User;
import cl.edutech.userservice.repository.UserRepository;
import cl.edutech.userservice.exception.NotFoundException;
import cl.edutech.userservice.exception.ConflictException;
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
        return userRepository.findById(rut)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con rut: " + rut));
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new NotFoundException("Usuario no encontrado con email: " + email);
        }
        return user;
    }

    public boolean existsByRut(String rut) {
        return userRepository.existsById(rut);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public User create(User user) {
        if (userRepository.existsById(user.getRut())) {
            throw new ConflictException("Ya existe un usuario con ese RUT");
        }
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }

    public void update(String rut, User userRequest) {
        User user = userRepository.findById(rut)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con rut: " + rut));

        // Valida si el nuevo email ya existe en otro usuario
        if (userRequest.getEmail() != null && !user.getEmail().equalsIgnoreCase(userRequest.getEmail())
                && userRepository.existsByEmailIgnoreCase(userRequest.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }

        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setRole(userRequest.getRole());

        userRepository.save(user);
    }

    public void partialUpdate(String rut, User userRequest) {
        User user = userRepository.findById(rut)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con rut: " + rut));

        if (userRequest.getEmail() != null && !user.getEmail().equalsIgnoreCase(userRequest.getEmail())
                && userRepository.existsByEmailIgnoreCase(userRequest.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }

        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }
        if (userRequest.getFirstName() != null) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getLastName() != null) {
            user.setLastName(userRequest.getLastName());
        }
        if (userRequest.getRole() != null) {
            user.setRole(userRequest.getRole());
        }
        userRepository.save(user);
    }

    public void remove(String rut) {
        if (!userRepository.existsById(rut)) {
            throw new NotFoundException("Usuario no encontrado con rut: " + rut);
        }
        userRepository.deleteById(rut);
    }

    public void updatePassword(String rut, String newPassword) {
        User user = userRepository.findById(rut)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con rut: " + rut));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}