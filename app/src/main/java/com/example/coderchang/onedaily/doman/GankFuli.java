package com.example.coderchang.onedaily.doman;

import java.util.List;

/**
 * Created by coderchang on 16/9/3.
 */
public class GankFuli {
    private boolean error;
    private List<GankResult> results;

    public GankFuli(boolean error, List<GankResult> results) {
        this.error = error;
        this.results = results;
    }

    public GankFuli() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankResult> getResults() {
        return results;
    }

    public void setResults(List<GankResult> results) {
        this.results = results;
    }
}
