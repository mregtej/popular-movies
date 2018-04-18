package com.udacity.popularmovies.ui;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Film;
import com.udacity.popularmovies.adapter.FilmsAdapter;
import com.udacity.popularmovies.model.Images;

import java.util.ArrayList;

public class MainActivityFragment extends Fragment {

    /**Default number of Grid-columns */
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 3;
    /** TAG Name - Logcat */
    private static final String TAG = MainActivity.class.getName();

    /** Popular films Custom ArrayAdapter */
    private FilmsAdapter popularFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView;

    /**
     * Empty constructor
     */
    public MainActivityFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate Views
        // TODO - Butterknive usage
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_popular_films);
        recyclerView.setHasFixedSize(true);

        // TODO Calculate optimal number of Grid-columns
        // int numberOfColumns = calculateOptimalNumberOfColumns();

        // Load & set GridLayout
        layoutManager = new GridLayoutManager(rootView.getContext(), DEFAULT_NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);

        // Load & set ArrayAdapter
        popularFilmAdapter = new FilmsAdapter();
        recyclerView.setAdapter(popularFilmAdapter);

        return rootView;
    }

    // Load & set ArrayAdapter
    public void updateAdapter(ArrayList<Film> popularFilms) {
        popularFilmAdapter.setFilmList(popularFilms);
        popularFilmAdapter.notifyDataSetChanged();
    }

    // Load & set ArrayAdapter
    public void updateAPIConfiguration(Images images) {
        popularFilmAdapter.setImages(images);
    }

    private int calculateOptimalNumberOfColumns() {
        return 0;
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
