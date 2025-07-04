package cl.edutech.supportservice.service;

import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.repository.SupportRepository;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.DTO.CourseDTO;
import cl.edutech.supportservice.exception.ConflictException;
import cl.edutech.supportservice.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
        // stubs para la cadena WebClient
        doReturn(uriSpec).when(userWebClient).get();
        doReturn(uriSpec).when(courseWebClient).get();
        doReturn(requestSpec).when(uriSpec).uri(anyString(), any());
        doReturn(responseSpec).when(requestSpec).retrieve();
    }

    @Test
    void whenFindAll_thenReturnList() {
        SupportTicket t = new SupportTicket();
        t.setId(1);
        t.setUserRut("u");
        t.setCourseId("c");
        t.setSubject("sub");
        t.setDescription("desc");
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
    void whenCreateExists_thenThrowConflict() {
        SupportTicket t = new SupportTicket();
        t.setId(3);
        when(supportRepository.existsById(3)).thenReturn(true);
        assertThrows(ConflictException.class, () -> supportService.create(t));
    }

    @Test
    void whenCreateUserNotFound_thenThrowNotFound() {
        SupportTicket t = new SupportTicket();
        t.setId(4);
        t.setUserRut("u4");
        t.setCourseId("c4");
        when(supportRepository.existsById(4)).thenReturn(false);
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(UserDTO.class);
        assertThrows(NotFoundException.class, () -> supportService.create(t));
    }

    @Test
    void whenCreateCourseNotFound_thenThrowNotFound() {
        SupportTicket t = new SupportTicket();
        t.setId(5);
        t.setUserRut("u5");
        t.setCourseId("c5");
        when(supportRepository.existsById(5)).thenReturn(false);

        UserDTO u = new UserDTO();
        u.setRut("u5");
        u.setEmail("e5");
        u.setFirstName("f");
        u.setLastName("l");
        doReturn(Mono.just(u)).when(responseSpec).bodyToMono(UserDTO.class);
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(CourseDTO.class);

        assertThrows(NotFoundException.class, () -> supportService.create(t));
    }

    @Test
    void whenCreateValid_thenSave() {
        SupportTicket t = new SupportTicket();
        t.setId(6);
        t.setUserRut("u6");
        t.setCourseId("c6");
        t.setSubject("s6");
        t.setDescription("d6");
        when(supportRepository.existsById(6)).thenReturn(false);

        UserDTO u = new UserDTO();
        u.setRut("u6");
        u.setEmail("e6");
        u.setFirstName("f6");
        u.setLastName("l6");
        CourseDTO c = new CourseDTO();
        c.setCourseId("c6");
        c.setNameCourse("Course6");
        doReturn(Mono.just(u)).when(responseSpec).bodyToMono(UserDTO.class);
        doReturn(Mono.just(c)).when(responseSpec).bodyToMono(CourseDTO.class);
        when(supportRepository.save(t)).thenReturn(t);

        var res = supportService.create(t);
        assertEquals(6, res.getId());
    }

    @Test
    void whenRemoveNotExists_thenThrowNotFound() {
        when(supportRepository.existsById(7)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> supportService.remove(7));
    }

    @Test
    void whenRemoveExists_thenDeleteCalled() {
        when(supportRepository.existsById(8)).thenReturn(true);
        supportService.remove(8);
        verify(supportRepository).deleteById(8);
    }

    @Test
    void whenUpdateNotExists_thenThrowNotFound() {
        SupportTicket t = new SupportTicket();
        when(supportRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> supportService.update(9, t));
    }

    @Test
    void whenUpdateUserOrCourseNotFound_thenThrowNotFound() {
        SupportTicket existing = new SupportTicket();
        existing.setId(10);
        SupportTicket req = new SupportTicket();
        req.setUserRut("u10");
        req.setCourseId("c10");
        when(supportRepository.findById(10)).thenReturn(Optional.of(existing));
        doReturn(Mono.empty()).when(responseSpec).bodyToMono(UserDTO.class);
        assertThrows(NotFoundException.class, () -> supportService.update(10, req));
    }

    @Test
    void whenUpdateValid_thenSaveChanges() {
        SupportTicket existing = new SupportTicket();
        existing.setId(11);
        SupportTicket req = new SupportTicket();
        req.setUserRut("u11");
        req.setCourseId("c11");
        req.setSubject("sub11");
        req.setDescription("desc11");
        when(supportRepository.findById(11)).thenReturn(Optional.of(existing));

        UserDTO u = new UserDTO();
        u.setRut("u11");
        u.setEmail("e11");
        u.setFirstName("f");
        u.setLastName("l");
        CourseDTO c = new CourseDTO();
        c.setCourseId("c11");
        c.setNameCourse("Course11");
        doReturn(Mono.just(u)).when(responseSpec).bodyToMono(UserDTO.class);
        doReturn(Mono.just(c)).when(responseSpec).bodyToMono(CourseDTO.class);

        supportService.update(11, req);
        assertEquals("u11", existing.getUserRut());
        assertEquals("c11", existing.getCourseId());
        assertEquals("sub11", existing.getSubject());
        assertEquals("desc11", existing.getDescription());
    }

    @Test
    void whenPartialUpdateValid_thenApplyChanges() {
        SupportTicket existing = new SupportTicket();
        existing.setId(12);
        existing.setUserRut("u12");
        existing.setCourseId("c12");
        existing.setSubject("sub");
        existing.setDescription("desc");
        SupportTicket req = new SupportTicket();
        req.setSubject("newSub");
        when(supportRepository.findById(12)).thenReturn(Optional.of(existing));

        supportService.partialUpdate(12, req);
        assertEquals("newSub", existing.getSubject());
        verify(supportRepository).save(existing);
    }
}
