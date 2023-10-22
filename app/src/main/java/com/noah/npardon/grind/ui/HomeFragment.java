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
import com.noah.npardon.grind.beans.Show;
import com.noah.npardon.grind.beans.VideoAdapter;
import com.noah.npardon.grind.beans.VideoContent;
import com.noah.npardon.grind.daos.DaoVideoContent;
import com.noah.npardon.grind.daos.DelegateAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    private VideoAdapter trendingAdapter, mostPopularAdapter, mostVotedAdapter;
    private TextView mainTrendingGenre, mainTrendingTitle;
    private ImageView mainTrendingImg;
    private RecyclerView recyclerViewMostPopular, recyclerViewTrending, recyclerViewMostVoted;
    private List<VideoContent> trendingsL, votedL, popularL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mainTrendingTitle = ((TextView) v.findViewById(R.id.txMainTrending));
        mainTrendingGenre = ((TextView) v.findViewById(R.id.txMainTrendingGenre));
        mainTrendingImg = ((ImageView) v.findViewById(R.id.imgMainTrendingPoster));

        recyclerViewMostPopular = ((RecyclerView) v.findViewById(R.id.rvPopular));
        recyclerViewTrending = ((RecyclerView) v.findViewById(R.id.rvTrending));
        recyclerViewMostVoted = ((RecyclerView) v.findViewById(R.id.rvMostVoted));

        setListViews();
        setAdapters();
        setLayoutManagers();
        getAllGenres();
        getAllListViewsData();

        return v;
    }

    private void getAllListViewsData(){
        DaoVideoContent.getInstance().getTrending(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                loadVideoContent();
            }
        });
        DaoVideoContent.getInstance().getBothPopular(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                sortListView("popular");
                loadMostPopular();
            }
        });

        DaoVideoContent.getInstance().getBothMostVoted(new DelegateAsyncTask() {
            @Override
            public void whenWSIsTerminated(Object result) {
                sortListView("mostVoted");
                loadMostVoted();
            }
        });
    }

    private void sortPopular(){
        boolean swapped;
        VideoContent temp;
        for (int i = 0; i < popularL.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < popularL.size() - i - 1; j++) {
                if (popularL.get(j).getPopularity() < popularL.get(j + 1).getPopularity()) {
                    temp = popularL.get(j);
                    popularL.set(j, (popularL.get(j + 1)));
                    popularL.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }
    private void sortMostVoted(){
        boolean swapped;
        VideoContent temp;
        for (int i = 0; i < votedL.size() - 1; i++) {
            swapped = false;
            for (int j = 0; j < votedL.size() - i - 1; j++) {
                if (votedL.get(j).getVote_count() < votedL.get(j + 1).getVote_count()) {
                    temp = votedL.get(j);
                    votedL.set(j, (votedL.get(j + 1)));
                    votedL.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }

    private void sortListView(String type){
        if(type.equals("popular")) {
            sortPopular();
        }else{
            sortMostVoted();
        }
    }



    private void getAllGenres(){
        if (DaoVideoContent.getInstance().getMovieGenres().isEmpty()) {
            DaoVideoContent.getInstance().getMovieGenres(new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {

                }
            });
        }
        if (DaoVideoContent.getInstance().getShowGenres().isEmpty()) {
            DaoVideoContent.getInstance().getShowGenres(new DelegateAsyncTask() {
                @Override
                public void whenWSIsTerminated(Object result) {

                }
            });
        }
    }

    private void setListViews(){
        trendingsL = DaoVideoContent.getInstance().getTrendings();
        popularL = DaoVideoContent.getInstance().getPopular();
        votedL = DaoVideoContent.getInstance().getMostVoted();
    }

    private void setAdapters(){
        trendingAdapter = new VideoAdapter(trendingsL);
        mostPopularAdapter = new VideoAdapter(popularL);
        mostVotedAdapter = new VideoAdapter(votedL);
    }

    private void setLayoutManagers(){
        LinearLayoutManager mLayoutManagerTrending = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerTrending.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager mLayoutManagerMostPopular = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerMostPopular.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager mLayoutManagerMostVoted = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerMostVoted.setOrientation(LinearLayoutManager.HORIZONTAL);

        setRecyclerViews(mLayoutManagerTrending, mLayoutManagerMostPopular, mLayoutManagerMostVoted);
    }

    private void setRecyclerViews(LinearLayoutManager mLayoutManagerTrending, LinearLayoutManager mLayoutManagerMostPopular, LinearLayoutManager mLayoutManagerMostVoted){
        recyclerViewTrending.setLayoutManager(mLayoutManagerTrending);
        recyclerViewTrending.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTrending.setAdapter(trendingAdapter);

        recyclerViewMostPopular.setLayoutManager(mLayoutManagerMostPopular);
        recyclerViewMostPopular.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMostPopular.setAdapter(mostPopularAdapter);

        recyclerViewMostVoted.setLayoutManager(mLayoutManagerMostVoted);
        recyclerViewMostVoted.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMostVoted.setAdapter(mostVotedAdapter);
    }

    private void loadVideoContent() {
        if (trendingsL.get(0) instanceof Movie) {
            loadTrendingMovie((Movie) trendingsL.get(0));
        } else if (trendingsL.get(0) instanceof Show) {
            loadTrendingShow((Show) trendingsL.get(0));
        }
        trendingAdapter.notifyDataSetChanged();
    }


    private String getGenresString(List<Genre> genreIds, List<Genre> genresList){
        StringBuilder genres = new StringBuilder();
        for (Genre genre : genresList) {
            for (Genre g : genreIds) {
                if (g.getId() == genre.getId()) {
                    genres.append(genre.getName()).append(" - ");
                }
            }
        }
        return genres.substring(0, genres.length() - 2);
    }


    private void loadTrendingMovie(Movie m){
        trendingsL.remove(0);
        mainTrendingTitle.setText(m.getTitle());
        List<Genre> genresList = DaoVideoContent.getInstance().getMovieGenres();
        String genres = getGenresString(m.getGenre_ids(), genresList);
        mainTrendingGenre.setText(genres);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + m.getPoster_path()).into(mainTrendingImg);
    }

    private void loadTrendingShow(Show s){
        trendingsL.remove(0);
        mainTrendingTitle.setText(s.getName());
        List<Genre> genresList = DaoVideoContent.getInstance().getShowGenres();
        String genres = getGenresString(s.getGenre_ids(), genresList);
        mainTrendingGenre.setText(genres);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + s.getPoster_path()).into(mainTrendingImg);
    }


    private void loadMostPopular() {
        mostPopularAdapter.notifyDataSetChanged();
    }



    private void loadMostVoted() {
        mostVotedAdapter.notifyDataSetChanged();
    }

}