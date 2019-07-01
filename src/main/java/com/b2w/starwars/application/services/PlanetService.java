package com.b2w.starwars.application.services;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetService {
    private final PlanetRepository planetRepository;

    public PlanetService(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    public List<Planet> getAll() {
        return planetRepository.getAll();
    }

    public Planet getById(String id) {
        return planetRepository.getById(id);
    }

    public Planet getByName(String name) {
        return planetRepository.getByName(name);
    }

    public Planet save(Planet planet) {
        return planetRepository.save(planet);
    }

    public void delete(String id) {
        planetRepository.delete(id);
    }
}
