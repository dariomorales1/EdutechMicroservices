package cl.edutech.enrollmentservice.DTO;

import lombok.Data;

@Data
public class EnrollmentUserCourseDTO {
    private Integer id;
    private String rut;
    private String email;
    private String password;
    private String courseId;
    private String nameCourse;
}

