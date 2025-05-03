package com.example.courseservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Course {

    @Id
    private String courseId;

    @Column
    private String nameCourse;

    @Column
    private String description;

    @Column
    private Date date;

    @Column
    private String instructorId;

}
