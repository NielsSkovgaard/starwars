package com.b2w.starwars.infrastructure.mappers;

import com.b2w.starwars.application.services.SwapiService;
import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.springframework.stereotype.Component;

@Component
public class PlanetMongoDbMapper {
    private final SwapiService swapiService;

    public PlanetMongoDbMapper(SwapiService swapiService) {
        this.swapiService = swapiService;
    }

    public PlanetMongoDb map(Planet planet) {
        if (planet == null) {
            return null;
        }
        return new PlanetMongoDb(planet.getName(), planet.getClimate(), planet.getTerrain());
    }

    public Planet map(PlanetMongoDb planetMongoDb) {
        if (planetMongoDb == null) {
            return null;
        }
        int movies = swapiService.getMovies(planetMongoDb.getName());
        return new Planet(planetMongoDb.getId().toString(), planetMongoDb.getName(), planetMongoDb.getClimate(), planetMongoDb.getTerrain(), movies);
    }
}
