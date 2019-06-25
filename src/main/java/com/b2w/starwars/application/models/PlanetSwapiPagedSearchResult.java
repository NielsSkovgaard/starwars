package com.b2w.starwars.application.models;

import java.util.List;

public class PlanetSwapiPagedSearchResult {
    private int count;
    private String next;
    private String previous;
    private List<PlanetSwapi> results;

    public PlanetSwapiPagedSearchResult() {
    }

    public PlanetSwapiPagedSearchResult(int count, String next, String previous, List<PlanetSwapi> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PlanetSwapi> getResults() {
        return results;
    }

    public void setResults(List<PlanetSwapi> results) {
        this.results = results;
    }
}
