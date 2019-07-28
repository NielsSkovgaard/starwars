package com.b2w.starwars.application.services;

public interface RestService {
    <T> T get(String url, Class<T> clazz);
}
