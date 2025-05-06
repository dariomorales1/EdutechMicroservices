package cl.edutech.enrollmentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ENROLLMENT")
@Entity
public class Enrollment {

    @Id
    private Integer id;

    @Column
    private String studentRut;

    @Column
    private String courseId;
}
