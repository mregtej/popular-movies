package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.model.Film;
import com.udacity.pmovies.adapter.FilmsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies MainActivityFragment
 *
 * TODO Implement MVP pattern
 */
public class MainActivityFragment extends Fragment {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class Name - Log TAG */
    private static final String TAG = MainActivity.class.getName();
    /** Key for storing the list state */
    private static final String LIST_STATE_KEY = "list-state";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** Popular films Custom ArrayAdapter */
    private FilmsAdapter popularFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** Custom GridView (RecyclerView) */
    @BindView(R.id.rv_films_grid) RecyclerView recyclerView;
    /** List state */
    private Parcelable mListState;


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

        // Load & set GridLayout
        setRecyclerViewLayoutManager(rootView);

        // Load & set ArrayAdapter
        popularFilmAdapter = new FilmsAdapter();

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
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Set-up and load RecyclerView's ArrayAdapter
     *
     * @param   popularFilms    List of films retrieved from TMDB
     */
    public void updateAdapter(ArrayList<Film> popularFilms) {
        popularFilmAdapter.setFilmList(popularFilms);
        recyclerView.setAdapter(popularFilmAdapter);
        popularFilmAdapter.notifyDataSetChanged();
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

}
