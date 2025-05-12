package cl.edutech.evaluationservice.repository;

import  org.springframework.data.jpa.repository.JpaRepository;
import cl.edutech.evaluationservice.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, String> {
}
