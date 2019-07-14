package com.b2w.starwars.domain.repositories;

import com.b2w.starwars.domain.models.Planet;

import java.util.List;

public interface PlanetRepository extends Repository<String, Planet> {
    @Override
    List<Planet> getAll();

    @Override
    Planet getById(String id);

    Planet getByName(String name);

    @Override
    Planet save(Planet planet);

    @Override
    void delete(String id);
}
