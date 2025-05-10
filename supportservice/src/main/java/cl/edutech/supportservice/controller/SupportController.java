package cl.edutech.supportservice.controller;

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

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() {
        return ResponseEntity.ok(new MessageResponse("pong"));
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllSupporttickets() {
        List<SupportTicket> ticketList = supportService.findAll();
        if (ticketList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketList);
    }

    @GetMapping("/{idRequest}")
    public ResponseEntity<SupportTicket> getSupportTicketById(@PathVariable Integer idRequest) {
        SupportTicket ticket = supportService.findById(idRequest);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createSupportTicket(@RequestBody SupportTicket ticket) {
        SupportTicket validation = supportService.findById(ticket.getId());
        // Valida si el ticket ya existe
        if (validation != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Ticket already exists"));
        }

        // Valida si el usuario existe
        boolean isValidUser = supportService.validateUser(ticket.getUserRut());
        if (!isValidUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User does not exist"));
        }

        SupportTicket createdTicket = supportService.create(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Ticket created"));
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateSupportTicket(@PathVariable Integer idRequest, @RequestBody SupportTicket ticketRequest) {
        SupportTicket validation = supportService.findById(idRequest);
        if (validation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Ticket not found"));
        }
        supportService.remove(idRequest);
        supportService.create(ticketRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Ticket updated"));
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
