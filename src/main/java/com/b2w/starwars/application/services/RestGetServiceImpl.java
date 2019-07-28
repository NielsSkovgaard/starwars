package com.b2w.starwars.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class RestGetServiceImpl implements RestGetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestGetServiceImpl.class);

    private final RestTemplate restTemplate;

    public RestGetServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T get(String url, String userAgent, Class<T> clazz) {
        HttpEntity<T> httpEntity = new HttpEntity<>(buildHttpHeaders(userAgent));
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, clazz);
        HttpStatus statusCode = responseEntity.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        LOGGER.error("SwapiService - response status code: {}.", statusCode);
        throw new ResponseStatusException(statusCode);
    }

    private HttpHeaders buildHttpHeaders(String userAgent) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);
        return httpHeaders;
    }
}
