package cl.edutech.billingservice.controller;

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
    public ResponseEntity<List<Billing>> getAllBillings() {
        List<Billing> billingList = billingService.findAll();
        if (billingList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(billingList);
    }

    @GetMapping("/{billingId}")
    public ResponseEntity<Billing> searchBilling(@PathVariable Integer billingId) {
        try {
            Billing billing = billingService.findById(billingId);
            return ResponseEntity.ok(billing);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createBilling(@RequestBody Billing billing) {
        List<Billing> billingList = billingService.findAll();
        for (Billing existingBilling : billingList) {
            if(billing.getBillingId().equals(existingBilling.getBillingId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("BILLING ALREADY EXISTS"));
            }
        }
        billingService.create(billing);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("BILLING CREATED"));
    }

    @PutMapping("/{billingId}")
    public ResponseEntity<MessageResponse> updateBilling(@PathVariable Integer billingId, @RequestBody Billing billingRequest) {
        List<Billing> billingList = billingService.findAll();
        for (Billing billing : billingList){
            if(billing.getBillingId().equals(billingId)){
                billingService.remove(billingId);
                billingService.create(billingRequest);
                return ResponseEntity.ok(new MessageResponse("BILLING UPDATED"));
            } else{
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("BILLING NOT FOUND"));
    }

    @DeleteMapping("/{billingId}")
    public ResponseEntity<MessageResponse> deleteBilling(@PathVariable Integer billingId) {
        List<Billing> billingList = billingService.findAll();
        for (Billing billing : billingList){
            if(billing.getBillingId().equals(billingId)){
                billingService.remove(billingId);
                return ResponseEntity.ok(new MessageResponse("BILLING REMOVED"));
            } else{
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("BILLING NOT FOUND"));
    }
}
