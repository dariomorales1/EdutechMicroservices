package cl.edutech.billingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient userWebClient(WebClient.Builder builder) {
        return builder
                // .baseUrl("http://apigateway:8080/users") // <<-- DOCKER
                .baseUrl("http://localhost:8080/users")     // <<-- LOCAL
                .build();
    }

    @Bean
    public WebClient courseWebClient(WebClient.Builder builder) {
        return builder
                // .baseUrl("http://apigateway:8080/courses")
                .baseUrl("http://localhost:8080/courses")
                .build();
    }

    @Bean
    public WebClient enrollmentWebClient(WebClient.Builder builder) {
        return builder
                // .baseUrl("http://apigateway:8080/enroll")
                .baseUrl("http://localhost:8080/enroll")
                .build();
    }
}