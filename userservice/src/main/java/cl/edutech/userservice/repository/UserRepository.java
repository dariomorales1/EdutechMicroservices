package cl.edutech.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.edutech.userservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {

}
