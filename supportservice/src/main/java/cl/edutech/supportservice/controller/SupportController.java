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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        SupportTicket ticketExist = supportService.findById(ticket.getId());
        UserDTO user = supportService.getUser(ticket.getUserRut());
        CourseDTO course = supportService.getCourse(ticket.getCourseId());
        if (ticketExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Ticket already exists"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course not found"));
        }
        supportService.create(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Ticket created"));
    }

    // -> Aqui voy

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateSupportTicket(@PathVariable Integer idRequest, @RequestBody SupportTicket ticketRequest) {
        SupportTicket ticketExist = supportService.findById(idRequest);
        UserDTO user = supportService.getUser(ticketRequest.getUserRut());
        CourseDTO course = supportService.getCourse(ticketRequest.getCourseId());
        if (ticketExist == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Ticket not found"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User not found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Course not found"));
        }
        supportService.remove(idRequest);
        supportService.create(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Ticket updated"));
    }

    @DeleteMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> removeSupportTicket(@PathVariable Integer idRequest) {
        SupportTicket validation = supportService.findById(idRequest);
        if (validation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Ticket not found"));
        }
        supportService.remove(idRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ticket removed"));
    }
}
