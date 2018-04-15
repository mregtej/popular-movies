package com.udacity.popularmovies.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmovies.R;

import java.util.List;

/**
 * Popular Films Custom ArrayAdapter
 */
public class PopularFilmsAdapter extends ArrayAdapter<PopularFilm> {

    /** Log TAG - Class Name */
    private static final String TAG = PopularFilmsAdapter.class.getSimpleName();
    /** Number of films */
    private int mNumberOfFilms;

    /**
     * Custom PopularFilms ArrayAdapterConstructor.
     *
     * @param context           Activity context for inflating the layout file
     * @param popularFilmList   List of PopularFilm objects to display in a list.
     */
    public PopularFilmsAdapter(Activity context, List<PopularFilm> popularFilmList) {
        super(context,0, popularFilmList);
        mNumberOfFilms = popularFilmList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PopularFilm popularFilm = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.film_item,
                    parent,
                    false);
        }

        ImageView filmPosterView = (ImageView) convertView.findViewById(R.id.iv_film_poster);
        filmPosterView.setImageResource(popularFilm.filmPoster);

        TextView versionNameView = (TextView) convertView.findViewById(R.id.tv_film_title);
        versionNameView.setText(popularFilm.filmTitle);

        return convertView;
    }

    @Override
    public int getCount() {
        return mNumberOfFilms;
    }
}
