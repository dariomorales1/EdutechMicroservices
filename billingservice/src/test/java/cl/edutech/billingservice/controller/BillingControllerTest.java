package cl.edutech.billingservice.controller;

import cl.edutech.billingservice.DTO.UserDTO;
import cl.edutech.billingservice.DTO.CourseDTO;
import cl.edutech.billingservice.DTO.EnrollmentDTO;
import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.service.BillingService;
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

class BillingControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock private BillingService billingService;
    @InjectMocks private BillingController billingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billingController).build();
    }

    @Test
    void ping_ReturnsPONG() throws Exception {
        mockMvc.perform(get("/billings/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("PONG"));
    }

    @Test
    void getAllBillings_NoContent() throws Exception {
        when(billingService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/billings"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllBillings_Ok() throws Exception {
        Billing b = new Billing();
        b.setBillingId(1);
        b.setUserId("u1");
        b.setCourseId("c1");
        b.setEnrollmentId(null);
        b.setAmount(10);
        when(billingService.findAll()).thenReturn(Collections.singletonList(b));
        mockMvc.perform(get("/billings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].billingId").value(1));
    }

    @Test
    void searchBilling_UserNotFound() throws Exception {
        Billing b = new Billing();
        b.setBillingId(2);
        b.setUserId("u2");
        b.setCourseId("c2");
        b.setEnrollmentId(null);
        b.setAmount(20);
        when(billingService.findById(2)).thenReturn(b);
        when(billingService.getUser("u2")).thenReturn(null);
        mockMvc.perform(get("/billings/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("USER NOT FOUND"));
    }

    @Test
    void searchBilling_CourseNotFound() throws Exception {
        Billing b = new Billing();
        b.setBillingId(3);
        b.setUserId("u3");
        b.setCourseId("c3");
        b.setEnrollmentId(null);
        b.setAmount(30);
        when(billingService.findById(3)).thenReturn(b);
        UserDTO u = new UserDTO(); u.setRut("u3");
        when(billingService.getUser("u3")).thenReturn(u);
        when(billingService.getCourse("c3")).thenReturn(null);
        mockMvc.perform(get("/billings/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("COURSE NOT FOUND"));
    }

    @Test
    void searchBilling_EnrollmentNotFound() throws Exception {
        Billing b = new Billing();
        b.setBillingId(4);
        b.setUserId("u4");
        b.setCourseId("c4");
        b.setEnrollmentId(100);
        b.setAmount(40);
        when(billingService.findById(4)).thenReturn(b);
        UserDTO u = new UserDTO(); u.setRut("u4");
        CourseDTO c = new CourseDTO(); c.setCourseId("c4");
        when(billingService.getUser("u4")).thenReturn(u);
        when(billingService.getCourse("c4")).thenReturn(c);
        when(billingService.getEnrollment(100)).thenReturn(null);
        mockMvc.perform(get("/billings/4"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ENROLLMENT NOT FOUND"));
    }

    @Test
    void searchBilling_Success() throws Exception {
        Billing b = new Billing();
        b.setBillingId(5);
        b.setUserId("u5");
        b.setCourseId("c5");
        b.setEnrollmentId(101);
        b.setAmount(50);
        when(billingService.findById(5)).thenReturn(b);
        when(billingService.getUser("u5")).thenReturn(new UserDTO());
        when(billingService.getCourse("c5")).thenReturn(new CourseDTO());
        when(billingService.getEnrollment(101)).thenReturn(new EnrollmentDTO());
        mockMvc.perform(get("/billings/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billingId").value(5));
    }

    @Test
    void createBilling_Conflict() throws Exception {
        Billing b = new Billing();
        b.setBillingId(6);
        b.setUserId("u6");
        b.setCourseId("c6");
        b.setEnrollmentId(null);
        b.setAmount(60);
        when(billingService.findById(6)).thenReturn(b);
        mockMvc.perform(post("/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("BILLING EXISTS"));
    }

    @Test
    void createBilling_UserNotFound() throws Exception {
        Billing b = new Billing();
        b.setBillingId(7);
        b.setUserId("u7");
        b.setCourseId("c7");
        b.setEnrollmentId(null);
        b.setAmount(70);
        when(billingService.findById(7)).thenReturn(null);
        when(billingService.getUser("u7")).thenReturn(null);
        mockMvc.perform(post("/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("USER NOT FOUND"));
    }

    @Test
    void createBilling_Success() throws Exception {
        Billing b = new Billing();
        b.setBillingId(8);
        b.setUserId("u8");
        b.setCourseId("c8");
        b.setEnrollmentId(null);
        b.setAmount(80);
        when(billingService.findById(8)).thenReturn(null);
        when(billingService.getUser("u8")).thenReturn(new UserDTO());
        when(billingService.getCourse("c8")).thenReturn(new CourseDTO());
        when(billingService.getEnrollment(null)).thenReturn(new EnrollmentDTO());
        mockMvc.perform(post("/billings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("BILLING CREATED"));
        verify(billingService).create(any(Billing.class));
    }

    @Test
    void updateBilling_ReturnsOk() throws Exception {
        Billing b = new Billing();
        b.setBillingId(9);
        b.setUserId("u9");
        b.setCourseId("c9");
        b.setEnrollmentId(null);
        b.setAmount(90);
        mockMvc.perform(put("/billings/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("BILLING UPDATED"));
        verify(billingService).update(eq(9), any(Billing.class));
    }

    @Test
    void deleteBilling_NotFound() throws Exception {
        when(billingService.findById(10)).thenReturn(null);
        mockMvc.perform(delete("/billings/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("BILLING NOT FOUND"));
    }

    @Test
    void deleteBilling_Success() throws Exception {
        Billing b = new Billing();
        b.setBillingId(11);
        b.setUserId("u11");
        b.setCourseId("c11");
        b.setEnrollmentId(null);
        b.setAmount(110);
        when(billingService.findById(11)).thenReturn(b);
        mockMvc.perform(delete("/billings/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("BILLING DELETED"));
        verify(billingService).remove(11);
    }

    @Test
    void patchBilling_ReturnsOk() throws Exception {
        Billing b = new Billing();
        b.setBillingId(12);
        b.setUserId("u12");
        b.setCourseId("c12");
        b.setEnrollmentId(null);
        b.setAmount(120);
        mockMvc.perform(patch("/billings/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(b)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("BILLING PARTIALLY UPDATED"));
        verify(billingService).partialUpdate(eq(12), any(Billing.class));
    }
}