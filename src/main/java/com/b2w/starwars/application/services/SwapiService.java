package com.b2w.starwars.application.services;

import com.b2w.starwars.domain.models.PlanetSwapi;
import com.b2w.starwars.domain.models.PlanetSwapiSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class SwapiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwapiService.class);

    @Value("${services.swapi.url}")
    private String swapiUrl;

    @Value("${services.config.useragent}")
    private String userAgent;

    // TODO: On post of movie, update cache with number of movies.

    /*
     * Gets a HashMap with the number of movies for each planet.
     */
    public HashMap<String, Integer> getPlanetMovies() {
        HashMap<String, Integer> planetNameAndMovies = new HashMap<>();
        String url = swapiUrl;

        do {
            PlanetSwapiSearchResult result = getSwapiPlanetResult(url);
            if (result != null && result.getResults() != null) {
                for (PlanetSwapi planet : result.getResults()) {
                    planetNameAndMovies.put(planet.getName(), planet.getFilms().size());
                }
                url = result.getNext();
            }
        } while (url != null && url.length() > 0);

        return planetNameAndMovies;
    }

    private PlanetSwapiSearchResult getSwapiPlanetResult(String apiUrl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);

        HttpEntity<PlanetSwapiSearchResult> httpEntity = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PlanetSwapiSearchResult> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, PlanetSwapiSearchResult.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        LOGGER.debug("Response Status Code: {}.", statusCode);

        if (statusCode == HttpStatus.OK) {
            PlanetSwapiSearchResult result = responseEntity.getBody();
            if (result != null) {
                return result;
            }
        }

        return null;
    }
}
