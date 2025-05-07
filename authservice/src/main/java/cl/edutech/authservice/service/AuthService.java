package cl.edutech.authservice.service;

import cl.edutech.authservice.model.Token;
import cl.edutech.authservice.repository.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthRepository authRepository;

    public boolean validateUser(String emailRequest, String passwordRequest) {
        String userServiceUrl = "http://localhost:8082/users/validate?emailRequest=" + emailRequest+ "&passwordRequest=" + passwordRequest;

        try {
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

    // Creacion del Token automatico

    private static int tokenCounter = 1;

    public String generateTokenId() {
        return "Token" + tokenCounter++;
    }

    //CRUD DB

    //Create

    public Token create(Token token) {
        return authRepository.save(token);
    }

    //Read

    public List<Token> findAll(){
        return authRepository.findAll();
    }

    public Token findById(String id) {
        return authRepository.findById(id).get();
    }

    //Remove

    public void remove(String id) {
        authRepository.deleteById(id);
    }

}
