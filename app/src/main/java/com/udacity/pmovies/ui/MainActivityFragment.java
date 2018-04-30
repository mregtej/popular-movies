package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
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
import com.udacity.pmovies.model.Images;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {

    /** TAG Name - Logcat */
    private static final String TAG = MainActivity.class.getName();

    /** Popular films Custom ArrayAdapter */
    private FilmsAdapter popularFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** Custom GridView (RecyclerView) */
    @BindView(R.id.rv_films_grid) RecyclerView recyclerView;

    /**
     * Empty constructor
     */
    public MainActivityFragment() { }

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

    // Load & set ArrayAdapter
    public void updateAdapter(ArrayList<Film> popularFilms) {
        popularFilmAdapter.setFilmList(popularFilms);
        recyclerView.setAdapter(popularFilmAdapter);
        popularFilmAdapter.notifyDataSetChanged();
    }

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
