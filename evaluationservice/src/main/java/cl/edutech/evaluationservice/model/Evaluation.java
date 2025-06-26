package cl.edutech.evaluationservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Evaluation {
    @Id
    private String evaluationId;

    @Column(nullable = false)
    private String evaluationName;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String courseId;
}
