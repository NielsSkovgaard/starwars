package com.b2w.starwars.infrastructure.repositories;

import com.b2w.starwars.core.DomainObject;
import com.b2w.starwars.core.Entity;
import com.b2w.starwars.core.InfrastructureObject;
import com.b2w.starwars.domain.repositories.Repository;
import com.b2w.starwars.infrastructure.mappers.DomainInfrastructureMapper;
import com.mongodb.client.result.DeleteResult;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MongoDbRepository<TDomainId, TDomainEntity extends DomainObject & Entity<TDomainId>, TInfrastructureId, TInfrastructureEntity extends InfrastructureObject & Entity<TInfrastructureId>> implements Repository<TDomainId, TDomainEntity> {
    private static final String MongoDbIdFieldName = "_id";
    private static final int TInfrastructureEntityTypeArgumentIndex = 3;

    private final MongoTemplate mongoTemplate;
    private final DomainInfrastructureMapper<TDomainEntity, TInfrastructureEntity> domainInfrastructureMapper;
    private final String collectionName;

    private final Class<TInfrastructureEntity> infrastructureEntityClass;

    public MongoDbRepository(MongoTemplate mongoTemplate, DomainInfrastructureMapper<TDomainEntity, TInfrastructureEntity> domainInfrastructureMapper, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.domainInfrastructureMapper = domainInfrastructureMapper;
        this.collectionName = collectionName;

        this.infrastructureEntityClass = getInfrastructureEntityClass();
    }

    @Override
    public List<TDomainEntity> getAll() {
        return mongoTemplate
                .findAll(infrastructureEntityClass, collectionName)
                .stream()
                .map(domainInfrastructureMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TDomainEntity getById(TDomainId id) {
        return getSingle(MongoDbIdFieldName, id);
    }

    @Override
    public TDomainEntity save(TDomainEntity entity) {
        return domainInfrastructureMapper.map(mongoTemplate.save(domainInfrastructureMapper.map(entity), collectionName));
    }

    @Override
    public void delete(TDomainId id) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where(MongoDbIdFieldName).is(id)), collectionName);
        if (!deleteResult.wasAcknowledged() || deleteResult.getDeletedCount() == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    public TDomainEntity getSingle(String fieldName, Object value) {
        return domainInfrastructureMapper.map(mongoTemplate.findOne(Query.query(Criteria.where(fieldName).is(value)), infrastructureEntityClass, collectionName));
    }

    @SuppressWarnings("unchecked")
    private Class<TInfrastructureEntity> getInfrastructureEntityClass() {
        String typeName = ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[TInfrastructureEntityTypeArgumentIndex]
                .getTypeName();

        try {
            return (Class<TInfrastructureEntity>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
