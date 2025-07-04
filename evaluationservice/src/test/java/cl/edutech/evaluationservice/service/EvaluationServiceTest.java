package cl.edutech.evaluationservice.service;

import cl.edutech.evaluationservice.DTO.CourseDTO;
import cl.edutech.evaluationservice.DTO.EvaluationUserCourse;
import cl.edutech.evaluationservice.DTO.UserDTO;
import cl.edutech.evaluationservice.exception.ConflictException;
import cl.edutech.evaluationservice.exception.NotFoundException;
import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.repository.EvaluationRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EvaluationServiceTest {
    @Mock private EvaluationRepository evaluationRepository;
    @Mock private WebClient userWebClient;
    @Mock private WebClient courseWebClient;
    @InjectMocks private EvaluationService evaluationService;

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
        doReturn(requestSpec).when(uriSpec).uri(anyString(), any(Object[].class));
        doReturn(responseSpec).when(requestSpec).retrieve();
    }

    @Test
    void whenFindAll_thenReturnList() {
        when(evaluationRepository.findAll()).thenReturn(List.of(
                new Evaluation("1","E1",9.0,"u1","c1")
        ));
        var list = evaluationService.findAll();
        assertFalse(list.isEmpty());
    }

    @Test
    void whenFindByIdNotExists_thenThrowNotFound() {
        when(evaluationRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> evaluationService.findById("2"));
    }

    @Test
    void whenCreateExists_thenThrowConflict() {
        Evaluation e = new Evaluation("3","E3",8.5,"u3","c3");
        when(evaluationRepository.existsByEvaluationId("3")).thenReturn(true);
        assertThrows(ConflictException.class, () -> evaluationService.create(e));
    }

    @Test
    void whenCreateUserNotFound_thenThrowNotFound() {
        Evaluation e = new Evaluation("4","E4",7.5,"u4","c4");
        when(evaluationRepository.existsByEvaluationId("4")).thenReturn(false);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> evaluationService.create(e));
    }

    @Test
    void whenCreateCourseNotFound_thenThrowNotFound() {
        Evaluation e = new Evaluation("5","E5",6.5,"u5","c5");
        when(evaluationRepository.existsByEvaluationId("5")).thenReturn(false);
        UserDTO userDto = new UserDTO();
        userDto.setRut("u5");
        userDto.setFirstName("FN");
        userDto.setLastName("LN");

        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.empty());
        assertThrows(NotFoundException.class, () -> evaluationService.create(e));
    }

    @Test
    void whenCreateValid_thenSave() {
        Evaluation e = new Evaluation("6","E6",5.5,"u6","c6");
        when(evaluationRepository.existsByEvaluationId("6")).thenReturn(false);
        UserDTO userDto = new UserDTO();
        userDto.setRut("u6");
        userDto.setFirstName("FN6");
        userDto.setLastName("LN6");

        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        CourseDTO courseDto = new CourseDTO();
        courseDto.setCourseId("c6");
        courseDto.setNameCourse("Course6");
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(courseDto));
        when(evaluationRepository.save(e)).thenReturn(e);
        var res = evaluationService.create(e);
        assertEquals("6", res.getEvaluationId());
    }

    @Test
    void whenUpdateNotExists_thenThrowNotFound() {
        when(evaluationRepository.findById("7")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> evaluationService.update("7", new Evaluation()));
    }

    @Test
    void whenPartialUpdate_Valid_thenSave() {
        Evaluation ex = new Evaluation("8","E8",4.5,"u8","c8");
        Evaluation req = new Evaluation(null,"E8p",null,"u8p",null);
        when(evaluationRepository.findById("8")).thenReturn(Optional.of(ex));
        UserDTO userDto = new UserDTO(); userDto.setRut("u8p"); userDto.setFirstName("F8"); userDto.setLastName("L8");
        CourseDTO courseDto = new CourseDTO(); courseDto.setCourseId("c8"); courseDto.setNameCourse("Course8");
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDto));
        when(responseSpec.bodyToMono(CourseDTO.class)).thenReturn(Mono.just(courseDto));
        evaluationService.partialUpdate("8", req);
        assertEquals("E8p", ex.getEvaluationName());
        assertEquals("u8p", ex.getStudentId());
    }

    @Test
    void whenRemoveNotExists_thenThrowNotFound() {
        when(evaluationRepository.existsByEvaluationId("9")).thenReturn(false);
        assertThrows(NotFoundException.class, () -> evaluationService.remove("9"));
    }

    @Test
    void whenRemoveExists_thenDeleteCalled() {
        when(evaluationRepository.existsByEvaluationId("10")).thenReturn(true);
        evaluationService.remove("10");
        verify(evaluationRepository).deleteById("10");
    }
}
