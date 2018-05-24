package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.pmovies.R;
import com.udacity.pmovies.database_model.FavMovie;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.tmdb_model.Film;
import com.udacity.pmovies.adapters.FilmsAdapter;
import com.udacity.pmovies.view_model.FavMoviesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies MainActivityFragment
 *
 * TODO Implement MVP pattern
 */
public class MainActivityFragment extends Fragment implements FilmsAdapter.OnFilmItemClickListener {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class Name - Log TAG */
    private static final String TAG = MainActivity.class.getName();
    /** Key for storing the list state in savedInstanceState */
    private static final String LIST_STATE_KEY = "list-state";
    /** Key for retrieving Film parcelable object from intent */
    private static final String FILM_EXTRA = "film";
    /** Key for identifying if film is in favorite film list */
    private static final String IS_IN_FAVS_EXTRA = "is-in-favs";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** Popular films Custom ArrayAdapter */
    private FilmsAdapter mFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** Custom GridView (RecyclerView) */
    @BindView(R.id.rv_films_grid) RecyclerView recyclerView;
    /** Activity Context */
    private Context mContext;
    /** List state stored in savedInstanceState */
    private Parcelable mListState;
    /** Favorite film list */
    private List<FavMovie> mFavMovies;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty constructor
     */
    public MainActivityFragment() { }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate Views
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        recyclerView.setHasFixedSize(true);
        mContext = rootView.getContext();

        // Load & set GridLayout
        setRecyclerViewLayoutManager(rootView);

        // Load & set ArrayAdapter
        mFilmAdapter = new FilmsAdapter(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save list state
        mListState = layoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }


    //--------------------------------------------------------------------------------|
    //                                  Setters                                       |
    //--------------------------------------------------------------------------------|

    /**
     * Set favorite movie list (retrieved via FavMoviesViewModel)
     *
     * @param mFavMovies    favorite movie list
     */
    public void setmFavMovies(List<FavMovie> mFavMovies) {
        this.mFavMovies = mFavMovies;
    }

    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Set-up and load RecyclerView's ArrayAdapter
     *
     * @param   popularFilms    List of films retrieved from TMDB
     */
    public void updateAdapter(ArrayList<Film> popularFilms) {
        mFilmAdapter.setFilmList(popularFilms);
        recyclerView.setAdapter(mFilmAdapter);
        mFilmAdapter.notifyDataSetChanged();
    }

    /**
     * Set RecyclerView's LayoutManager.
     * Different layouts for portrait and landscape mode
     *
     * @param   rootView    Activity view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        switch(this.getResources().getConfiguration().orientation) {
            case GlobalsPopularMovies.LANDSCAPE_VIEW: // Landscape Mode
                layoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        GlobalsPopularMovies.GRIDVIEW_LANDSCAPE_NUMBER_OF_COLUMNS);
                break;
            case GlobalsPopularMovies.PORTRAIT_VIEW: // Portrait Mode
            default:
                layoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        GlobalsPopularMovies.GRIDVIEW_PORTRAIT_NUMBER_OF_COLUMNS);
                break;
        }
        recyclerView.setLayoutManager(layoutManager);
    }


    //--------------------------------------------------------------------------------|
    //                           Adapter --> Fragment comm                            |
    //--------------------------------------------------------------------------------|

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(mContext, DetailFilmActivity.class);
        Film film = mFilmAdapter.getFilm(position);
        i.putExtra(FILM_EXTRA, film);
        i.putExtra(IS_IN_FAVS_EXTRA, isFilmInFavs(film.getId()));
        this.startActivity(i);
    }


    //--------------------------------------------------------------------------------|
    //                                Support Methods                                 |
    //--------------------------------------------------------------------------------|

    /**
     * Checks if current film is already included in favorite film list.
     *
     * @param   film_id     Film ID (uid)
     * @return  true, if film is in favorite film list; false, otherwise
     */
    private boolean isFilmInFavs(int film_id) {
        if(mFavMovies != null) {
            for (FavMovie favMovie : mFavMovies) {
                if (film_id == favMovie.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

}
