package com.b2w.starwars.domain.repositories;

import com.b2w.starwars.core.Entity;

import java.util.List;

public interface Repository<TId, TEntity extends Entity<TId>> {
    List<TEntity> getAll();

    TEntity getById(TId id);

    TEntity save(TEntity entity);

    void delete(TId id);
}
