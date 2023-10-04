package com.noah.npardon.grind.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noah.npardon.grind.R;
import com.noah.npardon.grind.beans.Genre;
import com.noah.npardon.grind.beans.Movie;
import com.noah.npardon.grind.beans.MoviesAdapter;
import com.noah.npardon.grind.daos.DaoMovies;
import com.noah.npardon.grind.daos.DelegateAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFragment extends Fragment {
    private MoviesAdapter trendingAdapter;
    private MoviesAdapter mostPopularAdapter;
    private MoviesAdapter mostVotedAdapter;
    private TextView mainMovieGenre, mainMovieTitle;
    private ImageView mainMovieImg;
    private List<Movie> trendingsL;
    private List<Movie> moviesVotedL;
    private List<Movie> moviesPopularL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        trendingsL = DaoMovies.getInstance().getTrendings();
        moviesPopularL = DaoMovies.getInstance().getMovies();
        moviesVotedL = DaoMovies.getInstance().getMovies();

        mainMovieTitle = ((TextView) v.findViewById(R.id.txMainMovie));
        mainMovieGenre = ((TextView) v.findViewById(R.id.txMainMovieGenre));
        mainMovieImg = ((ImageView) v.findViewById(R.id.imgMainMovie));
        if (DaoMovies.getInstance().getGenres().isEmpty()) {
            DaoMovies.getInstance().getMovieGenres(new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {

                }
            });
        }

        RecyclerView recyclerViewMostPopular = ((RecyclerView) v.findViewById(R.id.rvMoviesPopular));
        RecyclerView recyclerViewTrending = ((RecyclerView) v.findViewById(R.id.rvMoviesTrending));
        RecyclerView recyclerViewMostVoted = ((RecyclerView) v.findViewById(R.id.rvMoviesMostVoted));

        trendingAdapter = new MoviesAdapter(trendingsL);
        mostPopularAdapter = new MoviesAdapter(moviesPopularL);
        mostVotedAdapter = new MoviesAdapter(moviesVotedL);

        LinearLayoutManager mLayoutManagerTrending = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerTrending.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager mLayoutManagerMostPopular = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerMostPopular.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager mLayoutManagerMostVoted = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerMostVoted.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerViewTrending.setLayoutManager(mLayoutManagerTrending);
        recyclerViewTrending.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTrending.setAdapter(trendingAdapter);

        recyclerViewMostPopular.setLayoutManager(mLayoutManagerMostPopular);
        recyclerViewMostPopular.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMostPopular.setAdapter(mostPopularAdapter);

        recyclerViewMostVoted.setLayoutManager(mLayoutManagerMostVoted);
        recyclerViewMostVoted.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMostVoted.setAdapter(mostVotedAdapter);


        DaoMovies.getInstance().getTrendingMovies(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                loadTrending();
            }
        });
        DaoMovies.getInstance().getAllMovies(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                loadMostPopular();
            }
        });

        return v;
    }

    private void loadTrending() {
        Movie movie1 = trendingsL.get(0);
        trendingsL.remove(0);
        mainMovieTitle.setText(movie1.getTitle());
        String genres = "";
        List<Genre> genresList = DaoMovies.getInstance().getGenres();
        for (int i = 0; i < genresList.size(); i++) {
            for (Genre g : movie1.getGenre_ids()) {
                if (g.getId() == genresList.get(i).getId()) {
                    genres += genresList.get(i).getName() + " - ";
                }
            }
        }
        mainMovieGenre.setText(genres.substring(0, genres.length() - 2));
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie1.getPoster_path()).into(mainMovieImg);
        trendingAdapter.notifyDataSetChanged();
    }

    private void loadMostPopular() {
        sortMostPopular();
        mostPopularAdapter.notifyDataSetChanged();
    }

    private void sortMostPopular(){
        int temp;
        boolean swapped;
        for (int i = 0; i < moviesPopularL.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < moviesPopularL.size() - i - 1; j++) {
                if (moviesPopularL.get(j).getVote_count() < moviesPopularL.get(j + 1).getVote_count()) {
                    temp = moviesPopularL.get(j).getVote_count();
                    moviesPopularL.get(j).setVote_count(moviesPopularL.get(j + 1).getVote_count());
                    moviesPopularL.get(j + 1).setVote_count(temp);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }

    private void loadMostVoted() {
        sortMostVoted();
        mostPopularAdapter.notifyDataSetChanged();
    }

    private void sortMostVoted(){
        int temp;
        boolean swapped;
        for (int i = 0; i < moviesVotedL.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < moviesPopularL.size() - i - 1; j++) {
                if (moviesPopularL.get(j).getVote_count() < moviesPopularL.get(j + 1).getVote_count()) {
                    temp = moviesPopularL.get(j).getVote_count();
                    moviesPopularL.get(j).setVote_count(moviesPopularL.get(j + 1).getVote_count());
                    moviesPopularL.get(j + 1).setVote_count(temp);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }
}