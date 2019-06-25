package com.b2w.starwars;

import com.b2w.starwars.application.services.SwapiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class StarwarsApplication {
    @Autowired
    private SwapiService swapiService;

    public static void main(String[] args) {
        SpringApplication.run(StarwarsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> swapiService.buildPlanetMovies(restTemplate);
    }
}
