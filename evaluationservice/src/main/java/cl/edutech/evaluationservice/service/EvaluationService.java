package cl.edutech.evaluationservice.service;

import cl.edutech.evaluationservice.model.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/evaluations")

public class EvaluationService {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping
    public Evaluation createEvalaution(@RequestBody Evaluation evaluation) {
        return evaluationService.saveEvaluation(evaluation);
    }

    @GetMapping
    public List <Evaluation> getEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/{id}")
        public Optional<Evaluation> getEvaluationById(@PathVariable String id){
        return evaluationService.getEvaluationById(id);
        }

    @GetMapping("/name/{name}")
    public List <Evaluation> getEvaluationsByName(@PathVariable String name){
        return evaluationService.getEvaluationsByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteEvaluation(@)
}
