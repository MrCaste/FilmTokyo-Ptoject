package com.filmtokio.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.Getter;

@Configuration
@Getter
public class RestClientConfig {

    @Value("${app.rest-client.base-url:http://localhost:8081}")
    private String baseUrl;

    @Value("${app.rest-client.user:user}")
    private String user;
    @Value("${app.rest-client.password:pass}")
    private String password;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(header -> header.setBasicAuth(user, password))
                .build();
    }
}
