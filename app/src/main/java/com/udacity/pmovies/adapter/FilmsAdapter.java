package com.udacity.pmovies.adapter;

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

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.model.Images;
import com.udacity.pmovies.ui.DetailFilmActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Popular Films RecyclerView Adapter
 */
public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    /** Log TAG - Class Name */
    private static final String TAG = FilmsAdapter.class.getSimpleName();

    /** Aspect Ratio of Film poster (PosterWidth / PosterHeight) */
    private static final double FILM_POSTER_ASPECT_RATIO = 0.6537;

    private static final String DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String DEFAULT_POSTER_WIDTH = "w185";

    /** List of films - Model data ArrayList<Film> */
    private ArrayList<Film> mFilmList;
    /** API Configuration - Model data Images */
    private final Images mImages;
    /** Activity Context */
    private Context mContext;

    private final int mScreenWidth;

    /**
     * Empty Constructor for PopularFilms Adapter
     */
    public FilmsAdapter() {
        mFilmList = new ArrayList<>();
        mImages = Images.getInstance();
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
        @BindView(R.id.iv_film_poster) ImageView filmPosterImageView;
        @BindView(R.id.tv_film_title) TextView filmTitleTextView;
        private final View filmLayout;

        public ViewHolder(View v) {
            super(v);
            filmLayout = v;
            ButterKnife.bind(this, v);
            filmPosterImageView = v.findViewById(R.id.iv_film_poster);
            filmTitleTextView = v.findViewById(R.id.tv_film_title);

            // Resize film poster size
            resizeFilmPoster();
        }

        /**
         * Resize the film poster according to screen-sizes and the following conditions:
         *  - 3 films per row in portrait view
         *  - 4 films per row in landscape mode
         */
        private void resizeFilmPoster() {
            int width;
            int height;
            switch (mContext.getResources().getConfiguration().orientation) {
                case GlobalsPopularMovies.LANDSCAPE_VIEW: // Landscape Mode
                    width = ((mScreenWidth) / GlobalsPopularMovies.GRIDVIEW_LANDSCAPE_NUMBER_OF_COLUMNS);
                    height = (int) (width / FILM_POSTER_ASPECT_RATIO);
                    filmPosterImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    break;
                case GlobalsPopularMovies.PORTRAIT_VIEW: // Portrait Mode
                default:
                    width = ((mScreenWidth) / GlobalsPopularMovies.GRIDVIEW_PORTRAIT_NUMBER_OF_COLUMNS);
                    height = (int) (width / FILM_POSTER_ASPECT_RATIO);
                    filmPosterImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
                    break;
            }
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
        // TODO Choose best poster-size according to device resolution
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
        Picasso
                .with(mContext)
                .load(filmPosterURL)
                .fit()
                .centerCrop()
                .error(R.drawable.im_image_not_available)
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