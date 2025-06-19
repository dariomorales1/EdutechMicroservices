package cl.edutech.courseservice.service;

import cl.edutech.courseservice.model.Course;
import cl.edutech.courseservice.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    public boolean existsByCourseId(String id) {
        return courseRepository.existsById(id);
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public void update(String id, Course courseRequest) {
        Course existing = findById(id);
        if (existing == null) throw new RuntimeException("Course not found");

        existing.setNameCourse(courseRequest.getNameCourse());
        existing.setDescription(courseRequest.getDescription());
        existing.setPrice(courseRequest.getPrice());

        courseRepository.save(existing);
    }

    public void remove(String id) {
        courseRepository.deleteById(id);
    }
}
