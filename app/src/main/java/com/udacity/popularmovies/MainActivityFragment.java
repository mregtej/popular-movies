package com.udacity.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.udacity.popularmovies.model.PopularFilm;
import com.udacity.popularmovies.model.PopularFilmsAdapter;

import java.util.Arrays;

public class MainActivityFragment extends Fragment {

    private PopularFilmsAdapter popularFilmAdapter;

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

    public MainActivityFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        popularFilmAdapter = new PopularFilmsAdapter(getActivity(), Arrays.asList(popularFilms));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gv_popular_films);
        gridView.setAdapter(popularFilmAdapter);

        return rootView;
    }
}
