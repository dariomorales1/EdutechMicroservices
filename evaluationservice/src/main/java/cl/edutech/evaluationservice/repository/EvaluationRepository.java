package cl.edutech.evaluationservice.repository;

import cl.edutech.evaluationservice.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {
    boolean existsByEvaluationId(String evaluationId);
}
