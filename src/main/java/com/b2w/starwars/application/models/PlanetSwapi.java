package com.b2w.starwars.domain.models;

import java.util.List;

public class PlanetSwapi {
    private String name;
    private List<String> films;

    public PlanetSwapi() {
    }

    public PlanetSwapi(String name, List<String> films) {
        this.name = name;
        this.films = films;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }
}
