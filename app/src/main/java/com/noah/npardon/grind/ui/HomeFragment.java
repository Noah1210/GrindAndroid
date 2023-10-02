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
import android.widget.LinearLayout;

import com.noah.npardon.grind.R;
import com.noah.npardon.grind.beans.Movie;
import com.noah.npardon.grind.beans.MoviesAdapter;
import com.noah.npardon.grind.daos.DaoMovies;
import com.noah.npardon.grind.daos.DelegateAsyncTask;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private MoviesAdapter moviesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
//        Movie m1 = new Movie("La chasse");
//        Movie m2 = new Movie("Lapin");
//        Movie m3 = new Movie("Jessy");
//        Movie m4 = new Movie("Smile");
//        List<Movie> movies = new ArrayList<>();
//        movies.add(m1);
//        movies.add(m2);
//        movies.add(m3);
//        movies.add(m4);

        List<Movie> movies = DaoMovies.getInstance().getMovies();
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
                moviesAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }
}