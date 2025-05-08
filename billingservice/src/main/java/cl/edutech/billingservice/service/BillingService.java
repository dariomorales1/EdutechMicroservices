package cl.edutech.billingservice.service;

import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.repository.BillingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    public List<Billing> findAll() {
        return billingRepository.findAll();
    }

    public Billing findById(Integer id) {
        return billingRepository.findById(id).get();
    }

    public Billing create(Billing billing) {
        return billingRepository.save(billing);
    }

    public void remove (Integer id) {
        billingRepository.deleteById(id);
    }

}
