package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.models.PlanetDto;
import com.b2w.starwars.presentation.models.PlanetDtoCreate;
import org.springframework.stereotype.Component;

@Component
public class PlanetDtoMapper implements DomainPresentationMapper<Planet, PlanetDto, PlanetDtoCreate> {
    @Override
    public PlanetDto map(Planet planet) {
        if (planet == null) {
            return null;
        }
        return new PlanetDto(planet.getId(), planet.getName(), planet.getClimate(), planet.getTerrain(), planet.getMovies());
    }

    @Override
    public Planet map(PlanetDtoCreate planetDtoCreate) {
        if (planetDtoCreate == null) {
            return null;
        }
        return new Planet(planetDtoCreate.getName(), planetDtoCreate.getClimate(), planetDtoCreate.getTerrain());
    }
}
