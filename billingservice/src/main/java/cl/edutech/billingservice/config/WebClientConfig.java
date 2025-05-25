package cl.edutech.billingservice.config;

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

    @Bean
    public WebClient courseWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/courses")
                //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) <- Actualizar cn los datos de api key
                .build();
    }

    @Bean
    public WebClient enrollmentWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/enrollments")
                //.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) <- Actualizar cn los datos de api key
                .build();
    }
}
