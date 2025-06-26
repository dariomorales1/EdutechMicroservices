package cl.edutech.supportservice.repository;

import cl.edutech.supportservice.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<SupportTicket, Integer> {
    boolean existsById(Integer id);
}
