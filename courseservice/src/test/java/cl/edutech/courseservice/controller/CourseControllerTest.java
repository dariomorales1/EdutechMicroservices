package cl.edutech.courseservice.controller;

import cl.edutech.courseservice.model.Course;
import cl.edutech.courseservice.service.CourseService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void ping_ReturnsPONG() throws Exception {
        mockMvc.perform(get("/courses/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PONG"));
    }

    @Test
    void getAllCourses_NoContent() throws Exception {
        when(courseService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/courses"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllCourses_Ok() throws Exception {
        Course c = new Course("1","Name","Title","Desc","Image",100);
        when(courseService.findAll()).thenReturn(Collections.singletonList(c));
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value("1"));
    }

    @Test
    void searchCourse_NotFound() throws Exception {
        when(courseService.findById("99")).thenReturn(null);
        mockMvc.perform(get("/courses/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchCourse_Found() throws Exception {
        Course c = new Course("2","Name","Title","Desc","Image",200);
        when(courseService.findById("2")).thenReturn(c);
        mockMvc.perform(get("/courses/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameCourse").value("Name"));
    }

    @Test
    void createCourse_Conflict() throws Exception {
        Course c = new Course("3","N","T","D","I",300);
        when(courseService.existsByCourseId("3")).thenReturn(true);
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("COURSE ALREADY EXISTS"));
    }

    @Test
    void createCourse_Success() throws Exception {
        Course c = new Course("4","N","T","D","I",400);
        when(courseService.existsByCourseId("4")).thenReturn(false);
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("COURSE CREATED"));
        verify(courseService).create(any(Course.class));
    }

    @Test
    void updateCourse_NotFound() throws Exception {
        Course c = new Course();
        when(courseService.existsByCourseId("5")).thenReturn(false);
        mockMvc.perform(put("/courses/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("COURSE NOT FOUND"));
    }

    @Test
    void updateCourse_Success() throws Exception {
        Course c = new Course();
        when(courseService.existsByCourseId("6")).thenReturn(true);
        mockMvc.perform(put("/courses/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("COURSE UPDATED"));
        verify(courseService).update(eq("6"), any(Course.class));
    }

    @Test
    void deleteCourse_NotFound() throws Exception {
        when(courseService.existsByCourseId("7")).thenReturn(false);
        mockMvc.perform(delete("/courses/7"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("COURSE NOT FOUND"));
    }

    @Test
    void deleteCourse_Success() throws Exception {
        when(courseService.existsByCourseId("8")).thenReturn(true);
        mockMvc.perform(delete("/courses/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("COURSE DELETED"));
        verify(courseService).remove("8");
    }

    @Test
    void patchCourse_Success() throws Exception {
        Course c = new Course();
        mockMvc.perform(patch("/courses/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(c)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("COURSE PARTIALLY UPDATED"));
        verify(courseService).partialUpdate(eq("9"), any(Course.class));
    }
}