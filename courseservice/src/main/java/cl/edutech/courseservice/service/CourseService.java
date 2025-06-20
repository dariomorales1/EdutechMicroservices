package cl.edutech.courseservice.service;

import cl.edutech.courseservice.model.Course;
import cl.edutech.courseservice.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.edutech.courseservice.exception.NotFoundException;
import cl.edutech.courseservice.exception.ConflictException;

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
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
    }

    public boolean existsByCourseId(String id) {
        return courseRepository.existsById(id);
    }

    public Course create(Course course) {
        if (courseRepository.existsById(course.getCourseId())) {
            throw new ConflictException("A course with this ID already exists: " + course.getCourseId());
        }
        return courseRepository.save(course);
    }

    public void update(String id, Course courseRequest) {
        Course existing = findById(id);

        existing.setNameCourse(courseRequest.getNameCourse());
        existing.setDescription(courseRequest.getDescription());
        existing.setPrice(courseRequest.getPrice());

        courseRepository.save(existing);
    }

    public void partialUpdate(String id, Course courseRequest) {
        Course existing = findById(id);

        if (courseRequest.getNameCourse() != null) {
            existing.setNameCourse(courseRequest.getNameCourse());
        }
        if (courseRequest.getDescription() != null) {
            existing.setDescription(courseRequest.getDescription());
        }
        if (courseRequest.getPrice() != null) {
            existing.setPrice(courseRequest.getPrice());
        }

        courseRepository.save(existing);
    }

    public void remove(String id) {

        if (!courseRepository.existsById(id)) {
            throw new NotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}
