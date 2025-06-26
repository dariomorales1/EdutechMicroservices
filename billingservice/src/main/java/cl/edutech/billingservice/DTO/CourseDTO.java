package cl.edutech.billingservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private String courseId;
    private String nameCourse;
    private String title;
    private String description;
    private String imageName;
    private Integer price;
}
