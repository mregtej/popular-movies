package com.udacity.popularmovies;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.popularmovies.model.PopularFilm;
import com.udacity.popularmovies.utils.PopularFilmsAdapter;

import java.util.Arrays;

public class MainActivityFragment extends Fragment {

    /**Default number of Grid-columns */
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 3;
    /** TAG Name - Logcat */
    private static final String TAG = MainActivity.class.getName();

    /** Popular films Custom ArrayAdapter */
    private PopularFilmsAdapter popularFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;

    PopularFilm[] popularFilms = {
            new PopularFilm("Film 1", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 2", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 3", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 4", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 5", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 6", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 7", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 8", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 9", R.drawable.image_not_available_drawable),
            new PopularFilm("Film 10",R.drawable.image_not_available_drawable)
    };

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
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_popular_films);
        recyclerView.setHasFixedSize(true);

        // TODO Calculate optimal number of Grid-columns
        // int numberOfColumns = calculateOptimalNumberOfColumns();

        // Load & set GridLayout
        layoutManager = new GridLayoutManager(rootView.getContext(), DEFAULT_NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);

        // Load & set ArrayAdapter
        popularFilmAdapter = new PopularFilmsAdapter(Arrays.asList(popularFilms));
        recyclerView.setAdapter(popularFilmAdapter);
        return rootView;
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
