package com.b2w.starwars.infrastructure.models;

import com.b2w.starwars.core.Entity;
import com.b2w.starwars.core.InfrastructureObject;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "planets")
public class PlanetMongoDb implements InfrastructureObject, Entity<ObjectId> {
    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String name;

    private String climate;

    private String terrain;

    public PlanetMongoDb() {
    }

    public PlanetMongoDb(String name, String climate, String terrain) {
        this(null, name, climate, terrain);
    }

    public PlanetMongoDb(ObjectId id, String name, String climate, String terrain) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanetMongoDb that = (PlanetMongoDb) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(climate, that.climate) &&
                Objects.equals(terrain, that.terrain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, climate, terrain);
    }
}
