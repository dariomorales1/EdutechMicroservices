package cl.edutech.supportservice.service;

import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.repository.SupportRepository;
import cl.edutech.supportservice.DTO.UserDTO;
import cl.edutech.supportservice.DTO.CourseDTO;
import cl.edutech.supportservice.exception.ConflictException;
import cl.edutech.supportservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportRepository supportRepository;
    private final WebClient userWebClient;
    private final WebClient courseWebClient;

    public List<SupportTicket> findAll() {
        return supportRepository.findAll();
    }

    public SupportTicket findById(Integer id) {
        return supportRepository.findById(id).orElse(null);
    }

    public SupportTicket create(SupportTicket ticket) {
        if (supportRepository.existsById(ticket.getId())) {
            throw new ConflictException("Ticket already exists");
        }
        if (getUser(ticket.getUserRut()) == null) {
            throw new NotFoundException("User not found");
        }
        if (getCourse(ticket.getCourseId()) == null) {
            throw new NotFoundException("Course not found");
        }
        return supportRepository.save(ticket);
    }

    public void remove(Integer id) {
        if (!supportRepository.existsById(id)) {
            throw new NotFoundException("Ticket not found");
        }
        supportRepository.deleteById(id);
    }

    public void update(Integer id, SupportTicket ticketRequest) {
        SupportTicket existing = findById(id);
        if (existing == null) throw new NotFoundException("Ticket not found");

        if (getUser(ticketRequest.getUserRut()) == null) {
            throw new NotFoundException("User not found");
        }
        if (getCourse(ticketRequest.getCourseId()) == null) {
            throw new NotFoundException("Course not found");
        }

        existing.setUserRut(ticketRequest.getUserRut());
        existing.setCourseId(ticketRequest.getCourseId());
        existing.setSubject(ticketRequest.getSubject());
        existing.setDescription(ticketRequest.getDescription());

        supportRepository.save(existing);
    }

    public void partialUpdate(Integer id, SupportTicket ticketRequest) {
        SupportTicket existing = findById(id);
        if (existing == null) throw new NotFoundException("Ticket not found");

        if (ticketRequest.getUserRut() != null) {
            if (getUser(ticketRequest.getUserRut()) == null) {
                throw new NotFoundException("User not found");
            }
            existing.setUserRut(ticketRequest.getUserRut());
        }
        if (ticketRequest.getCourseId() != null) {
            if (getCourse(ticketRequest.getCourseId()) == null) {
                throw new NotFoundException("Course not found");
            }
            existing.setCourseId(ticketRequest.getCourseId());
        }
        if (ticketRequest.getSubject() != null) {
            existing.setSubject(ticketRequest.getSubject());
        }
        if (ticketRequest.getDescription() != null) {
            existing.setDescription(ticketRequest.getDescription());
        }

        supportRepository.save(existing);
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
}
