package com.b2w.starwars.core;

public interface Entity<TId> {
    TId getId();

    void setId(TId id);
}
