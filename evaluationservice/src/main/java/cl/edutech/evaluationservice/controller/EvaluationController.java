package cl.edutech.evaluationservice.controller;

import cl.edutech.evaluationservice.DTO.EvaluationUserCourse;
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
    public ResponseEntity<?> getEvaluations() {
        List<Evaluation> evaluationsList = evaluationService.findAll();
        if (evaluationsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("No evaluations found"));
        }
        return ResponseEntity.ok(evaluationsList);
    }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<EvaluationUserCourse> searchEvaluation(@PathVariable String evaluationId) {
        Evaluation evaluation = evaluationService.findById(evaluationId);
        var user = evaluationService.getUser(evaluation.getStudentId());
        var course = evaluationService.getCourse(evaluation.getCourseId());

        EvaluationUserCourse evaluationUserCourse = new EvaluationUserCourse();
        evaluationUserCourse.setEvaluationId(evaluationId);
        evaluationUserCourse.setEvaluationName(evaluation.getEvaluationName());
        evaluationUserCourse.setScore(evaluation.getScore());
        evaluationUserCourse.setStudentId(user.getRut());
        evaluationUserCourse.setStudentFirstName(user.getFirstName());
        evaluationUserCourse.setStudentLastName(user.getLastName());
        evaluationUserCourse.setCourseId(course.getCourseId());
        return ResponseEntity.ok(evaluationUserCourse);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createEvaluation(@RequestBody Evaluation evaluation) {
        evaluationService.create(evaluation);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Evaluation created"));
    }

    @PutMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> updateEvaluation(@PathVariable String evaluationId, @RequestBody Evaluation evaluationRequest) {
        evaluationService.update(evaluationId, evaluationRequest);
        return ResponseEntity.ok(new MessageResponse("Evaluation updated"));
    }

    @PatchMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> patchEvaluation(@PathVariable String evaluationId, @RequestBody Evaluation evaluationRequest) {
        evaluationService.partialUpdate(evaluationId, evaluationRequest);
        return ResponseEntity.ok(new MessageResponse("Evaluation partially updated"));
    }

    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> deleteEvaluation(@PathVariable String evaluationId) {
        evaluationService.remove(evaluationId);
        return ResponseEntity.ok(new MessageResponse("Evaluation was deleted"));
    }
}
