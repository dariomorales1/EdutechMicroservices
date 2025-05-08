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
    private String nameEvaluation;

    @Column
    private String description;

//    @Column
//    private String date;

    @Column
    private String courseId;
}
