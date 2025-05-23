package cl.edutech.evaluationservice.controller;

import cl.edutech.evaluationservice.DTO.CourseDTO;
import cl.edutech.evaluationservice.DTO.EvaluationUserCourse;
import cl.edutech.evaluationservice.DTO.UserDTO;
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
    public ResponseEntity<?> searchEvaluation(@PathVariable String evaluationId) {
        Evaluation evaluation = evaluationService.findById(evaluationId);
        UserDTO user = evaluationService.getUser(evaluation.getStudentId());
        CourseDTO course = evaluationService.getCourse(evaluation.getCourseId());
        EvaluationUserCourse evaluationUserCourse = new EvaluationUserCourse();

        if (evaluation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No evaluation found"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No user found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No course found"));
        } else {
            evaluationUserCourse.setEvaluationId(evaluationId);
            evaluationUserCourse.setEvaluationName(evaluation.getEvaluationName());
            evaluationUserCourse.setScore(evaluation.getScore());
            evaluationUserCourse.setStudentId(user.getRut());
            evaluationUserCourse.setStudentFirstName(user.getFirstName());
            evaluationUserCourse.setStudentLastName(user.getLastName());
            evaluationUserCourse.setCourseId(course.getCourseId());
            return ResponseEntity.ok(evaluationUserCourse);
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createEvaluation(@RequestBody Evaluation evaluation) {
        Evaluation evaluationExist = evaluationService.findById(evaluation.getEvaluationId());
        UserDTO user = evaluationService.getUser(evaluation.getStudentId());
        CourseDTO course = evaluationService.getCourse(evaluation.getCourseId());

        if (evaluationExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Evaluation already exists"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No user found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No course found"));
        }
        evaluationService.create(evaluation);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Evaluation created"));
    }

    @PutMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> updateEvaluation(@PathVariable String evaluationId, @RequestBody Evaluation evaluationRequest) {
        Evaluation evaluationExist = evaluationService.findById(evaluationId);
        UserDTO user = evaluationService.getUser(evaluationRequest.getStudentId());
        CourseDTO course = evaluationService.getCourse(evaluationRequest.getCourseId());

        if (evaluationExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No evaluation found"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No user found"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No course found"));
        }
        evaluationService.remove(evaluationId);
        evaluationService.create(evaluationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Evaluation updated"));

    }
    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<MessageResponse> deleteEvaluation(@PathVariable String evaluationId) {
        Evaluation evaluationExist = evaluationService.findById(evaluationId);

        if (evaluationExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Evaluation not found"));
        }
        evaluationService.remove(evaluationId);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Evaluation was deleted"));
    }
}



