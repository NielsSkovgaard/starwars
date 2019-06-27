package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import com.b2w.starwars.infrastructure.mappers.PlanetMongoDbMapper;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class PlanetMongoDbRepository implements PlanetRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PlanetMongoDbMapper planetMongoDbMapper;

    @Value("${mongodb.collection-name}")
    private String collectionName;

    @Override
    public List<Planet> getAll() {
        return mongoTemplate.findAll(PlanetMongoDb.class, collectionName).stream().map(planetMongoDbMapper::map).collect(Collectors.toList());
    }

    @Override
    public Planet getById(String id) {
        return planetMongoDbMapper.map(mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), PlanetMongoDb.class, collectionName));
    }

    @Override
    public Planet getByName(String name) {
        return planetMongoDbMapper.map(mongoTemplate.findOne(Query.query(Criteria.where("name").is(name)), PlanetMongoDb.class, collectionName));
    }

    @Override
    public Planet save(Planet planet) {
        return planetMongoDbMapper.map(mongoTemplate.save(planetMongoDbMapper.map(planet), collectionName));
    }

    @Override
    public void delete(String id) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), collectionName);
        if (!deleteResult.wasAcknowledged()) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
