package cl.edutech.supportservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportUserCourseDTO {
    private Integer id;
    private String userRut;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String courseId;
    private String courseName;
    private String message;
}
