package cl.edutech.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()

                //###DOCKER - LOCAL###
                //.baseUrl("http://apigateway:8080/users")
                .baseUrl("http://localhost:8080/users")
                .build();
    }
}
