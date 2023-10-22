package com.noah.npardon.grind.beans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.noah.npardon.grind.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<VideoContent> trendings ;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvLVMovieName);
            image = view.findViewById(R.id.tvLVMovieImg);

        }
    }

    public VideoAdapter(List<VideoContent> trendings) {
        this.trendings = trendings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_movies, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(trendings.get(position) instanceof Movie) {
            Movie movie = (Movie) trendings.get(position);
            holder.title.setText(movie.getTitle());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPoster_path()).into(holder.image);
        }else{
            Show show = (Show) trendings.get(position);
            holder.title.setText(show.getName());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + show.getPoster_path()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return trendings.size();
    }
}


