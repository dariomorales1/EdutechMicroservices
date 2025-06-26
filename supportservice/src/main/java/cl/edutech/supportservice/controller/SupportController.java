package cl.edutech.supportservice.controller;

import cl.edutech.supportservice.DTO.CourseDTO;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.controller.response.MessageResponse;
import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() {
        return ResponseEntity.ok(new MessageResponse("pong"));
    }

    @GetMapping
    public ResponseEntity<?> getAllSupporttickets() {
        List<SupportTicket> ticketList = supportService.findAll();
        if (ticketList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("No tickets found"));
        }
        return ResponseEntity.ok(ticketList);
    }

    @GetMapping("/{idRequest}")
    public ResponseEntity<?> getSupportTicketById(@PathVariable Integer idRequest) {
        SupportTicket ticketExists = supportService.findById(idRequest);
        if (ticketExists == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Ticket not found"));
        }
        UserDTO user = supportService.getUser(ticketExists.getUserRut());
        CourseDTO course = supportService.getCourse(ticketExists.getCourseId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course not found"));
        }
        return ResponseEntity.ok(ticketExists);
    }

    @PostMapping
    public ResponseEntity<?> createSupportTicket(@RequestBody SupportTicket ticket) {
        supportService.create(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Ticket created"));
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateSupportTicket(@PathVariable Integer idRequest, @RequestBody SupportTicket ticketRequest) {
        supportService.update(idRequest, ticketRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ticket updated"));
    }

    @PatchMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> patchSupportTicket(@PathVariable Integer idRequest, @RequestBody SupportTicket ticketRequest) {
        supportService.partialUpdate(idRequest, ticketRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ticket partially updated"));
    }

    @DeleteMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> removeSupportTicket(@PathVariable Integer idRequest) {
        supportService.remove(idRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ticket removed"));
    }
}
