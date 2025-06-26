package cl.edutech.enrollmentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enrollment {
    @Id
    private Integer id;
    @Column(nullable = false)
    private String userRut;
    @Column(nullable = false)
    private String courseId;
}
