package cl.edutech.authservice.service;

import cl.edutech.authservice.DTO.LoginRequest;
import cl.edutech.authservice.DTO.UserDTO;
import cl.edutech.authservice.model.Token;
import cl.edutech.authservice.repository.AuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
public class AuthService {

    private final WebClient userWebClient;

    public AuthService(WebClient webClient) {
        this.userWebClient = webClient;
    }


    public UserDTO getUser(String emailRequest) {
        UserDTO user = userWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/email/{emailRequest}").build(emailRequest))
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
        return user;
    }



    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthRepository authRepository;

    public ResponseEntity<String> pingUserService() {
        return userWebClient.get()
                .uri("/ping")
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    // Creacion del Token automatico

    private static int tokenCounter = 1;

    public String generateTokenId() {
        return "Token" + tokenCounter++;
    }



    //CRUD DB ------------------------------------------------------------------------------------


    public Token create(Token token) {
        return authRepository.save(token);
    }

    public List<Token> findAll(){
        return authRepository.findAll();
    }

    public Token findById(String id) {
        return authRepository.findById(id).get();
    }

    public void remove(String id) {
        authRepository.deleteById(id);
    }

}
