package com.noah.npardon.grind.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noah.npardon.grind.R;
import com.noah.npardon.grind.beans.Genre;
import com.noah.npardon.grind.beans.Movie;
import com.noah.npardon.grind.beans.MoviesAdapter;
import com.noah.npardon.grind.daos.DaoMovies;
import com.noah.npardon.grind.daos.DelegateAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private MoviesAdapter moviesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        List<Movie> movies = DaoMovies.getInstance().getMovies();
        TextView mainMovieTitle = ((TextView) v.findViewById(R.id.txMainMovie));
        TextView mainMovieGenre = ((TextView) v.findViewById(R.id.txMainMovieGenre));
        ImageView mainMovieImg = ((ImageView) v.findViewById(R.id.imgMainMovie));
        if(DaoMovies.getInstance().getGenres().isEmpty()){
            DaoMovies.getInstance().getMovieGenres(new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {

                }
            });
        }

        RecyclerView recyclerView = ((RecyclerView) v.findViewById(R.id.rvMoviesTrending));
        moviesAdapter = new MoviesAdapter(movies);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);
        DaoMovies.getInstance().getTrendingMovies(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                Movie movie1 = movies.get(0);
                movies.remove(0);
                mainMovieTitle.setText(movie1.getTitle());
                String genres = "";
                List<Genre> genresList = DaoMovies.getInstance().getGenres();
                for(int i = 0 ; i < genresList.size(); i++){
                    for(Genre g : movie1.getGenre_ids()){
                        if(g.getId() == genresList.get(i).getId()){
                            genres += genresList.get(i).getName()+ " - ";
                        }
                    }
                }
                mainMovieGenre.setText(genres.substring(0, genres.length() - 2));
                Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie1.getPoster_path()).into(mainMovieImg);
                moviesAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }
}