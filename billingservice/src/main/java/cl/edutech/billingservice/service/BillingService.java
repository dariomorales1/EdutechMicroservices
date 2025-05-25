package cl.edutech.billingservice.service;

import cl.edutech.billingservice.DTO.CourseDTO;
import cl.edutech.billingservice.DTO.EnrollmentDTO;
import cl.edutech.billingservice.DTO.UserDTO;
import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.repository.BillingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
public class BillingService {

    private final WebClient userWebClient;
    private final WebClient courseWebClient;
    private final WebClient enrollmentWebClient;

    public BillingService(WebClient userWebClient, WebClient courseWebClient, WebClient enrollmentWebClient) {
        this.userWebClient = userWebClient;
        this.courseWebClient = courseWebClient;
        this.enrollmentWebClient = enrollmentWebClient;
    }

    public UserDTO getUser(String rutRequest) {
        UserDTO user = userWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{rutRequest}").build(rutRequest))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).then(Mono.empty())
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
        return user;
    }

    public CourseDTO getCourse(String courseIdRequest) {
        CourseDTO course = courseWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{courseIdRequest}").build(courseIdRequest))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).then(Mono.empty())
                )
                .bodyToMono(CourseDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
        return course;
    }

    public EnrollmentDTO getEnrollment(Integer enrollmentIdRequest) {
        EnrollmentDTO enrollment = enrollmentWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{enrollmentIdRequest}").build(enrollmentIdRequest))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).then(Mono.empty())
                )
                .bodyToMono(EnrollmentDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
        return enrollment;
    }




    @Autowired
    private BillingRepository billingRepository;



    public List<Billing> findAll() {
        return billingRepository.findAll();
    }

    public Billing findById(Integer id) {return billingRepository.findById(id).orElse(null);}

    public Billing create(Billing billing) {
        return billingRepository.save(billing);
    }

    public void remove (Integer id) {
        billingRepository.deleteById(id);
    }

}
