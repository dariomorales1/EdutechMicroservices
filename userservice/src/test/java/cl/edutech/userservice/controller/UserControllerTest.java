package cl.edutech.userservice.controller;

import cl.edutech.userservice.model.User;
import cl.edutech.userservice.dto.PasswordChangeRequest;
import cl.edutech.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testPing() throws Exception {
        mockMvc.perform(get("/users/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PONG"));
    }

    @Test
    void testGetAllUsers_NoContent() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllUsers_Ok() throws Exception {
        User u = new User("1", "u1@example.com", "First", "Last", "pwd", "USER");
        when(userService.findAll()).thenReturn(Collections.singletonList(u));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rut").value("1"))
                .andExpect(jsonPath("$[0].email").value("u1@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("First"));
    }

    @Test
    void testSearchUser_Found() throws Exception {
        User u = new User("2", "u2@example.com", "First", "Last", "pwd", "USER");
        when(userService.findByRut("2")).thenReturn(u);
        mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("2"));
    }

    @Test
    void testSearchUser_NotFound() throws Exception {
        when(userService.findByRut("99")).thenThrow(new RuntimeException());
        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchByEmail_Found() throws Exception {
        User u = new User("3", "u3@example.com", "First", "Last", "pwd", "USER");
        when(userService.findByEmail("u3@example.com")).thenReturn(u);
        mockMvc.perform(get("/users/email/u3@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("u3@example.com"));
    }

    @Test
    void testCreateUser_ConflictRut() throws Exception {
        User req = new User("4", "u4@example.com", "First", "Last", "pwd", "USER");
        when(userService.existsByRut("4")).thenReturn(true);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("USER ALREADY EXISTS"));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        User req = new User("5", "u5@example.com", "First", "Last", "pwd", "USER");
        when(userService.existsByRut("5")).thenReturn(false);
        when(userService.existsByEmail("u5@example.com")).thenReturn(false);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("USER CREATED"));
        verify(userService).create(any(User.class));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        User req = new User("6", "u6@example.com", "First", "Last", "pwd", "USER");
        when(userService.existsByRut("6")).thenReturn(true);
        mockMvc.perform(put("/users/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("USER UPDATED"));
        verify(userService).update(eq("6"), any(User.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.existsByRut("7")).thenReturn(true);
        mockMvc.perform(delete("/users/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("USER DELETED"));
        verify(userService).remove("7");
    }

    @Test
    void testUpdatePassword_Success() throws Exception {
        PasswordChangeRequest passReq = new PasswordChangeRequest();
        passReq.setNewPassword("newPass");
        mockMvc.perform(put("/users/8/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PASSWORD UPDATED"));
        verify(userService).updatePassword("8", "newPass");
    }

    @Test
    void testPatchUser_Success() throws Exception {
        User req = new User("9", null, null, "First", null, null);
        mockMvc.perform(patch("/users/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("USER PARTIALLY UPDATED"));
        verify(userService).partialUpdate(eq("9"), any(User.class));
    }
}

