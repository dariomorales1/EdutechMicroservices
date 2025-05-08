package cl.edutech.enrollmentservice.service;

import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Integer id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    public Enrollment create(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void remove(Integer id) {
        enrollmentRepository.deleteById(id);
    }
}