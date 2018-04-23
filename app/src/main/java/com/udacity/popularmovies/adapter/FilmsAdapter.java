package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Film;
import com.udacity.popularmovies.model.Images;
import com.udacity.popularmovies.ui.DetailFilmActivity;

import java.util.ArrayList;

/**
 * Popular Films RecyclerView Adapter
 */
public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    /** Log TAG - Class Name */
    private static final String TAG = FilmsAdapter.class.getSimpleName();

    private static final String DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String DEFAULT_POSTER_WIDTH = "w185";

    /** List of films - Model data ArrayList<Film> */
    private ArrayList<Film> mFilmList;
    /** API Configuration - Model data Images */
    private Images mImages;
    /** Activity Context */
    private Context mContext;

    private int mScreenWidth;
    private int mScreenHeight;

    /**
     * Empty Constructor for PopularFilms Adapter
     */
    public FilmsAdapter() {
        mFilmList = new ArrayList<Film>();
        mImages = null;
        mScreenHeight = getScreenHeight();
        mScreenWidth = getScreenWidth();
    }

    /**
     * Set new data for populating the Adapter from JSON results
     *
     * @param FilmList  List of PopularFilm objects to display in a list.
     */
    public void setFilmList(ArrayList<Film> FilmList) {
        this.mFilmList = FilmList;
    }

    /**
     * Set API Configuration from JSON results
     * @param mImages   Images' API Configuration
     */
    public void setImages(Images mImages) {
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.film_item_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(mFilmList != null) {
            // Retrieve data from Model object
            Film popularFilm = mFilmList.get(position);

            // Set position-tag
            holder.filmLayout.setTag(position);

            // Populate UI elements
            populateUIView(holder, popularFilm);

            // Add OnClickListeners
            setOnClickListenerView(holder);
        }
    }

    @Override
    public int getItemCount() {
        return mFilmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView filmPosterImageView;
        private TextView filmTitleTextView;
        private View filmLayout;

        public ViewHolder(View v) {
            super(v);
            filmLayout = v;
            filmPosterImageView = (ImageView) v.findViewById(R.id.iv_film_poster);
            filmTitleTextView = (TextView) v.findViewById(R.id.tv_film_title);
            // TODO Improve performance (calc of padding & keep ratio-aspect)
            filmPosterImageView.setLayoutParams(new LinearLayout.LayoutParams(
                    (mScreenWidth / 3) - 48,
                    (int)(((mScreenWidth / 3) - 48) * 1.528) ));
        }
    }

    public void add(int position, Film item) {
        mFilmList.add(position, item);
        Log.d(TAG, "New film inserted in position: " + position);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFilmList.remove(position);
        Log.d(TAG, "Film inserted from position: " + position);
        notifyItemRemoved(position);
    }

    private void populateUIView(ViewHolder holder, Film film) {
        // Set poster image
        // Build poster path:
        // base_url + file_size + file_path
        // TODO Adjust width via arrayString file_size
        String filmPosterURL;
        if(mImages != null) {
            filmPosterURL =
                    mImages.getBaseUrl() + "/"
                            + mImages.getPosterSizes().get(2) + "/"
                            + film.getPosterPath();
        } else {
            filmPosterURL =
                    DEFAULT_BASE_URL + "/"
                            + DEFAULT_POSTER_WIDTH + "/"
                            + film.getPosterPath();
        }
        // TODO Resize width/height according to screen sizes
        Picasso
                .with(mContext)
                .load(filmPosterURL)
                .fit()
                .centerCrop()
                .error(R.drawable.image_not_available_drawable)
                .into(holder.filmPosterImageView);
        // Set title of film
        holder.filmTitleTextView.setText(film.getTitle());
    }

    private void setOnClickListenerView(final ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Intent i = new Intent(mContext, DetailFilmActivity.class);
                i.putExtra("film", mFilmList.get(position));
                mContext.startActivity(i);
            }
        });
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}