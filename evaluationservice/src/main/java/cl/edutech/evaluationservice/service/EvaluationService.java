package cl.edutech.evaluationservice.service;

import cl.edutech.evaluationservice.DTO.CourseDTO;
import cl.edutech.evaluationservice.DTO.UserDTO;
import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.repository.EvaluationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
public class EvaluationService {

    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public EvaluationService(WebClient userWebClient, WebClient courseWebClient) {
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


    @Autowired
    private EvaluationRepository evaluationRepository;



    public List<Evaluation> findAll(){return evaluationRepository.findAll();}

    public Evaluation findById(String id){return evaluationRepository.findById(id).orElse(null);}

    public Evaluation create(Evaluation evaluation){return evaluationRepository.save(evaluation);}

    public void remove (String id){evaluationRepository.deleteById(id);}
}
