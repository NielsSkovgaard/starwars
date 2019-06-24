package com.b2w.starwars.presentation.models;

public class PlanetDto {
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
}
