package com.noah.npardon.grind.beans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noah.npardon.grind.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movies;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvLVMovieName);
            image = view.findViewById(R.id.tvLVMovieImg);

        }
    }

    public MoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_movies, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}


