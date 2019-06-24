package com.b2w.starwars.presentation.mappers;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.presentation.models.PlanetDto;

public class PlanetDtoMapper {
    public static PlanetDto map(Planet planet) {
        return new PlanetDto(planet.getId(), planet.getName(), planet.getClimate(), planet.getTerrain(), planet.getMovies());
    }

    public static Planet map(PlanetDto planetDto) {
        return new Planet(planetDto.getId(), planetDto.getName(), planetDto.getClimate(), planetDto.getTerrain(), planetDto.getMovies());
    }
}
