package com.b2w.starwars.domain.repositories;

import com.b2w.starwars.domain.models.Planet;

import java.util.List;

public interface PlanetRepository {
    List<Planet> findAll();
}
