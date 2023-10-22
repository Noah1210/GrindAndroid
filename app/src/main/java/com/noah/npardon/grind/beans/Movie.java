package com.noah.npardon.grind.beans;

import java.util.List;

public class Movie extends VideoContent{
    private String title;
    private String original_title;
    private String release_date;
    private boolean video;


    public Movie(String title, String original_title, String release_date, boolean video) {
        this.title = title;
        this.original_title = original_title;
        this.release_date = release_date;
        this.video = video;
    }

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isVideo() {
        return video;
    }

}
