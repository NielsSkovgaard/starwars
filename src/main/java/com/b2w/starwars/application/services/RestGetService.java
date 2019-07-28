package com.b2w.starwars.application.services;

public interface RestGetService {
    <T> T get(String url, String userAgent, Class<T> clazz);
}
