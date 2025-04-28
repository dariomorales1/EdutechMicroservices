package cl.edutech.userservice.service;

import cl.edutech.userservice.model.User;
import cl.edutech.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByRut(String rut) {
        return userRepository.findById(rut).get();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public void remove(String rut) {
        userRepository.deleteById(rut);
    }

}
