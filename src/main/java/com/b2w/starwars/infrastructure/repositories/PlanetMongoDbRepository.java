package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.config.Configuration;
import com.b2w.starwars.domain.models.Planet;
import com.b2w.starwars.domain.repositories.PlanetRepository;
import com.b2w.starwars.infrastructure.mappers.PlanetMongoDbMapper;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlanetMongoDbRepository extends MongoDbRepository<String, Planet, ObjectId, PlanetMongoDb> implements PlanetRepository {
    public PlanetMongoDbRepository(MongoTemplate mongoTemplate, PlanetMongoDbMapper planetMongoDbMapper, Configuration configuration) {
        super(mongoTemplate, planetMongoDbMapper, configuration.getMongoDbCollectionName());
    }

    @Override
    public Planet getByName(String name) {
        return getSingle("name", name);
    }

    @Override
    protected Class<PlanetMongoDb> getTInfrastructureEntityClass() {
        return PlanetMongoDb.class;
    }
}
