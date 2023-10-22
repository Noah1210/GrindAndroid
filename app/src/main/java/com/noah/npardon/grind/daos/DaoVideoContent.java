package com.noah.npardon.grind.daos;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.grind.beans.Genre;
import com.noah.npardon.grind.beans.Movie;
import com.noah.npardon.grind.beans.Show;
import com.noah.npardon.grind.beans.VideoContent;
import com.noah.npardon.grind.net.WSConnexionHTTPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DaoVideoContent {
    private static DaoVideoContent instance = null;
    private final List<VideoContent> trendings;
    private final List<VideoContent> popular;
    private final List<VideoContent> mostVoted;
    private final List<Genre> movieGenres;
    private final List<Genre> showGenres;

    private DaoVideoContent() {
        trendings = new ArrayList<>();
        movieGenres = new ArrayList<>();
        showGenres = new ArrayList<>();
        popular = new ArrayList<>();
        mostVoted = new ArrayList<>();
    }

    public List<VideoContent> getTrendings() {
        return trendings;
    }

    public List<VideoContent> getPopular() {
        return popular;
    }

    public List<VideoContent> getMostVoted() {
        return mostVoted;
    }

    public List<Genre> getMovieGenres() {
        return movieGenres;
    }

    public List<Genre> getShowGenres() {
        return showGenres;
    }

    public static DaoVideoContent getInstance() {
        if (instance == null) {
            instance = new DaoVideoContent();
        }
        return instance;
    }


    public void getTrending(DelegateAsyncTask delegate) {
        String url = "3/trending/all/week?language=en-US";
        Log.d("TAG", "getTrendingMovies: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getTrendingFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }

    private void getTrendingFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        trendings.clear();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    if (jsResult.getString("media_type").equals("movie")) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Movie movie = objectMapper.readValue(jsResult.toString(), Movie.class);
                        trendings.add(movie);
                    }else if (jsResult.getString("media_type").equals("tv")){
                        ObjectMapper objectMapper = new ObjectMapper();
                        Show show = objectMapper.readValue(jsResult.toString(), Show.class);
                        trendings.add(show);
                    }
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("TrendingMoviesError", "getTrendingMoviesFinished: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }

    public void getBothPopular(DelegateAsyncTask delegate){
        popular.clear();
        getMoviePopular(delegate);
        getShowPopular(delegate);

    }



    public void getMoviePopular(DelegateAsyncTask delegate) {
        String url = "3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc";
        Log.d("TAG", "getPopularMovies: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getMoviePopularFinihed(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }


    private void getMoviePopularFinihed(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Movie movie = objectMapper.readValue(jsResult.toString(), Movie.class);
                        popular.add(movie);
                    }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("PopularMoviesError", "getMoviePopularFinihed: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }

    public void getShowPopular(DelegateAsyncTask delegate) {
        String url = "3/discover/tv?include_adult=false&include_null_first_air_dates=false&language=en-US&page=1&sort_by=popularity.desc";
        Log.d("TAG", "getPopularMovies: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getShowPopularFinihed(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }


    private void getShowPopularFinihed(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Show show = objectMapper.readValue(jsResult.toString(), Show.class);
                    popular.add(show);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("PopularShowError", "getShowPopularFinihed: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }


    public void getBothMostVoted(DelegateAsyncTask delegate){
        mostVoted.clear();
        getMostVotedMovies(delegate);
        getMostVotedShows(delegate);

    }



    public void getMostVotedMovies(DelegateAsyncTask delegate) {
        String url = "3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=vote_count.desc";
        Log.d("TAG", "getMostVotedMovies: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getMovieMostVotedFinihed(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }


    private void getMovieMostVotedFinihed(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Movie movie = objectMapper.readValue(jsResult.toString(), Movie.class);
                    mostVoted.add(movie);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("MostVotedMoviesError", "getMovieMostVotedFinihed: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }

    public void getMostVotedShows(DelegateAsyncTask delegate) {
        String url = "3/discover/tv?include_adult=false&include_null_first_air_dates=false&language=en-US&page=1&sort_by=vote_count.desc";
        Log.d("TAG", "getMostVotedShows: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getShowMostVotedFinihed(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }


    private void getShowMostVotedFinihed(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Show show = objectMapper.readValue(jsResult.toString(), Show.class);
                    mostVoted.add(show);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("MostVotedShowsError", "getShowMostVotedFinished: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }

    public void getMovieGenres(DelegateAsyncTask delegate) {
        String url = "3/genre/movie/list?language=en";
        Log.d("TAG", "getGenresMovie: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getMovieGenresFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }

    private void getMovieGenresFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("genres")) {
                JSONArray resultsArray = jsonObject.getJSONArray("genres");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Genre genre = objectMapper.readValue(jsResult.toString(), Genre.class);
                    movieGenres.add(genre);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("MovieGenresError", "getMovieGenresFinished: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }

    public void getShowGenres(DelegateAsyncTask delegate) {
        String url = "3/genre/tv/list?language=en";
        Log.d("TAG", "getGenresShow: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getShowGenresFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }

    private void getShowGenresFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("genres")) {
                JSONArray resultsArray = jsonObject.getJSONArray("genres");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Genre genre = objectMapper.readValue(jsResult.toString(), Genre.class);
                    showGenres.add(genre);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("ShowGenresError", "getShowGenresFinished: " + statusMessage);
            } else {
                System.out.println("Unknown JSON structure.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        delegate.whenWSIsTerminated(s);
    }


}
