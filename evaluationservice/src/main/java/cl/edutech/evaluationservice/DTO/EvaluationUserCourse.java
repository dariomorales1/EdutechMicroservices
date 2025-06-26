package cl.edutech.evaluationservice.DTO;

import lombok.Data;

@Data
public class EvaluationUserCourse {
    private String evaluationId;
    private String evaluationName;
    private Double score;
    private String studentId;
    private String studentFirstName;
    private String studentLastName;
    private String courseId;
}
