package cl.edutech.evaluationservice.controller;

import cl.edutech.evaluationservice.controller.Response.MessageResponse;
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
    public ResponseEntity<MessageResponse> ping() {return ResponseEntity.ok(new MessageResponse("PONG"));
    }




}
