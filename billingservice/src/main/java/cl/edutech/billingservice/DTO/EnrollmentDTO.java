package cl.edutech.billingservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentDTO {
    private Integer id;
    private String userRut;
    private String courseId;
    private String message;
}
