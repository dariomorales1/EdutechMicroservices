package cl.edutech.courseservice.controller;

import cl.edutech.courseservice.controller.Response.MessageResponse;
import cl.edutech.courseservice.model.Course;
import cl.edutech.courseservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/ping")
    public ResponseEntity<MessageResponse> ping() {
        return ResponseEntity.ok(new MessageResponse("PONG"));
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courseList = courseService.findAll();
        if (courseList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> searchCourse(@PathVariable String courseId) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createCourse(@RequestBody Course course) {
        if (courseService.existsByCourseId(course.getCourseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("COURSE ALREADY EXISTS"));
        }
        courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("COURSE CREATED"));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<MessageResponse> updateCourse(@PathVariable String courseId, @RequestBody Course courseRequest) {
        if (!courseService.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("COURSE NOT FOUND"));
        }
        courseService.update(courseId, courseRequest);
        return ResponseEntity.ok(new MessageResponse("COURSE UPDATED"));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable String courseId) {
        if (!courseService.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("COURSE NOT FOUND"));
        }
        courseService.remove(courseId);
        return ResponseEntity.ok(new MessageResponse("COURSE DELETED"));
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<MessageResponse> patchCourse(
            @PathVariable String courseId,
            @RequestBody Course courseRequest) {
        courseService.partialUpdate(courseId, courseRequest);
        return ResponseEntity.ok(new MessageResponse("COURSE PARTIALLY UPDATED"));
    }
}
