package cl.edutech.billingservice.controller;

import cl.edutech.billingservice.DTO.CourseDTO;
import cl.edutech.billingservice.DTO.EnrollmentDTO;
import cl.edutech.billingservice.DTO.UserDTO;
import cl.edutech.billingservice.controller.response.MessageResponse;
import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billings")

public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() { return ResponseEntity.ok(new MessageResponse("PONG"));
    }

    @GetMapping
    public ResponseEntity<List<Billing>> getAllBillings(Integer id) {
        List<Billing> billingList = billingService.findAll();
        if (billingList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(billingList);
    }

    @GetMapping("/{billingId}")
    public ResponseEntity<?> searchBilling(@PathVariable Integer billingId) {
        Billing billingExists = billingService.findById(billingId);
        if (billingExists == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("BILLING NOT FOUND"));
        }

        UserDTO user = billingService.getUser(billingExists.getUserId());
        CourseDTO course = billingService.getCourse(billingExists.getCourseId());
        EnrollmentDTO enroll = billingService.getEnrollment(billingExists.getEnrollmentId());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
        } else if (enroll == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
        }
        return ResponseEntity.ok(billingExists);

    }

    @PostMapping
    public ResponseEntity<?> createBilling(@RequestBody Billing billing) {
        Billing billingExists = billingService.findById(billing.getBillingId());
        if (billingExists != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("BILLING EXISTS"));
        }
        UserDTO user = billingService.getUser(billing.getUserId());
        CourseDTO course = billingService.getCourse(billing.getCourseId());
        EnrollmentDTO enroll = billingService.getEnrollment(billing.getEnrollmentId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
        } else if (enroll == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
        }
        billingService.create(billing);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("BILLING CREATED"));
    }

    @PutMapping("/{billingId}")
    public ResponseEntity<MessageResponse> updateBilling(@PathVariable Integer billingId, @RequestBody Billing billingRequest) {
        Billing billingExists = billingService.findById(billingId);
        if (billingExists == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("BILLING NOT FOUND"));
        }
        UserDTO user = billingService.getUser(billingRequest.getUserId());
        CourseDTO course = billingService.getCourse(billingRequest.getCourseId());
        EnrollmentDTO enroll = billingService.getEnrollment(billingRequest.getEnrollmentId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
        } else if (enroll == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
        } else {
            billingService.remove(billingId);
            billingService.create(billingRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("BILLING UPDATED"));
        }
    }

    @DeleteMapping("/{billingId}")
    public ResponseEntity<MessageResponse> deleteBilling(@PathVariable Integer billingId) {
        Billing billingExists = billingService.findById(billingId);
        if (billingExists == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("BILLING NOT FOUND"));
        }
        billingService.remove(billingId);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("BILLING DELETED"));

    }
}
