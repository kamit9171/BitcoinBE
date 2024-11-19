package com.kamit9171;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CoinBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinBackendApplication.class, args);
		System.out.println("Backend is Running Fine");
		
		
	}
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
	
}
