package com.b2w.starwars.application.services;

import com.b2w.starwars.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class RestServiceImpl implements RestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceImpl.class);

    private final RestTemplate restTemplate;
    private final Configuration configuration;

    public RestServiceImpl(RestTemplate restTemplate, Configuration configuration) {
        this.restTemplate = restTemplate;
        this.configuration = configuration;
    }

    @Override
    public <T> T get(String url, Class<T> clazz) {
        HttpEntity<T> httpEntity = new HttpEntity<>(buildHttpHeaders());
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz);
        HttpStatus statusCode = responseEntity.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        LOGGER.error("RestServiceImpl - response status code: {}.", statusCode);
        throw new ResponseStatusException(statusCode);
    }

    private HttpHeaders buildHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.USER_AGENT, configuration.getUserAgent());
        return httpHeaders;
    }
}
