package com.b2w.starwars.domain.models;

import java.util.List;

public class PlanetSwapiSearchResult {
    private long count;
    private String next;
    private String previous;
    private List<PlanetSwapi> results;

    public PlanetSwapiSearchResult() {
    }

    public PlanetSwapiSearchResult(long count, String next, String previous, List<PlanetSwapi> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
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
