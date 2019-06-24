package com.b2w.starwars.domain.repositories;

import com.b2w.starwars.domain.models.Planet;

import java.util.List;

public interface PlanetRepository {
    List<Planet> getAll();

    Planet getById(String id);

    Planet getByName(String name);

    Planet save(Planet planet);

    void delete(String id);
}
