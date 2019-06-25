package com.b2w.starwars.application.services;

import com.b2w.starwars.application.models.PlanetSwapi;
import com.b2w.starwars.application.models.PlanetSwapiPagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Builds and holds a HashMap cache with the number of movies for each planet.
 */
@Service
public class SwapiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwapiService.class);

    @Value("${services.swapi.url}")
    private String swapiUrl;

    @Value("${services.config.useragent}")
    private String userAgent;

    private HashMap<String, Integer> planetMovies = new HashMap<>();

    public int getMovies(String planetName) {
        Integer movies = planetMovies.get(planetName.toLowerCase());
        return movies == null ? 0 : movies;
    }

    public void buildPlanetMovies(RestTemplate restTemplate) {
        LOGGER.info("Starting building SWAPI cache.");

        HttpEntity<PlanetSwapiPagedSearchResult> httpEntity = buildHttpEntity();
        String searchUrl = swapiUrl;

        do {
            PlanetSwapiPagedSearchResult result = fetchData(restTemplate, httpEntity, searchUrl);
            if (result != null && result.getResults() != null) {
                for (PlanetSwapi planet : result.getResults()) {
                    planetMovies.put(planet.getName().toLowerCase(), planet.getFilms().size());
                }
                searchUrl = result.getNext();
            }
        } while (searchUrl != null && searchUrl.length() > 0);

        LOGGER.info("Finished building SWAPI cache.");
    }

    private HttpEntity<PlanetSwapiPagedSearchResult> buildHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);
        return new HttpEntity<>(httpHeaders);
    }

    private PlanetSwapiPagedSearchResult fetchData(RestTemplate restTemplate, HttpEntity<PlanetSwapiPagedSearchResult> httpEntity, String apiUrl) {
        ResponseEntity<PlanetSwapiPagedSearchResult> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, httpEntity, PlanetSwapiPagedSearchResult.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        if (statusCode == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        // At the moment, the SWAPI REST service appears stable and fast. In other words, connecting with the service
        // didn't result in server errors, connection timeouts, etc., and it takes < 5 seconds to build the cache of
        // movies per planet on application start.
        // Otherwise, one could consider applying the Retry design pattern (https://www.baeldung.com/spring-retry) for
        // a more robust solution. However, at this point it doesn't seem important.
        LOGGER.error("SwapiService - response status code: {}.", statusCode);
        throw new ResponseStatusException(statusCode);
    }
}
