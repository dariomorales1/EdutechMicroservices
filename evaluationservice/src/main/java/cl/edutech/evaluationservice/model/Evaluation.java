package cl.edutech.evaluationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evaluations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Evaluation {
    @Id
    private String evaluationId;

    @Column
    private String evaluationName;

    @Column
    private String StudentId;

    @Column
    private String courseId;

    @Column
    private Double score;

}
