package cl.edutech.authservice.service;

import cl.edutech.authservice.repository.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateUser(String emailRequest, String passwordRequest) {
        String userServiceUrl = "http://localhost:8082/users/validate?emailRequest=" + emailRequest+ "&passwordRequest=" + passwordRequest;

        try {
            RestTemplate restTemplate = new RestTemplate();
            Boolean isValid = restTemplate.postForObject(userServiceUrl,null, Boolean.class);
            return isValid != null && isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String pingUserService() {
        String userServiceUrl = "http://localhost:8082/users/ping";
        try {
            return restTemplate.getForObject(userServiceUrl, String.class);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
