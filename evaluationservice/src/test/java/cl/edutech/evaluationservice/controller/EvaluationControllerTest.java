package cl.edutech.evaluationservice.controller;

import cl.edutech.evaluationservice.DTO.EvaluationUserCourse;
import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.service.EvaluationService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import cl.edutech.evaluationservice.DTO.UserDTO;
import cl.edutech.evaluationservice.DTO.CourseDTO;

class EvaluationControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock private EvaluationService evaluationService;
    @InjectMocks private EvaluationController evaluationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(evaluationController).build();
    }

    @Test
    void ping_ReturnsPONG() throws Exception {
        mockMvc.perform(get("/evaluations/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PONG"));
    }

    @Test
    void getEvaluations_NoContent() throws Exception {
        when(evaluationService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/evaluations"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("No evaluations found"));
    }

    @Test
    void getEvaluations_Ok() throws Exception {
        Evaluation e = new Evaluation("1","Eval1", 9.5, "u1", "c1");
        when(evaluationService.findAll()).thenReturn(Collections.singletonList(e));
        mockMvc.perform(get("/evaluations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].evaluationName").value("Eval1"));
    }

    @Test
    void searchEvaluation_ReturnsDto() throws Exception {
        Evaluation e = new Evaluation("2","Eval2", 8.0, "u2", "c2");
        EvaluationUserCourse dto = new EvaluationUserCourse();
        dto.setEvaluationId("2");
        dto.setEvaluationName("Eval2");
        dto.setScore(8.0);
        dto.setStudentId("u2");
        dto.setStudentFirstName("First");
        dto.setStudentLastName("Last");
        dto.setCourseId("c2");
        when(evaluationService.findById("2")).thenReturn(e);
        UserDTO userDto = new UserDTO();
        userDto.setRut("u2");
        userDto.setFirstName("First");
        userDto.setLastName("Last");

        when(evaluationService.getUser("u2")).thenReturn(userDto);
        CourseDTO courseDto = new CourseDTO();
        courseDto.setCourseId("c2");
        courseDto.setNameCourse("CourseName");
        when(evaluationService.getCourse("c2")).thenReturn(courseDto);

        mockMvc.perform(get("/evaluations/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.evaluationId").value("2"))
                .andExpect(jsonPath("$.studentFirstName").value("First"));
    }

    @Test
    void createEvaluation_ReturnsCreated() throws Exception {
        Evaluation e = new Evaluation("3","Eval3",7.0,"u3","c3");
        mockMvc.perform(post("/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Evaluation created"));
        verify(evaluationService).create(any(Evaluation.class));
    }

    @Test
    void updateEvaluation_ReturnsOk() throws Exception {
        Evaluation e = new Evaluation("4","Eval4",6.5,"u4","c4");
        mockMvc.perform(put("/evaluations/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Evaluation updated"));
        verify(evaluationService).update(eq("4"), any(Evaluation.class));
    }

    @Test
    void patchEvaluation_ReturnsOk() throws Exception {
        Evaluation e = new Evaluation("5",null,null,"u5",null);
        mockMvc.perform(patch("/evaluations/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Evaluation partially updated"));
        verify(evaluationService).partialUpdate(eq("5"), any(Evaluation.class));
    }

    @Test
    void deleteEvaluation_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/evaluations/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Evaluation was deleted"));
        verify(evaluationService).remove("6");
    }
}