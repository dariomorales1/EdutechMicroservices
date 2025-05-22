package cl.edutech.enrollmentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentUserCourseDTO {
    private Integer id;
    private String rut;
    private String email;
    private String password;
    private String courseId;
    private String nameCourse;
}
