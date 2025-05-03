package com.example.courseservice.controller;

import cl.edutech.userservice.controller.Response.MessageResponse;
import cl.edutech.userservice.model.Corurse;
import cl.edutech.userservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class CourseController {

    @Autowired
    private CourseService courseService;



}
