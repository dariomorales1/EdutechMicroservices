package cl.edutech.billingservice.service;

import cl.edutech.billingservice.model.Billing;
import cl.edutech.billingservice.repository.BillingRepository;
import cl.edutech.billingservice.exception.NotFoundException;
import cl.edutech.billingservice.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import cl.edutech.billingservice.DTO.UserDTO;
import cl.edutech.billingservice.DTO.CourseDTO;
import cl.edutech.billingservice.DTO.EnrollmentDTO;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private WebClient userWebClient;

    @Autowired
    private WebClient courseWebClient;

    @Autowired
    private WebClient enrollmentWebClient;

    public List<Billing> findAll() {
        return billingRepository.findAll();
    }

    public Billing findById(Integer id) {
        return billingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BILLING NOT FOUND"));
    }

    public Billing create(Billing billing) {
        if (billingRepository.existsById(billing.getBillingId())) {
            throw new ConflictException("BILLING EXISTS");
        }

        CourseDTO course = getCourse(billing.getCourseId());
        if (course == null) {
            throw new NotFoundException("COURSE NOT FOUND");
        }

        Integer price = course.getPrice(); //se obtiene el precio del curso desde CourseService
        if (price == null) {
            throw new ConflictException("COURSE PRICE NOT FOUND");
        }

        int amountWithTax = (int) Math.round(price * 1.19); //calculo del monto con IVA (19%)

        billing.setAmount(amountWithTax);

        return billingRepository.save(billing);
    }

    public void update(Integer billingId, Billing billingRequest) {
        Billing existing = findById(billingId);

        UserDTO user = getUser(billingRequest.getUserId());
        if (user == null) {
            throw new NotFoundException("USER NOT FOUND");
        }
        CourseDTO course = getCourse(billingRequest.getCourseId());
        if (course == null) {
            throw new NotFoundException("COURSE NOT FOUND");
        }
        EnrollmentDTO enroll = getEnrollment(billingRequest.getEnrollmentId());
        if (enroll == null) {
            throw new NotFoundException("ENROLLMENT NOT FOUND");
        }

        if (!billingRequest.getCourseId().equals(existing.getCourseId())) {
            int amountWithTax = (int) Math.round(course.getPrice() * 1.19);
            existing.setAmount(amountWithTax);
        }

        existing.setUserId(billingRequest.getUserId());
        existing.setCourseId(billingRequest.getCourseId());
        existing.setEnrollmentId(billingRequest.getEnrollmentId());

        billingRepository.save(existing);
    }


    public void partialUpdate(Integer billingId, Billing billingRequest) {
        Billing existing = findById(billingId);

        if (billingRequest.getUserId() != null) existing.setUserId(billingRequest.getUserId());
        if (billingRequest.getCourseId() != null) existing.setCourseId(billingRequest.getCourseId());
        if (billingRequest.getEnrollmentId() != null) existing.setEnrollmentId(billingRequest.getEnrollmentId());
        if (billingRequest.getAmount() != null) existing.setAmount(billingRequest.getAmount());


        billingRepository.save(existing);
    }

    public void remove(Integer id) {
        if (!billingRepository.existsById(id)) {
            throw new NotFoundException("BILLING NOT FOUND");
        }
        billingRepository.deleteById(id);
    }

    public UserDTO getUser(String userId) {
        try {
            return userWebClient.get()
                    .uri("/{userId}", userId)
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

    public EnrollmentDTO getEnrollment(Integer enrollmentId) {
        try {
            return enrollmentWebClient.get()
                    .uri("/{enrollmentId}", enrollmentId)
                    .retrieve()
                    .bodyToMono(EnrollmentDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
