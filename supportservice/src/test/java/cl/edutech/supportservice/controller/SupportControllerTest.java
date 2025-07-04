package cl.edutech.supportservice.controller;

import cl.edutech.supportservice.controller.SupportController;
import cl.edutech.supportservice.controller.response.MessageResponse;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.DTO.CourseDTO;
import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.service.SupportService;
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

class SupportControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private SupportService supportService;
    @InjectMocks
    private SupportController supportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(supportController).build();
    }

    @Test
    void ping_ReturnsPong() throws Exception {
        mockMvc.perform(get("/support/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("pong"));
    }

    @Test
    void getAllSupporttickets_NoContent() throws Exception {
        when(supportService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/support"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("No tickets found"));
    }

    @Test
    void getAllSupporttickets_Ok() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setId(1);
        t.setUserRut("u1");
        t.setCourseId("c1");
        t.setSubject("issue1");
        when(supportService.findAll()).thenReturn(Collections.singletonList(t));

        mockMvc.perform(get("/support"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getSupportTicketById_NotFound() throws Exception {
        when(supportService.findById(2)).thenReturn(null);
        mockMvc.perform(get("/support/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Ticket not found"));
    }

    @Test
    void getSupportTicketById_UserNotFound() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setId(3);
        t.setUserRut("u3");
        t.setCourseId("c3");
        t.setSubject("i3");
        when(supportService.findById(3)).thenReturn(t);
        when(supportService.getUser("u3")).thenReturn(null);

        mockMvc.perform(get("/support/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void getSupportTicketById_CourseNotFound() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setId(4);
        t.setUserRut("u4");
        t.setCourseId("c4");
        t.setSubject("i4");
        when(supportService.findById(4)).thenReturn(t);
        when(supportService.getUser("u4")).thenReturn(new UserDTO());
        when(supportService.getCourse("c4")).thenReturn(null);

        mockMvc.perform(get("/support/4"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void getSupportTicketById_Success() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setId(5);
        t.setUserRut("u5");
        t.setCourseId("c5");
        t.setSubject("i5");
        when(supportService.findById(5)).thenReturn(t);
        when(supportService.getUser("u5")).thenReturn(new UserDTO());
        when(supportService.getCourse("c5")).thenReturn(new CourseDTO());

        mockMvc.perform(get("/support/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }


    @Test
    void updateSupportTicket_ReturnsOk() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setUserRut("u7");
        t.setCourseId("c7");
        t.setSubject("i7");
        doNothing().when(supportService).update(eq(7), any(SupportTicket.class));
        mockMvc.perform(put("/support/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket updated"));
    }

    @Test
    void patchSupportTicket_ReturnsOk() throws Exception {
        SupportTicket t = new SupportTicket();
        t.setSubject("updated");
        doNothing().when(supportService).partialUpdate(eq(8), any(SupportTicket.class));
        mockMvc.perform(patch("/support/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket partially updated"));
    }

    @Test
    void removeSupportTicket_ReturnsOk() throws Exception {
        doNothing().when(supportService).remove(9);
        mockMvc.perform(delete("/support/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ticket removed"));
    }
}