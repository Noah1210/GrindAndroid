package com.noah.npardon.grind.beans;

import java.util.List;

public class Show extends VideoContent {
    private String name;
    private String original_name;
    private String first_air_date;
    private List<String> origin_country;


    public Show(String name, String original_name, String first_air_date, List<String> origin_country) {
        this.name = name;
        this.original_name = original_name;
        this.first_air_date = first_air_date;
        this.origin_country = origin_country;
    }

    public Show() {
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }
}
