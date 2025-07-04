package cl.edutech.authservice.controller;

import cl.edutech.authservice.dto.LoginRequest;
import cl.edutech.authservice.dto.UserDTO;
import cl.edutech.authservice.service.AuthService;
import cl.edutech.authservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_EmailNotFound_Unauthorized() throws Exception {
        LoginRequest req = new LoginRequest("x@x.com", "pwd");
        when(authService.getUser("x@x.com")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Email not found"));
    }

    @Test
    void login_PasswordMismatch_Unauthorized() throws Exception {
        LoginRequest req = new LoginRequest("u@u.com", "raw");
        UserDTO user = new UserDTO("u@u.com", "encoded", "USER");
        when(authService.getUser("u@u.com")).thenReturn(user);
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Password not correct"));
    }

    @Test
    void login_Success_ReturnsAuthUser() throws Exception {
        LoginRequest req = new LoginRequest("u@u.com", "raw");
        UserDTO user = new UserDTO("u@u.com", "encoded", "ADMIN");
        when(authService.getUser("u@u.com")).thenReturn(user);
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("u@u.com", "ADMIN")).thenReturn("tok");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("u@u.com"))
                .andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    void ping_ReturnsPong() throws Exception {
        mockMvc.perform(get("/auth/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void pingUserService_ReturnsUserServicePing() throws Exception {
        ResponseEntity<String> resp = ResponseEntity.ok("up");
        when(authService.pingUserService()).thenReturn(resp);

        mockMvc.perform(get("/auth/ping-user"))
                .andExpect(status().isOk())
                .andExpect(content().string("up"));
    }
}
