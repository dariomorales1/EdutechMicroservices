// File: src/test/java/cl/edutech/authservice/service/AuthServiceTest.java
package cl.edutech.authservice.service;

import cl.edutech.authservice.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private WebClient userWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getUser_Success() {
        UserDTO dto = new UserDTO("e@e.com", "pwd", "USER");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(dto));

        UserDTO result = authService.getUser("e@e.com");
        assertNotNull(result);
        assertEquals("e@e.com", result.getEmail());
    }

    @Test
    void getUser_ErrorReturnsNull() {
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.error(new RuntimeException()));

        UserDTO result = authService.getUser("bad");
        assertNull(result);
    }

    @Test
    void pingUserService_ReturnsBody() {
        when(uriSpec.uri("/ping")).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("up"))); // stub on responseSpec

        ResponseEntity<String> result = authService.pingUserService();
        assertNotNull(result);
        assertEquals("up", result.getBody());
    }
}
