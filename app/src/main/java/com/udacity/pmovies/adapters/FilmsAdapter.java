package com.udacity.pmovies.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.tmdb_model.Images;
import com.udacity.pmovies.tmdb_model.TMDBFilm;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Popular Films RecyclerView Adapter
 */
public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder> {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Log TAG - Class Name */
    private static final String TAG = FilmsAdapter.class.getSimpleName();

    /** Aspect Ratio of TMDBFilm poster (PosterWidth / PosterHeight) */
    private static final double FILM_POSTER_ASPECT_RATIO = 0.6537;
    /** Default TMDB base URL for displaying DB images */
    private static final String DEFAULT_BASE_URL = "https://image.tmdb.org/t/p/";
    /** Default TMDB film poster width */
    private static final String DEFAULT_POSTER_WIDTH = "w185";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** List of films - Model data ArrayList<TMDBFilm> */
    private ArrayList<TMDBFilm> mTMDBFilmList;
    /** TMDBFilm listener */
    private OnFilmItemClickListener mFilmListener;
    /** API Configuration - Model data Images */
    private final Images mImages;
    /** Activity Context */
    private Context mContext;
    /**ScreenWidth (in px) - Runtime resize of GridView elements */
    private final int mScreenWidth;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty Constructor for PopularFilms Adapter
     */
    public FilmsAdapter() {
        mTMDBFilmList = new ArrayList<>();
        mFilmListener = null;
        mImages = Images.getInstance();
        mScreenWidth = getScreenWidth();
    }

    /**
     * Constructor which sets the film click-listener (comm with MainActivityFragment)
     *
     * @param listener  film click-listener
     */
    public FilmsAdapter(OnFilmItemClickListener listener) {
        mTMDBFilmList = new ArrayList<>();
        mFilmListener = listener;
        mImages = Images.getInstance();
        mScreenWidth = getScreenWidth();
    }


    //--------------------------------------------------------------------------------|
    //                               Getters/Setters                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Set new data for populating the Adapter from JSON results
     *
     * @param TMDBFilmList  List of PopularFilm objects to display in a list.
     */
    public void setFilmList(ArrayList<TMDBFilm> TMDBFilmList) {
        this.mTMDBFilmList = TMDBFilmList;
    }

    /**
     * Gets new data for populating the Adapter from JSON results
     */
    public ArrayList<TMDBFilm> getmTMDBFilmList() { return mTMDBFilmList; }

    /**
     * Set new TMDBFilm click-listener
     *
     * @param mFilmListener TMDBFilm click-listener
     */
    public void setmFilmListener(OnFilmItemClickListener mFilmListener) {
        this.mFilmListener = mFilmListener;
    }

    /**
     * Get a TMDBFilm
     *
     * @param   position    TMDBFilm position in GridView
     * @return  TMDBFilm        TMDBFilm parcelable object
     */
    public TMDBFilm getFilm(int position) {
        if(mTMDBFilmList != null) { return mTMDBFilmList.get(position); }
        else { return null; }
    }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_film_layout, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(mTMDBFilmList != null) {
            // Retrieve data from Model object
            TMDBFilm popularTMDBFilm = mTMDBFilmList.get(position);

            // Set position-tag
            holder.filmLayout.setTag(position);

            // Populate UI elements
            populateUIView(holder, popularTMDBFilm);

            // Add OnClickListeners
            setOnClickListenerView(holder);
        }
    }

    @Override
    public int getItemCount() {
        if (mTMDBFilmList != null) {
            return mTMDBFilmList.size();
        } else {
            return 0;
        }
    }


    //--------------------------------------------------------------------------------|
    //                               ViewHolder                                       |
    //--------------------------------------------------------------------------------|

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


    //--------------------------------------------------------------------------------|
    //                                UI Methods                                      |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder    ViewHolder (View container)
     * @param TMDBFilm      TMDBFilm parcelable object
     */
    private void populateUIView(ViewHolder holder, TMDBFilm TMDBFilm) {
        // Set poster image
        // Build poster path:
        // base_url + file_size + file_path
        // TODO Choose best poster-size according to device resolution
        String filmPosterURL;
        /*
        if(mImages != null) {
            filmPosterURL =
                    mImages.getBaseUrl() + "/"
                            + mImages.getPosterSizes().get(2) + "/"
                            + TMDBFilm.getPosterPath();
        } else {
        */
        filmPosterURL =
                    DEFAULT_BASE_URL + "/"
                            + DEFAULT_POSTER_WIDTH + "/"
                            + TMDBFilm.getPosterPath();
        //}
        Picasso
                .with(mContext)
                .load(filmPosterURL)
                .fit()
                .centerCrop()
                .error(R.drawable.im_image_not_available)
                .into(holder.filmPosterImageView);
        // Set title of TMDBFilm
        holder.filmTitleTextView.setText(TMDBFilm.getTitle());
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    //--------------------------------------------------------------------------------|
    //                              Support Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Set a film click-listener on the film-view
     *
     * @param    holder    ViewHolder (View container)
     */
    private void setOnClickListenerView(final ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFilmListener != null) {
                    mFilmListener.onItemClick((int)v.getTag());
                }
            }
        });
    }


    //--------------------------------------------------------------------------------|
    //                           Fragment Comm Interfaces                             |
    //--------------------------------------------------------------------------------|

    /**
     * Interface for handling the film-click
     */
    public interface OnFilmItemClickListener {
        void onItemClick(int position);
    }

}