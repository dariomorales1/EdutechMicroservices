package cl.edutech.enrollmentservice.service;

import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateUserRut(String rutRequest) {
        String userServiceUrl = "http://localhost:8082/users/validate/rut?rutRequest=" + rutRequest;

        try {
            Boolean isValid = restTemplate.postForObject(userServiceUrl,null, Boolean.class);
            return isValid != null && isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    FALTA LA PARTE DEL FELIPE PARA SEGUIR

    public boolean validateCourseId(String courseIdRequest) {
        String userServiceUrl = "http://localhost:8082/course/validate/rut?courseIdRequest=" + courseIdRequest;

        try {
            Boolean isValid = restTemplate.postForObject(CourseServiceUrl,null, Boolean.class);
            return isValid != null && isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    } */

    public String pingUserService() {
        String userServiceUrl = "http://localhost:8082/users/ping";
        try {
            return restTemplate.getForObject(userServiceUrl, String.class);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

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
