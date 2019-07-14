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

import java.util.Collections;
import java.util.HashMap;

/**
 * Builds and holds a HashMap cache with the number of movies for each planet.
 */
@Service
public class SwapiServiceImpl implements SwapiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwapiServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${services.swapi.url}")
    private String swapiUrl;

    @Value("${services.config.useragent}")
    private String userAgent;

    private HashMap<String, Integer> planetMovies;

    public SwapiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getMovies(String planetName) {
        if (planetName != null) {
            if (planetMovies == null) {
                planetMovies = buildPlanetMovies();
            }
            Integer movies = planetMovies.get(planetName.toLowerCase());
            if (movies != null) {
                return movies;
            }
        }
        return 0;
    }

    private HashMap<String, Integer> buildPlanetMovies() {
        LOGGER.info("Starting building SWAPI cache.");

        HashMap<String, Integer> hashMap = new HashMap<>();
        HttpEntity<PlanetSwapiPagedSearchResult> httpEntity = buildHttpEntity();
        String searchUrl = swapiUrl;

        do {
            PlanetSwapiPagedSearchResult result = fetchPagedSearchResult(httpEntity, searchUrl);
            if (result != null && result.getResults() != null) {
                for (PlanetSwapi planet : result.getResults()) {
                    hashMap.put(planet.getName().toLowerCase(), planet.getFilms().size());
                }
                searchUrl = result.getNext();
            }
        } while (searchUrl != null && searchUrl.length() > 0);

        LOGGER.info("Finished building SWAPI cache.");
        return hashMap;
    }

    private HttpEntity<PlanetSwapiPagedSearchResult> buildHttpEntity() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.USER_AGENT, userAgent);
        return new HttpEntity<>(httpHeaders);
    }

    private PlanetSwapiPagedSearchResult fetchPagedSearchResult(HttpEntity<PlanetSwapiPagedSearchResult> httpEntity, String url) {
        ResponseEntity<PlanetSwapiPagedSearchResult> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, PlanetSwapiPagedSearchResult.class);
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
