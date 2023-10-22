package com.noah.npardon.grind.beans;

import java.util.List;

public abstract class VideoContent implements Comparable<VideoContent> {
    private boolean adult;
    private String backdrop_path;
    private int id;
    private String original_language;
    private String overview;
    private String poster_path;
    private String media_type;
    private List<Genre> genre_ids;
    private float popularity;
    private float vote_average;
    private int vote_count;


    public VideoContent() {
    }

    public VideoContent(boolean adult, String backdrop_path, int id, String original_language, String overview, String poster_path, String media_type, List<Genre> genre_ids, float popularity, float vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.id = id;
        this.original_language = original_language;
        this.overview = overview;
        this.poster_path = poster_path;
        this.media_type = media_type;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }


    @Override
    public int compareTo(VideoContent d) {
        return (int) (this.getPopularity() - d.getPopularity());
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

    public String getOriginal_language() {
        return original_language;
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

    public float getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }
}


