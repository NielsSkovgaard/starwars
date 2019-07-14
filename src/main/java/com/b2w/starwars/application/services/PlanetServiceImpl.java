package com.b2w.starwars.application.services;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {
    private final PlanetRepository planetRepository;

    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public List<Planet> getAll() {
        return planetRepository.getAll();
    }

    @Override
    public Planet getById(String id) {
        return planetRepository.getById(id);
    }

    @Override
    public Planet getByName(String name) {
        return planetRepository.getByName(name);
    }

    @Override
    public Planet save(Planet planet) {
        return planetRepository.save(planet);
    }

    @Override
    public void delete(String id) {
        planetRepository.delete(id);
    }
}
