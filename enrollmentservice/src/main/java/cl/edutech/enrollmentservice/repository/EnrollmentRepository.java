package cl.edutech.enrollmentservice.repository;

import cl.edutech.enrollmentservice.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
