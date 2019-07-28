package com.b2w.starwars.config;

import org.springframework.beans.factory.annotation.Value;

@org.springframework.context.annotation.Configuration
public class ConfigurationImpl implements Configuration {
    @Value("${swapi-url}")
    private String swapiUrl;

    @Value("${user-agent}")
    private String userAgent;

    @Value("${mongodb-collection-name}")
    private String mongoDbCollectionName;

    @Override
    public String getSwapiUrl() {
        return swapiUrl;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String getMongoDbCollectionName() {
        return mongoDbCollectionName;
    }
}
