package cl.edutech.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.edutech.courseservice.model.Course;

public interface CourseRepository extends JpaRepository<Course, String> {

}
