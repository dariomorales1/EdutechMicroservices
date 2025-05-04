package cl.edutech.enrollmentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ENROLLMENT")
public class Enrollment {

    @Id
    private String id;

    @Column (nullable = false)
    private String studentRun;

    @Column (nullable = false)
    private String courseId;

    @Column (nullable = false)
    private Date enrollmentDate;
}
