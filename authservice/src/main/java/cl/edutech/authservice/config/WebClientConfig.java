package cl.edutech.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/users")
                //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) <- Actualizar cn los datos de api key
                .build();
    }
}
