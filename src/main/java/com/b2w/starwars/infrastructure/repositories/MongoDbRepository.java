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

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class MongoDbRepository<TDomainEntity extends DomainObject & Entity<TDomainId>, TDomainId, TInfrastructureEntity extends InfrastructureObject & Entity<TInfrastructureId>, TInfrastructureId> implements Repository<TDomainEntity, TDomainId> {
    private static final String MongoDbIdFieldName = "_id";

    private final MongoTemplate mongoTemplate;
    private final DomainInfrastructureMapper<TDomainEntity, TInfrastructureEntity> domainInfrastructureMapper;
    private final String mongoDbCollectionName;

    public MongoDbRepository(MongoTemplate mongoTemplate, DomainInfrastructureMapper<TDomainEntity, TInfrastructureEntity> domainInfrastructureMapper, String mongoDbCollectionName) {
        this.mongoTemplate = mongoTemplate;
        this.domainInfrastructureMapper = domainInfrastructureMapper;
        this.mongoDbCollectionName = mongoDbCollectionName;
    }

    @Override
    public List<TDomainEntity> getAll() {
        return mongoTemplate
                .findAll(getTInfrastructureEntityClass(), mongoDbCollectionName)
                .stream()
                .map(domainInfrastructureMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TDomainEntity getById(TDomainId id) {
        return domainInfrastructureMapper.map(mongoTemplate.findOne(Query.query(Criteria.where(MongoDbIdFieldName).is(id)), getTInfrastructureEntityClass(), mongoDbCollectionName));
    }

    @Override
    public TDomainEntity save(TDomainEntity entity) {
        return domainInfrastructureMapper.map(mongoTemplate.save(domainInfrastructureMapper.map(entity), mongoDbCollectionName));
    }

    @Override
    public void delete(TDomainId id) {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where(MongoDbIdFieldName).is(id)), mongoDbCollectionName);
        if (!deleteResult.wasAcknowledged() || deleteResult.getDeletedCount() == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    TDomainEntity getSingleCaseInsensitive(String fieldName, String value) {
        Pattern pattern = Pattern.compile(MessageFormat.format("^{0}$", Pattern.quote(value)), Pattern.CASE_INSENSITIVE);
        return domainInfrastructureMapper.map(mongoTemplate.findOne(Query.query(Criteria.where(fieldName).regex(pattern)), getTInfrastructureEntityClass(), mongoDbCollectionName));
    }

    protected abstract Class<TInfrastructureEntity> getTInfrastructureEntityClass();
}
