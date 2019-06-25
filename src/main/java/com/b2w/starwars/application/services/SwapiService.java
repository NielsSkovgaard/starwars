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
 * Builds and holds a HashMap with the number of movies for each planet.
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

        LOGGER.debug("Response Status Code: {}.", statusCode);

        if (statusCode == HttpStatus.OK) {
            return responseEntity.getBody();
        }

        // TODO: For a more robust solution and depending on how stable the SWAPI REST service is in the future, consider applying the Retry design pattern.
        // See: https://www.baeldung.com/spring-retry
        // For now, building of the SWAPI cache on application start takes around 5 seconds and is running stable, which doesn't indicate the need for further engineering at the moment.
        throw new ResponseStatusException(statusCode);
    }
}
