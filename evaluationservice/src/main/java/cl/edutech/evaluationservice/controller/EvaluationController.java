package cl.edutech.evaluationservice.controller;

import cl.edutech.evaluationservice.controller.Response.MessageResponse;
import cl.edutech.evaluationservice.model.Evaluation;
import cl.edutech.evaluationservice.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() {
        return ResponseEntity.ok(new MessageResponse("PONG"));
    }

    @GetMapping
    public ResponseEntity<List<Evaluation>> getEvaluations() {
        List<Evaluation> evaluationsList = evaluationService.findAll();
        if (evaluationsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(evaluationsList);
    }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<Evaluation> searchEvaluation(@PathVariable String evaluationId) {
        try {
            Evaluation evaluation = evaluationService.findById(evaluationId);
            return ResponseEntity.ok(evaluation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/Create")
    public ResponseEntity<MessageResponse> createEvaluation(@RequestBody Evaluation evaluation) {
        List<Evaluation> evaluationList = evaluationService.findAll();
        for (Evaluation existingEvaluation : evaluationList) {
            if (evaluation.getEvaluationId().equals(existingEvaluation.getEvaluationId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Evaluation already exists"));
            }
        }
        evaluationService.create(evaluation);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Evaluation created"));
    }

    @PutMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> updateEvaluation(@PathVariable String evaluationId, @RequestBody Evaluation evaluationRequest) {
        List<Evaluation> evaluationList = evaluationService.findAll();
        for (Evaluation evaluation : evaluationList) {
            if (evaluation.getEvaluationId().equals(evaluationId)) {
                evaluationService.remove(evaluationId);
                evaluationService.create(evaluationRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Evaluation Updated"));
            } else {
                break;

            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evaluation NOT FOUND"));
    }
    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> deleteEvaluation(@PathVariable String evaluationId){
        List<Evaluation> evaluationList = evaluationService.findAll();
        for (Evaluation evaluation : evaluationList){
            if(evaluation.getEvaluationId().equals(evaluationId)) {
                evaluationService.remove(evaluationId);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("EVALUATION DELETED"));
            } else {
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("EVALUATION NOT FOUND"));
    }
}



