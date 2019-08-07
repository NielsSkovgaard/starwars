package com.b2w.starwars.domain.models;

import com.b2w.starwars.core.DomainObject;
import com.b2w.starwars.core.Entity;

import java.util.Objects;

public class Planet implements DomainObject, Entity<String> {
    private String id;
    private String name;
    private String climate;
    private String terrain;
    private int movies;

    public Planet() {
    }

    public Planet(String name, String climate, String terrain) {
        this(null, name, climate, terrain, 0);
    }

    public Planet(String id, String name, String climate, String terrain, int movies) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.movies = movies;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public int getMovies() {
        return movies;
    }

    public void setMovies(int movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return movies == planet.movies &&
                Objects.equals(id, planet.id) &&
                Objects.equals(name, planet.name) &&
                Objects.equals(climate, planet.climate) &&
                Objects.equals(terrain, planet.terrain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, climate, terrain, movies);
    }
}
