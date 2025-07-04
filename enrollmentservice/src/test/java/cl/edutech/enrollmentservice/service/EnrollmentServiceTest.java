// File: src/test/java/cl/edutech/enrollmentservice/service/EnrollmentServiceTest.java
package cl.edutech.enrollmentservice.service;

import cl.edutech.enrollmentservice.DTO.CourseDTO;
import cl.edutech.enrollmentservice.DTO.EnrollmentUserCourseDTO;
import cl.edutech.enrollmentservice.DTO.UserDTO;
import cl.edutech.enrollmentservice.exception.ConflictException;
import cl.edutech.enrollmentservice.exception.NotFoundException;
import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {
    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private WebClient userWebClient;
    @Mock private WebClient courseWebClient;
    @InjectMocks private EnrollmentService enrollmentService;

    private WebClient.RequestHeadersUriSpec<?> uriSpec;
    private WebClient.RequestHeadersSpec<?> requestSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        // use doReturn to avoid generic capture issues
        doReturn(uriSpec).when(userWebClient).get();
        doReturn(uriSpec).when(courseWebClient).get();
        doReturn(requestSpec).when(uriSpec).uri(anyString(), any(Object[].class));
        doReturn(responseSpec).when(requestSpec).retrieve();
    }

    @Test
    void whenFindAll_thenReturnList() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(new Enrollment(1, "u", "c")));
        var list = enrollmentService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenFindByIdNotExists_thenThrowNotFound() {
        when(enrollmentRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> enrollmentService.findById(2));
    }

    @Test
    void whenCreateExists_thenThrowConflict() {
        Enrollment e = new Enrollment(3, "u", "c");
        when(enrollmentRepository.existsById(3)).thenReturn(true);
        assertThrows(ConflictException.class, () -> enrollmentService.create(e));
    }

    @Test
    void whenCreateUserNotFound_thenThrowNotFound() {
        Enrollment e = new Enrollment(4, "u4", "c4");
        when(enrollmentRepository.existsById(4)).thenReturn(false);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> enrollmentService.create(e));
    }

    @Test
    void whenCreateCourseNotFound_thenThrowNotFound() {
        Enrollment e = new Enrollment(5, "u5", "c5");
        when(enrollmentRepository.existsById(5)).thenReturn(false);
        UserDTO userDto = new UserDTO();
        userDto.setRut("u5");
        userDto.setEmail("e5");
        userDto.setPassword("p5");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> enrollmentService.create(e));
    }

    @Test
    void whenCreateValid_thenSave() {
        Enrollment e = new Enrollment(6, "u6", "c6");
        when(enrollmentRepository.existsById(6)).thenReturn(false);
        UserDTO userDto = new UserDTO();
        userDto.setRut("u6");
        userDto.setEmail("e6");
        userDto.setPassword("p6");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        CourseDTO courseDto = new CourseDTO();
        courseDto.setCourseId("c6");
        courseDto.setNameCourse("Course6");
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(courseDto));
        when(enrollmentRepository.save(e)).thenReturn(e);
        var res = enrollmentService.create(e);
        assertEquals(6, res.getId());
    }

    @Test
    void whenGetEnrollmentWithUserAndCourse_Valid_thenReturnDto() {
        Enrollment e = new Enrollment(7, "u7", "c7");
        when(enrollmentRepository.findById(7)).thenReturn(Optional.of(e));
        UserDTO userDto = new UserDTO();
        userDto.setRut("u7");
        userDto.setEmail("e7");
        userDto.setPassword("p7");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        CourseDTO courseDto = new CourseDTO();
        courseDto.setCourseId("c7");
        courseDto.setNameCourse("Course7");
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(courseDto));
        EnrollmentUserCourseDTO dto = enrollmentService.getEnrollmentWithUserAndCourse(7);
        assertEquals(7, dto.getId());
        assertEquals("u7", dto.getRut());
        assertEquals("c7", dto.getCourseId());
    }

    @Test
    void whenPingUserService_Success() {
        doReturn(requestSpec).when(uriSpec).uri(eq("/ping"), any(Object[].class));
        doReturn(responseSpec).when(requestSpec).retrieve();
        when(responseSpec.toEntity(String.class)).thenReturn(Mono.just(ResponseEntity.ok("pong")));
        assertTrue(enrollmentService.pingUserService());
    }

    @Test
    void whenPingUserService_Failure() {
        when(userWebClient.get()).thenThrow(new RuntimeException());
        assertFalse(enrollmentService.pingUserService());
    }
}
