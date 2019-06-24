package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import com.b2w.starwars.infrastructure.mappers.PlanetMongoDbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class PlanetMongoDbRepositoryImpl implements PlanetRepository {
    private final static String COLLECTION_NAME = "planets";
    private final MongoOperations mongoOperations;

    @Autowired
    private PlanetMongoDbMapper planetMongoDbMapper;

    @Autowired
    public PlanetMongoDbRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Planet> getAll() {
        ArrayList<Planet> list = new ArrayList<>();
        list.add(new Planet("id1", "name1", "climate1", "terrain1", 1));
        list.add(new Planet("id2", "name2", "climate2", "terrain2", 2));
        list.add(new Planet("id3", "name3", "climate3", "terrain3", 3));
        list.add(new Planet("id4", "name4", "climate4", "terrain4", 4));
        list.add(new Planet("id5", "name5", "climate5", "terrain5", 5));
        return list;

        // TODO
        // return mongoOperations.findAll(PlanetMongoDb.class, COLLECTION_NAME).stream().map(planetMongoDbMapper::map).collect(Collectors.toList());
    }

    @Override
    public Planet getById(String id) {
        return new Planet("id1", "name1", "climate1", "terrain1", 1);

        // return planetMongoDbMapper.map(mongoOperations.findOne(Query.query(Criteria.where("_id").is(id)), PlanetMongoDb.class, COLLECTION_NAME));
    }

    @Override
    public Planet getByName(String name) {
        return new Planet("id1", "name1", "climate1", "terrain1", 1);

        // return planetMongoDbMapper.map(mongoOperations.findOne(Query.query(Criteria.where("name").is(name)), PlanetMongoDb.class, COLLECTION_NAME));
    }

    @Override
    public Planet save(Planet planet) {
        return planetMongoDbMapper.map(mongoOperations.save(planetMongoDbMapper.map(planet), COLLECTION_NAME));
    }

    @Override
    public void delete(String id) {
    }
}
