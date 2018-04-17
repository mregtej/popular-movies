package com.udacity.popularmovies.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.PopularFilm;

import java.util.ArrayList;

/**
 * Popular Films RecyclerView Adapter
 */
public class PopularFilmsAdapter extends RecyclerView.Adapter<PopularFilmsAdapter.ViewHolder> {

    /** Log TAG - Class Name */
    private static final String TAG = PopularFilmsAdapter.class.getSimpleName();

    /** List of Popular films - Model data ArrayList */
    private ArrayList<PopularFilm> mPopularFilmList;

    /**
     * Empty Constructor for PopularFilms Adapter
     */
    public PopularFilmsAdapter() {
        mPopularFilmList = new ArrayList<PopularFilm>();
    }

    /**
     * Set new data for populating the Adapter from JSON results
     *
     * @param popularFilmList  List of PopularFilm objects to display in a list.
     */
    public void setPopularFilmList(ArrayList<PopularFilm> popularFilmList) {
        this.mPopularFilmList = popularFilmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.film_item_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // Retrieve data from Model object
        PopularFilm popularFilm = mPopularFilmList.get(position);
        holder.filmPosterImageView.setImageResource(popularFilm.getFilmPoster());
        holder.filmNameTextView.setText(popularFilm.getFilmTitle());
    }

    @Override
    public int getItemCount() {
        return mPopularFilmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView filmPosterImageView;
        private TextView filmNameTextView;
        private View filmLayout;

        public ViewHolder(View v) {
            super(v);
            filmLayout = v;
            filmPosterImageView = (ImageView) v.findViewById(R.id.iv_film_poster);
            filmNameTextView = (TextView) v.findViewById(R.id.tv_film_title);
        }
    }

    public void add(int position, PopularFilm item) {
        mPopularFilmList.add(position, item);
        Log.d(TAG, "New film inserted in position: " + position);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mPopularFilmList.remove(position);
        Log.d(TAG, "Film inserted from position: " + position);
        notifyItemRemoved(position);
    }
}