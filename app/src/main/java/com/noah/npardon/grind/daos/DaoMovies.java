package com.noah.npardon.grind.daos;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noah.npardon.grind.beans.Movie;
import com.noah.npardon.grind.beans.User;
import com.noah.npardon.grind.net.WSConnexionHTTPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaoMovies {
    private static DaoMovies instance = null;
    private final List<Movie> movies;

    private DaoMovies() {
        movies = new ArrayList<>();

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public static DaoMovies getInstance() {
        if (instance == null) {
            instance = new DaoMovies();
        }
        return instance;
    }


    public void getTrendingMovies(DelegateAsyncTask delegate) {
        String url = "3/trending/movie/week?language=en-US";
        Log.d("TAG", "getTrendingMovies: " + url);
        WSConnexionHTTPS wsConnexionHTTPS = new WSConnexionHTTPS() {
            @Override
            protected void onPostExecute(String s) {
                try {
                    getTrendingMoviesFinished(s, delegate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        wsConnexionHTTPS.execute(url, "TMDB");
    }

    private void getTrendingMoviesFinished(String s, DelegateAsyncTask delegate) throws JSONException {
        movies.clear();
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("results")) {
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for(int i = 0 ; i < resultsArray.length() ; i++){
                    JSONObject jsResult = resultsArray.getJSONObject(i);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Movie movie = objectMapper.readValue(jsResult.toString(), Movie.class);
                    movies.add(movie);
                }
            } else if (jsonObject.has("status_message")) {
                String statusMessage = jsonObject.getString("status_message");
                Log.d("TrendingMoviesError", "getTrendingMoviesFinished: "+statusMessage);
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
