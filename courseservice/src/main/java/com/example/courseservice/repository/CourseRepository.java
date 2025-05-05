package com.example.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.courseservice.model.Course;

public interface CourseRepository extends JpaRepository<Course, String> {

}
