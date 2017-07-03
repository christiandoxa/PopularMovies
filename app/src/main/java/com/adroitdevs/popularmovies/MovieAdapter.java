package com.adroitdevs.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private final MovieAdapterOnClickHandler mClickHandler;
    private String[][] mMovieData;
    private Context context;

    MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForGridItem, viewGroup, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String imageForMovie = mMovieData[position][1];
        String urlImage = "http://image.tmdb.org/t/p/w185/" + imageForMovie;
        String number = String.valueOf(position + 1);
        movieAdapterViewHolder.mNumberPosition.setText(" " + number + " ");
        ImageView imageDisplay = movieAdapterViewHolder.mDisplayImagePoster;
        Glide.with(context).load(urlImage).into(imageDisplay);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null)
            return 0;
        return mMovieData.length;
    }

    void setMovieData(String[][] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Map<String, String> infoDetail);
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mDisplayImagePoster;
        TextView mNumberPosition;

        MovieAdapterViewHolder(View view) {
            super(view);
            mNumberPosition = (TextView) view.findViewById(R.id.number_position);
            mDisplayImagePoster = (ImageView) view.findViewById(R.id.iv_image_display);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Map<String, String> infoDetail = new HashMap<>();
            infoDetail.put("title", mMovieData[adapterPosition][0]);
            infoDetail.put("image", mMovieData[adapterPosition][1]);
            infoDetail.put("synopsis", mMovieData[adapterPosition][2]);
            infoDetail.put("rating", mMovieData[adapterPosition][3]);
            infoDetail.put("releaseDate", mMovieData[adapterPosition][4]);
            mClickHandler.onClick(infoDetail);
        }
    }

}
