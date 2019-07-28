package com.b2w.starwars.application.services;

import com.b2w.starwars.application.models.PlanetSwapi;
import com.b2w.starwars.application.models.PlanetSwapiPagedSearchResult;
import com.b2w.starwars.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Builds and holds a HashMap cache with the number of movies for each planet.
 */
@Service
public class SwapiServiceImpl implements SwapiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SwapiServiceImpl.class);

    private final RestGetService restGetService;
    private final Configuration configuration;

    private HashMap<String, Integer> planetMovies;

    public SwapiServiceImpl(RestGetService restGetService, Configuration configuration) {
        this.restGetService = restGetService;
        this.configuration = configuration;
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
        String searchUrl = configuration.getSwapiUrl();

        do {
            PlanetSwapiPagedSearchResult result = restGetService.get(searchUrl, configuration.getUserAgent(), PlanetSwapiPagedSearchResult.class);
            if (result == null || result.getResults() == null) {
                LOGGER.error("PlanetSwapiPagedSearchResult is null or .getResults() is null.");
                break;
            }
            for (PlanetSwapi planet : result.getResults()) {
                hashMap.put(planet.getName(), planet.getFilms().size());
            }
            searchUrl = result.getNext();
        } while (searchUrl != null && searchUrl.length() > 0);

        LOGGER.info("Finished building SWAPI cache.");
        return hashMap;
    }
}
