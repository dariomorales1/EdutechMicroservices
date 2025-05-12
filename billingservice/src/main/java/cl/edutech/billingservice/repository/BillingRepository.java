package cl.edutech.billingservice.repository;

import cl.edutech.billingservice.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Integer> {
}