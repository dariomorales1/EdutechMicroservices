package cl.edutech.enrollmentservice.controller;

import cl.edutech.enrollmentservice.DTO.CourseDTO;
import cl.edutech.enrollmentservice.DTO.EnrollmentUserCourseDTO;
import cl.edutech.enrollmentservice.DTO.UserDTO;
import cl.edutech.enrollmentservice.controller.response.MessageResponse;
import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    // Consumo desde enrollment hacia course y user

    @GetMapping("/EnrollUser/{idRequest}")
    public ResponseEntity<?> EnrollUserData(@PathVariable Integer idRequest) {
        try {
            Enrollment enroll = enrollmentService.findById(idRequest);
            UserDTO userEnroll = enrollmentService.getUser(enroll.getStudentRut());
            CourseDTO courseEnroll = enrollmentService.getCourse(enroll.getCourseId());

            // Aqui Creamos el EnrollmentUserCourseDTO
            EnrollmentUserCourseDTO enrollUserCourse = new EnrollmentUserCourseDTO();

            //Aqui les asignamos los datos obtenidos con los DTOs de enroll userEnroll y courseEnroll
            enrollUserCourse.setId(enroll.getId());
            enrollUserCourse.setRut(userEnroll.getRut());
            enrollUserCourse.setEmail(userEnroll.getEmail());
            enrollUserCourse.setPassword(userEnroll.getPassword());
            enrollUserCourse.setCourseId(courseEnroll.getCourseId());
            enrollUserCourse.setNameCourse(courseEnroll.getNameCourse());
            return ResponseEntity.status(HttpStatus.OK).body(enrollUserCourse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User or Course not found!"));
        }

    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments(Integer id) {
        List<Enrollment> enrollmentList = enrollmentService.findAll();
        if (enrollmentList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enrollmentList);
    }

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody Enrollment enrollment) {
        Enrollment enrollExist = enrollmentService.findById(enrollment.getId());
        UserDTO user = enrollmentService.getUser(enrollment.getStudentRut());
        CourseDTO course = enrollmentService.getCourse(enrollment.getCourseId());
        if (enrollExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("ENROLLMENT ALREADY EXISTS"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
        }
        enrollmentService.create(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("ENROLLMENT CREATED"));
    }

    @GetMapping("/ping-user")
    public ResponseEntity<MessageResponse> pingUserService() {
        ResponseEntity<String> userResponse = enrollmentService.pingUserService();
        if (userResponse.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(new MessageResponse("PONG"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("USER SERVICE NOT AVAILABLE"));
        }
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateEnrollment(@PathVariable Integer idRequest, @RequestBody Enrollment enrollment) {
        Enrollment enrollExist = enrollmentService.findById(idRequest);
        UserDTO user = enrollmentService.getUser(enrollment.getStudentRut());
        CourseDTO course = enrollmentService.getCourse(enrollment.getCourseId());
        if (enrollExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
        } else if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USER NOT FOUND"));
        } else if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("COURSE NOT FOUND"));
        } else {
            enrollmentService.remove(idRequest);
            enrollmentService.create(enrollment);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("ENROLLMENT UPDATED"));
        }
    }

    @DeleteMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> deleteEnrollment(@PathVariable Integer idRequest) {
        Enrollment enrollExist = enrollmentService.findById(idRequest);
        if (enrollExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
        } else {
            enrollmentService.remove(idRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("ENROLLMENT DELETED"));
        }
    }
}




