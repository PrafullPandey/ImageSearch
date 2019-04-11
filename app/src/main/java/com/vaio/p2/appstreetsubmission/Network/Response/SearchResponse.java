package com.vaio.p2.appstreetsubmission.Network.Response;

import java.util.ArrayList;

/**
 * Created by p2 on 12/4/19.
 */

public class SearchResponse {
    ArrayList<Result> results ;

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "results=" + results +
                '}';
    }
}
