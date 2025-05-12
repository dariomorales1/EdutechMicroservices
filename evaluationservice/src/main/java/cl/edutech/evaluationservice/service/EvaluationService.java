package cl.edutech.evaluationservice.service;

import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.repository.EvaluationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public List<Evaluation> findAll(){return evaluationRepository.findAll();}

    public Evaluation findById(String id){return evaluationRepository.findById(id).get();}

    public Evaluation create(Evaluation evaluation){return evaluationRepository.save(evaluation);}

    public void remove (String id){evaluationRepository.deleteById(id);}
}
