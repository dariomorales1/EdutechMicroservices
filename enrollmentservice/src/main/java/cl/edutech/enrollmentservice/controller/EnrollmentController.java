package cl.edutech.enrollmentservice.controller;

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

    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments(Integer id) {
        List<Enrollment> enrollmentList = enrollmentService.findAll();
        if (enrollmentList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enrollmentList);
    }

    @GetMapping("/{idRequest}")
    public ResponseEntity<Enrollment> searchEnrollment(@PathVariable Integer idRequest) {
        Enrollment enrollment = enrollmentService.findById(idRequest);
        if (enrollment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(enrollment);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody Enrollment enrollment) {
//        List<Enrollment> enrollmentList = enrollmentService.findAll();
//
//        // Validacion si existe el enrollment
//        for (Enrollment e : enrollmentList) {
//            if (e.getId().equals(enrollment.getId())) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("ENROLLMENT WAS EXISTS"));
//            } else {
//                //Validar si existe el user

        Enrollment existingEnrollment = enrollmentService.findById(enrollment.getId());
        if (existingEnrollment != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Enrollment already exists"));
        }

        boolean isAutenticatedRut = enrollmentService.validateUserRut(enrollment.getStudentRut());
                /*
                FALTA LA PARTE DEL FELIPE PARA SEGUIR

                boolean isAutenticatedCourse = enrollmentService.validateCourseId(String.valueOf(enrollment.getCourseId()))*/

        if (!isAutenticatedRut /*&& isAutenticatedCourse*/) {


            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("User Not Exists"));
        }

        enrollmentService.create(enrollment);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("ENROLLMENT CREATED"));



                /*enrollmentService.create(enrollment);
                return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("ENROLLMENT CREATED"));*/


    }
    @GetMapping("/ping-user")
    public ResponseEntity<MessageResponse> pingUserService() {
        String response = enrollmentService.pingUserService();
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(response));
    }

    @PutMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> updateEnrollment(@PathVariable Integer idRequest, @RequestBody Enrollment enrollment) {
        List<Enrollment> enrollmentList = enrollmentService.findAll();
        for (Enrollment e : enrollmentList) {
            if (e.getId().equals(idRequest)) {
                enrollmentService.remove(idRequest);
                enrollmentService.create(enrollment);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("ENROLLMENT UPDATED"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));

    }

    @DeleteMapping("/{idRequest}")
    public ResponseEntity<MessageResponse> deleteEnrollment(@PathVariable Integer idRequest) {
        List<Enrollment> enrollmentList = enrollmentService.findAll();
        for (Enrollment e : enrollmentList) {
            if(e.getId().equals(idRequest)) {
                enrollmentService.remove(idRequest);
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("ENROLLMENT DELETED"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("ENROLLMENT NOT FOUND"));
    }
}




