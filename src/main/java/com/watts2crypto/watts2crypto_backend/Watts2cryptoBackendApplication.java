package com.watts2crypto.watts2crypto_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Watts2cryptoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Watts2cryptoBackendApplication.class, args);
	}

	@Bean //restTemplate que se usa en las llamadas a las APIs
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
