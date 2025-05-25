package cl.edutech.evaluationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()
                //.baseUrl("http://apigateway:8080/users")  //#<<<<<<<-------DOCKER
                .baseUrl("http://localhost:8080/users")   //#<<<<<<<-------LOCAL
                .build();
    }

    @Bean
    public WebClient courseWebClient() {
        return WebClient.builder()
                //.baseUrl("http://apigateway:8080/courses")  //#<<<<<<<-------DOCKER
                .baseUrl("http://localhost:8080/courses")   //#<<<<<<<-------LOCAL
                .build();
    }
}
