package test.raven.moviescatalog.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {

    @Value("${server.port}")
    private int port;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder().rootUri("http://localhost:" + port).build();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
