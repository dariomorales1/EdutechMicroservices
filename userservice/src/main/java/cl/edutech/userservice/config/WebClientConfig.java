package cl.edutech.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient courseWebClient() {
        return WebClient.builder()

                //###DOCKER###
                //.baseUrl("http://apigateway:8080/auth")
                //.baseUrl("http://localhost:8080/users")
                .build();
    }
}

