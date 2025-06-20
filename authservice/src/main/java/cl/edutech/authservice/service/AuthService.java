package cl.edutech.authservice.service;

import cl.edutech.authservice.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final WebClient userWebClient;

    public AuthService(WebClient webClient) {
        this.userWebClient = webClient;
    }

    public UserDTO getUser(String emailRequest) {
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/email/{emailRequest}").build(emailRequest))
                .retrieve()
                .bodyToMono(UserDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }

    public ResponseEntity<String> pingUserService() {
        return userWebClient.get()
                .uri("/ping")
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
