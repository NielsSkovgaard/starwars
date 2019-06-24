package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.domain.repositories.PlanetRepository;
import com.b2w.starwars.infrastructure.models.PlanetMongoDb;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlanetMongoDbRepository extends MongoRepository<PlanetMongoDb, ObjectId>, PlanetRepository {
}
