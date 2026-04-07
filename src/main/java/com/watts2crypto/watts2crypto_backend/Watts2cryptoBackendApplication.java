package com.watts2crypto.watts2crypto_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling; //warning para que volvieras, mira la linea 8
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// @EnableScheduling Esto por si despliegas, para que las llamadas a las APIs, scraping y demás, se hagan cada X tiempo, deja el import con el
//warning para acordarte de que esto hay que descomentarlo
public class Watts2cryptoBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Watts2cryptoBackendApplication.class, args);
	}

	@Bean //restTemplate que se usa en las llamadas a las APIsS
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
