package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlanetMongoDbRepository implements PlanetRepository {
    @Override
    public List<Planet> findAll() {

        // TODO

        ArrayList<Planet> list = new ArrayList<>();
        list.add(new Planet("id", "name", "climate", "terrain", 1));
        return list;
    }
}
