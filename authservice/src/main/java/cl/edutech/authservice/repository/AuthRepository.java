package cl.edutech.authservice.repository;

import cl.edutech.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Token, String> {
}
