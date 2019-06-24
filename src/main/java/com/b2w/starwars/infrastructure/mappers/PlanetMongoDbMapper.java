package com.b2w.starwars.infrastructure.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.springframework.stereotype.Component;

@Component
public class PlanetMongoDbMapper {
    public static PlanetMongoDb map(Planet planet) {
        return new PlanetMongoDb(planet.getName(), planet.getClimate(), planet.getTerrain());
    }

    public static Planet map(PlanetMongoDb planetMongoDb) {
        int movies = 5; // TODO: Dependency injection of SWAPI service / caching layer to be called here
        return new Planet(planetMongoDb.getId().toString(), planetMongoDb.getName(), planetMongoDb.getClimate(), planetMongoDb.getTerrain(), movies);
    }
}
