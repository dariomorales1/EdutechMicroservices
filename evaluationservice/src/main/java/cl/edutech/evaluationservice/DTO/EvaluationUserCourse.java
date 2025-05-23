package cl.edutech.evaluationservice.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationUserCourse {
    private String evaluationId;
    private String evaluationName;
    private Double score;
    private String StudentId;
    private String StudentFirstName;
    private String StudentLastName;
    private String courseId;
}
