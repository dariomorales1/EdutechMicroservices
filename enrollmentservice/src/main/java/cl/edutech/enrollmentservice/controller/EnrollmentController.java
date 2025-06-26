package cl.edutech.enrollmentservice.controller;

import cl.edutech.enrollmentservice.DTO.EnrollmentUserCourseDTO;
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

    @GetMapping("/{idRequest}")
    public ResponseEntity<EnrollmentUserCourseDTO> EnrollUserData(@PathVariable Integer idRequest) {
        EnrollmentUserCourseDTO dto = enrollmentService.getEnrollmentWithUserAndCourse(idRequest);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollmentList = enrollmentService.findAll();
        if (enrollmentList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enrollmentList);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createEnrollment(@RequestBody Enrollment enrollment) {
        enrollmentService.create(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("ENROLLMENT CREATED"));
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateEnrollment(@PathVariable Integer idRequest, @RequestBody Enrollment enrollment) {
        enrollmentService.update(idRequest, enrollment);
        return ResponseEntity.ok(new MessageResponse("ENROLLMENT UPDATED"));
    }

    @PatchMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> patchEnrollment(@PathVariable Integer idRequest, @RequestBody Enrollment enrollment) {
        enrollmentService.partialUpdate(idRequest, enrollment);
        return ResponseEntity.ok(new MessageResponse("ENROLLMENT PARTIALLY UPDATED"));
    }

    @DeleteMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> deleteEnrollment(@PathVariable Integer idRequest) {
        enrollmentService.remove(idRequest);
        return ResponseEntity.ok(new MessageResponse("ENROLLMENT DELETED"));
    }

    @GetMapping("/ping-user")
    public ResponseEntity<MessageResponse> pingUserService() {
        boolean ok = enrollmentService.pingUserService();
        return ok
                ? ResponseEntity.ok(new MessageResponse("PONG"))
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("USER SERVICE NOT AVAILABLE"));
    }
}
