package cl.edutech.enrollmentservice.controller;

import cl.edutech.enrollmentservice.DTO.EnrollmentUserCourseDTO;
import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.service.EnrollmentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EnrollmentControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock private EnrollmentService enrollmentService;
    @InjectMocks private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    void ping_ReturnsPong() throws Exception {
        mockMvc.perform(get("/enroll/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void getAllEnrollments_NoContent() throws Exception {
        when(enrollmentService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/enroll"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEnrollments_Ok() throws Exception {
        Enrollment e = new Enrollment(1,"u","c");
        when(enrollmentService.findAll()).thenReturn(Collections.singletonList(e));
        mockMvc.perform(get("/enroll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getEnrollmentWithUserAndCourse_ReturnsDto() throws Exception {
        EnrollmentUserCourseDTO dto = new EnrollmentUserCourseDTO();
        dto.setId(2);
        when(enrollmentService.getEnrollmentWithUserAndCourse(2)).thenReturn(dto);
        mockMvc.perform(get("/enroll/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void createEnrollment_ReturnsCreated() throws Exception {
        Enrollment e = new Enrollment(3,"u3","c3");
        mockMvc.perform(post("/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("ENROLLMENT CREATED"));
        verify(enrollmentService).create(any(Enrollment.class));
    }

    @Test
    void updateEnrollment_ReturnsOk() throws Exception {
        Enrollment e = new Enrollment(4,"u4","c4");
        mockMvc.perform(put("/enroll/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ENROLLMENT UPDATED"));
        verify(enrollmentService).update(eq(4), any(Enrollment.class));
    }

    @Test
    void patchEnrollment_ReturnsOk() throws Exception {
        Enrollment e = new Enrollment(5,null,"c5");
        mockMvc.perform(patch("/enroll/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(e)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ENROLLMENT PARTIALLY UPDATED"));
        verify(enrollmentService).partialUpdate(eq(5), any(Enrollment.class));
    }

    @Test
    void deleteEnrollment_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/enroll/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("ENROLLMENT DELETED"));
        verify(enrollmentService).remove(6);
    }

    @Test
    void pingUserService_Success() throws Exception {
        when(enrollmentService.pingUserService()).thenReturn(true);
        mockMvc.perform(get("/enroll/ping-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PONG"));
    }

    @Test
    void pingUserService_Failure() throws Exception {
        when(enrollmentService.pingUserService()).thenReturn(false);
        mockMvc.perform(get("/enroll/ping-user"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("USER SERVICE NOT AVAILABLE"));
    }
}