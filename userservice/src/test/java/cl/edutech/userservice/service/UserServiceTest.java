package cl.edutech.userservice.service;

import cl.edutech.userservice.model.User;
import cl.edutech.userservice.repository.UserRepository;
import cl.edutech.userservice.exception.ConflictException;
import cl.edutech.userservice.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindAll_thenReturnsList() {
        when(userRepository.findAll()).thenReturn(java.util.List.of(new User("1","e@e.com","First","Last","pwd","USER")));
        var list = userService.findAll();
        assertFalse(list.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void whenFindByRutExists_thenReturnUser() {
        User u = new User("1","e@e.com","First","Last","pwd","USER");
        when(userRepository.findById("1")).thenReturn(Optional.of(u));
        var result = userService.findByRut("1");
        assertEquals("1", result.getRut());
    }

    @Test
    void whenFindByRutNotExists_thenThrowNotFound() {
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findByRut("2"));
    }

    @Test
    void whenCreateWithExistingRut_thenThrowConflict() {
        User u = new User("1","e@e.com","First","Last","pwd","USER");
        when(userRepository.existsById("1")).thenReturn(true);
        assertThrows(ConflictException.class, () -> userService.create(u));
    }

    @Test
    void whenCreateWithExistingEmail_thenThrowConflict() {
        User u = new User("2","e@e.com","First","Last","pwd","USER");
        when(userRepository.existsById("2")).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase("e@e.com")).thenReturn(true);
        assertThrows(ConflictException.class, () -> userService.create(u));
    }

    @Test
    void whenCreateValid_thenEncodePasswordAndSave() {
        User u = new User("3","e@e.com","First","Last","pwd","USER");
        when(userRepository.existsById("3")).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase("e@e.com")).thenReturn(false);
        when(passwordEncoder.encode("pwd")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        var result = userService.create(u);
        assertEquals("encoded", result.getPassword());
        verify(userRepository).save(result);
    }

    @Test
    void whenUpdateNotExists_thenThrowNotFound() {
        when(userRepository.findById("4")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update("4", new User()));
    }

    @Test
    void whenRemoveNotExists_thenThrowNotFound() {
        when(userRepository.existsById("5")).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.remove("5"));
    }
    @Test
    void whenPartialUpdateValid_thenSaveUpdatedFields() {
        User existing = new User("6","e6@example.com","OldFirst","OldLast","pwd","USER");
        User updates = new User(null,"new@example.com","NewFirst","NewLast",null,null);
        when(userRepository.findById("6")).thenReturn(Optional.of(existing));
        when(userRepository.existsByEmailIgnoreCase("new@example.com")).thenReturn(false);

        userService.partialUpdate("6", updates);

        assertEquals("new@example.com", existing.getEmail());
        assertEquals("NewFirst", existing.getFirstName());
        assertEquals("NewLast", existing.getLastName());
        verify(userRepository).save(existing);
    }

    @Test
    void whenUpdatePasswordNotExists_thenThrowNotFound() {
        when(userRepository.findById("7")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.updatePassword("7", "newPwd"));
    }

    @Test
    void whenUpdatePasswordValid_thenEncodeAndSave() {
        User existing = new User("8","e8@example.com","Fn","Ln","oldPwd","USER");
        when(userRepository.findById("8")).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("newPwd")).thenReturn("encPwd");

        userService.updatePassword("8", "newPwd");

        assertEquals("encPwd", existing.getPassword());
        verify(userRepository).save(existing);
    }
}
