package com.example.courseservice.controller;

import com.example.courseservice.controller.Response.MessageResponse;
import com.example.courseservice.model.Course;
import com.example.courseservice.service.CourseService;
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
    public ResponseEntity<MessageResponse> ping() { return ResponseEntity.ok(new MessageResponse("PONG"));
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
        try {
            Course course = courseService.findById(courseId);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createCourse(@RequestBody Course course) {
        List<Course> courseList = courseService.findAll();
        for (Course existingCourse : courseList) {
            if(course.getCourseId().equals(existingCourse.getCourseId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("COURSE ALREADY EXISTS"));
            }
        }
        courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("COURSE CREATED"));

    }

    @PutMapping("/{courseId}")
    public ResponseEntity<MessageResponse> updateCourse(@PathVariable String courseId, @RequestBody Course courseRequest) {
        List<Course> courseList = courseService.findAll();
        for (Course course : courseList){
            if(course.getCourseId().equals(courseId)){
                courseService.remove(courseId);
                courseService.create(courseRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("COURSE UPDATED"));
            }  else {
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<MessageResponse> deleteCourse(@PathVariable String courseId) {
        List<Course> courseList = courseService.findAll();
        for (Course course : courseList){
            if(course.getCourseId().equals(courseId)){
                courseService.remove(courseId);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("COURSE DELETED"));
            }  else {
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
    }
}
