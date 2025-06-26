package cl.edutech.enrollmentservice.service;

import cl.edutech.enrollmentservice.model.Enrollment;
import cl.edutech.enrollmentservice.repository.EnrollmentRepository;
import cl.edutech.enrollmentservice.exception.ConflictException;
import cl.edutech.enrollmentservice.exception.NotFoundException;
import cl.edutech.enrollmentservice.DTO.UserDTO;
import cl.edutech.enrollmentservice.DTO.CourseDTO;
import cl.edutech.enrollmentservice.DTO.EnrollmentUserCourseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Integer id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ENROLLMENT NOT FOUND"));
    }

    public Enrollment create(Enrollment enrollment) {
        if (enrollmentRepository.existsById(enrollment.getId())) {
            throw new ConflictException("ENROLLMENT ALREADY EXISTS");
        }
        if (getUser(enrollment.getUserRut()) == null) {
            throw new NotFoundException("USER NOT FOUND");
        }
        if (getCourse(enrollment.getCourseId()) == null) {
            throw new NotFoundException("COURSE NOT FOUND");
        }
        return enrollmentRepository.save(enrollment);
    }

    public void remove(Integer id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new NotFoundException("ENROLLMENT NOT FOUND");
        }
        enrollmentRepository.deleteById(id);
    }

    public void partialUpdate(Integer id, Enrollment enrollmentRequest) {
        Enrollment existing = findById(id);

        if (enrollmentRequest.getUserRut() != null) {
            if (getUser(enrollmentRequest.getUserRut()) == null) {
                throw new NotFoundException("USER NOT FOUND");
            }
            existing.setUserRut(enrollmentRequest.getUserRut());
        }
        if (enrollmentRequest.getCourseId() != null) {
            if (getCourse(enrollmentRequest.getCourseId()) == null) {
                throw new NotFoundException("COURSE NOT FOUND");
            }
            existing.setCourseId(enrollmentRequest.getCourseId());
        }
        enrollmentRepository.save(existing);
    }

    public void update(Integer id, Enrollment enrollmentRequest) {
        Enrollment existing = findById(id);

        if (getUser(enrollmentRequest.getUserRut()) == null) {
            throw new NotFoundException("USER NOT FOUND");
        }
        if (getCourse(enrollmentRequest.getCourseId()) == null) {
            throw new NotFoundException("COURSE NOT FOUND");
        }

        existing.setUserRut(enrollmentRequest.getUserRut());
        existing.setCourseId(enrollmentRequest.getCourseId());
        enrollmentRepository.save(existing);
    }

    public EnrollmentUserCourseDTO getEnrollmentWithUserAndCourse(Integer idRequest) {
        Enrollment enrollment = findById(idRequest);
        UserDTO user = getUser(enrollment.getUserRut());
        if (user == null) throw new NotFoundException("USER NOT FOUND");
        CourseDTO course = getCourse(enrollment.getCourseId());
        if (course == null) throw new NotFoundException("COURSE NOT FOUND");

        EnrollmentUserCourseDTO dto = new EnrollmentUserCourseDTO();
        dto.setId(enrollment.getId());
        dto.setRut(user.getRut());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setCourseId(course.getCourseId());
        dto.setNameCourse(course.getNameCourse());
        return dto;
    }

    public UserDTO getUser(String userRut) {
        try {
            return userWebClient.get()
                    .uri("/{rut}", userRut)
                    .retrieve()
                    .bodyToMono(UserDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public CourseDTO getCourse(String courseId) {
        try {
            return courseWebClient.get()
                    .uri("/{courseId}", courseId)
                    .retrieve()
                    .bodyToMono(CourseDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean pingUserService() {
        try {
            var response = userWebClient.get()
                    .uri("/ping")
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            return response != null && response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
