package cl.edutech.evaluationservice.service;

import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.repository.EvaluationRepository;
import cl.edutech.evaluationservice.DTO.UserDTO;
import cl.edutech.evaluationservice.DTO.CourseDTO;
import cl.edutech.evaluationservice.DTO.EvaluationUserCourse;
import cl.edutech.evaluationservice.exception.ConflictException;
import cl.edutech.evaluationservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public List<Evaluation> findAll() {
        return evaluationRepository.findAll();
    }

    public Evaluation findById(String evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new NotFoundException("No evaluation found"));
    }

    public Evaluation create(Evaluation evaluation) {
        if (evaluationRepository.existsByEvaluationId(evaluation.getEvaluationId())) {
            throw new ConflictException("Evaluation already exists");
        }
        if (getUser(evaluation.getStudentId()) == null) {
            throw new NotFoundException("No user found");
        }
        if (getCourse(evaluation.getCourseId()) == null) {
            throw new NotFoundException("No course found");
        }
        return evaluationRepository.save(evaluation);
    }

    public void remove(String evaluationId) {
        if (!evaluationRepository.existsByEvaluationId(evaluationId)) {
            throw new NotFoundException("Evaluation not found");
        }
        evaluationRepository.deleteById(evaluationId);
    }

    public void update(String evaluationId, Evaluation evaluationRequest) {
        Evaluation existing = findById(evaluationId);

        if (getUser(evaluationRequest.getStudentId()) == null) {
            throw new NotFoundException("No user found");
        }
        if (getCourse(evaluationRequest.getCourseId()) == null) {
            throw new NotFoundException("No course found");
        }

        existing.setEvaluationName(evaluationRequest.getEvaluationName());
        existing.setScore(evaluationRequest.getScore());
        existing.setStudentId(evaluationRequest.getStudentId());
        existing.setCourseId(evaluationRequest.getCourseId());

        evaluationRepository.save(existing);
    }

    public void partialUpdate(String evaluationId, Evaluation evaluationRequest) {
        Evaluation existing = findById(evaluationId);

        if (evaluationRequest.getEvaluationName() != null)
            existing.setEvaluationName(evaluationRequest.getEvaluationName());
        if (evaluationRequest.getScore() != null)
            existing.setScore(evaluationRequest.getScore());
        if (evaluationRequest.getStudentId() != null) {
            if (getUser(evaluationRequest.getStudentId()) == null)
                throw new NotFoundException("No user found");
            existing.setStudentId(evaluationRequest.getStudentId());
        }
        if (evaluationRequest.getCourseId() != null) {
            if (getCourse(evaluationRequest.getCourseId()) == null)
                throw new NotFoundException("No course found");
            existing.setCourseId(evaluationRequest.getCourseId());
        }
        evaluationRepository.save(existing);
    }

    public UserDTO getUser(String studentId) {
        try {
            return userWebClient.get()
                    .uri("/{rut}", studentId)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public CourseDTO getCourse(String courseId) {
        try {
            return courseWebClient.get()
                    .uri("/{courseId}", courseId)
                    .retrieve()
                    .bodyToMono(CourseDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
