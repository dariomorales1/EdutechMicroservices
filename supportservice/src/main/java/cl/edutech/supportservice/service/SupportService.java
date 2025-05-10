package cl.edutech.supportservice.service;

import cl.edutech.supportservice.model.SupportTicket;
import cl.edutech.supportservice.repository.SupportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class SupportService {

    @Autowired
    private SupportRepository supportRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<SupportTicket> findAll() {
        return supportRepository.findAll();
    }

    public SupportTicket findById(Integer id) {
        return supportRepository.findById(id).orElse(null);
    }

    public SupportTicket create(SupportTicket supportTicket) {
        return supportRepository.save(supportTicket);
    }

    public void remove(Integer id) {
        supportRepository.deleteById(id);
    }

    public boolean validateUser(String rutRequest) {
        String userServiceUrl = "http://localhost:8082/users/validate/rut?rutRequest=" + rutRequest;

        try {
            Boolean isValid = restTemplate.postForObject(userServiceUrl,null, Boolean.class);
            return isValid != null && isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
