package cl.edutech.supportservice.service;

import cl.edutech.supportservice.DTO.CourseDTO;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.repository.SupportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
public class SupportService {

    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public SupportService(WebClient userWebClient, WebClient courseWebClient) {
        this.userWebClient = userWebClient;
        this.courseWebClient = courseWebClient;
    }

    public UserDTO getUser(String rutRequest) {
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{rutRequest}").build(rutRequest))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).then(Mono.empty())
                )
                .bodyToMono(UserDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    public CourseDTO getCourse(String courseIdRequest) {
        return courseWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/{courseIdRequest}").build(courseIdRequest))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class).then(Mono.empty())
                )
                .bodyToMono(CourseDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }


    @Autowired
    private SupportRepository supportRepository;



    public List<SupportTicket> findAll() {
        return supportRepository.findAll();
    }

    public SupportTicket findById(Integer id) {
        return supportRepository.findById(id).orElse(null);
    }

    public SupportTicket create(SupportTicket supportTicket) {
        return supportRepository.save(supportTicket);
    }

    public void remove(Integer id) {
        supportRepository.deleteById(id);
    }
}
