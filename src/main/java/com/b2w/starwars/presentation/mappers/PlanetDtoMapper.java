package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.models.PlanetDto;
import org.springframework.stereotype.Component;

@Component
public class PlanetDtoMapper {
    public PlanetDto map(Planet planet) {
        if (planet == null) {
            return null;
        }
        return new PlanetDto(planet.getId(), planet.getName(), planet.getClimate(), planet.getTerrain(), planet.getMovies());
    }

    public Planet map(PlanetDto planetDto) {
        if (planetDto == null) {
            return null;
        }
        return new Planet(planetDto.getId(), planetDto.getName(), planetDto.getClimate(), planetDto.getTerrain(), planetDto.getMovies());
    }
}
