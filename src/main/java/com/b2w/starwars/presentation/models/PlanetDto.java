package com.b2w.starwars.presentation.models;

import com.b2w.starwars.core.Entity;
import com.b2w.starwars.core.PresentationObject;

import java.util.Objects;

public class PlanetDto implements PresentationObject, Entity<String> {
    private String id;
    private String name;
    private String climate;
    private String terrain;
    private int movies;

    public PlanetDto() {
    }

    public PlanetDto(String id, String name, String climate, String terrain, int movies) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.movies = movies;
    }

    public String getId() {
        return id;
    }

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
        PlanetDto planetDto = (PlanetDto) o;
        return movies == planetDto.movies &&
                Objects.equals(id, planetDto.id) &&
                Objects.equals(name, planetDto.name) &&
                Objects.equals(climate, planetDto.climate) &&
                Objects.equals(terrain, planetDto.terrain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, climate, terrain, movies);
    }
}
