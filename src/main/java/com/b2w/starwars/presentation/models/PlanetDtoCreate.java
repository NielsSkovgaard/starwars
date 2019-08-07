package com.b2w.starwars.presentation.models;

import com.b2w.starwars.core.PresentationCreateObject;

import java.util.Objects;

public class PlanetDtoCreate implements PresentationCreateObject {
    private String name;
    private String climate;
    private String terrain;

    public PlanetDtoCreate() {
    }

    public PlanetDtoCreate(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanetDtoCreate that = (PlanetDtoCreate) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(climate, that.climate) &&
                Objects.equals(terrain, that.terrain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, climate, terrain);
    }
}
