package com.noah.npardon.grind.beans;

import java.util.List;

public class Movie {
    private boolean adult;
    private String backdrop_path;
    private int id;
    private String title;
    private String original_language;
    private String original_title;
    private String overview;
    private String poster_path;
    private String media_type;
    private List<Genre> genre_ids;
    private float popularity;
    private String release_date;
    private boolean video;
    private float vote_average;
    private int vote_count;


    public Movie(boolean adult, String backdrop_path, int id, String title, String original_language, String original_title, String overview, String poster_path, String media_type, List<Genre> genre_ids, float popularity, String release_date, boolean video, float vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.title = title;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.media_type = media_type;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.release_date = release_date;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public Movie() {
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getMedia_type() {
        return media_type;
    }

    public List<Genre> getGenre_ids() {
        return genre_ids;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
