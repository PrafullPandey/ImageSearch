package com.vaio.p2.appstreetsubmission.Network.Response;

import java.util.ArrayList;

/**
 * Created by p2 on 12/4/19.
 */

public class Result {
    String description ;
    Url urls ;

    public Url getUrls() {
        return urls;
    }

    public void setUrls(Url urls) {
        this.urls = urls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Result{" +
                "description='" + description + '\'' +
                ", urls=" + urls +
                '}';
    }
}
