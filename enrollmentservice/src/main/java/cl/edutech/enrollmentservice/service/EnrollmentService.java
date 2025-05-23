package cl.edutech.enrollmentservice.service;

import cl.edutech.enrollmentservice.DTO.CourseDTO;
import cl.edutech.enrollmentservice.DTO.UserDTO;
import cl.edutech.enrollmentservice.controller.response.MessageResponse;
import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public EnrollmentService(WebClient userWebClient, WebClient courseWebClient) {
        this.userWebClient = userWebClient;
        this.courseWebClient = courseWebClient;
    }

    public UserDTO getUser(String rutRequest) {
        UserDTO user = userWebClient.get()
                            .uri(uriBuilder -> uriBuilder.path("/{rutRequest}").build(rutRequest))
                            .retrieve()
                            .bodyToMono(UserDTO.class)
                            .block();
        return user;
    }

    public CourseDTO getCourse(String courseIdRequest) {
        CourseDTO course = courseWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{courseIdRequest}").build(courseIdRequest))
                .retrieve()
                .bodyToMono(CourseDTO.class)
                .block();
        return course;
    }

    public ResponseEntity<String> pingUserService() {
        return userWebClient.get()
                .uri("/ping")
                .retrieve()
                .toEntity(String.class)
                .block();
    }


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