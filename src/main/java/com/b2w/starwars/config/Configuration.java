package com.b2w.starwars.config;

public interface Configuration {
    String getSwapiUrl();

    String getUserAgent();

    String getMongoDbCollectionName();
}
