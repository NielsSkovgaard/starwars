package com.b2w.starwars.infrastructure.mappers;

import com.b2w.starwars.application.services.SwapiService;
import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlanetMongoDbMapper {
    @Autowired
    private SwapiService swapiService;

    public PlanetMongoDb map(Planet planet) {
        return new PlanetMongoDb(planet.getName(), planet.getClimate(), planet.getTerrain());
    }

    public Planet map(PlanetMongoDb planetMongoDb) {
        int movies = swapiService.getMovies(planetMongoDb.getName());
        return new Planet(planetMongoDb.getId().toString(), planetMongoDb.getName(), planetMongoDb.getClimate(), planetMongoDb.getTerrain(), movies);
    }
}
