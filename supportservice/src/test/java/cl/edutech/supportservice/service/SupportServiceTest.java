package cl.edutech.supportservice.service;

import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.repository.SupportRepository;
import cl.edutech.supportservice.exception.NotFoundException;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.DTO.CourseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SupportServiceTest {
    @Mock private SupportRepository supportRepository;
    @Mock private WebClient userWebClient;
    @Mock private WebClient courseWebClient;
    @InjectMocks private SupportService supportService;

    private WebClient.RequestHeadersUriSpec<?> uriSpec;
    private WebClient.RequestHeadersSpec<?> requestSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        doReturn(uriSpec).when(userWebClient).get();
        doReturn(uriSpec).when(courseWebClient).get();

        doReturn(requestSpec)
                .when(uriSpec).uri(anyString(), any(Object[].class));
        doReturn(requestSpec)
                .when(uriSpec).uri(anyString(), any(Function.class));

        doReturn(responseSpec)
                .when(requestSpec).retrieve();
    }

    @Test
    void whenFindAll_thenReturnList() {
        SupportTicket t = new SupportTicket();
        t.setId(1);
        t.setUserRut("u");
        t.setCourseId("c");
        t.setSubject("i");
        when(supportRepository.findAll()).thenReturn(List.of(t));

        var list = supportService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenFindByIdNotExists_thenReturnNull() {
        when(supportRepository.findById(2)).thenReturn(Optional.empty());
        assertNull(supportService.findById(2));
    }

    @Test
    void whenCreateValid_thenSave() {
        SupportTicket t = new SupportTicket();
        t.setId(3);
        t.setUserRut("u3");
        t.setCourseId("c3");
        t.setSubject("i3");
        when(supportRepository.save(t)).thenReturn(t);

        UserDTO u = new UserDTO(); u.setRut("u3");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(u));

        CourseDTO c = new CourseDTO(); c.setCourseId("c3"); c.setNameCourse("n");
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(c));

        var res = supportService.create(t);
        assertEquals(3, res.getId());
    }

    @Test
    void whenCreateUserNotFound_thenThrowNotFound() {
        SupportTicket t = new SupportTicket();
        t.setId(4);
        t.setUserRut("u4");
        t.setCourseId("c4");
        t.setSubject("i4");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.empty());

        assertThrows(NotFoundException.class, () -> supportService.create(t));
    }

    @Test
    void whenCreateCourseNotFound_thenThrowNotFound() {
        SupportTicket t = new SupportTicket();
        t.setId(5);
        t.setUserRut("u5");
        t.setCourseId("c5");
        t.setSubject("i5");

        UserDTO u = new UserDTO(); u.setRut("u5");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(u));
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.empty());

        assertThrows(NotFoundException.class, () -> supportService.create(t));
    }

    @Test
    void whenRemoveNotExists_thenThrowNotFound() {
        when(supportRepository.existsById(6)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> supportService.remove(6));
    }

    @Test
    void whenRemoveExists_thenDeleteCalled() {
        when(supportRepository.existsById(7)).thenReturn(true);
        supportService.remove(7);
        verify(supportRepository).deleteById(7);
    }
}
