package com.udacity.pmovies.ui;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.pmovies.R;
import com.udacity.pmovies.adapters.TrailerAdapter;
import com.udacity.pmovies.tmdb_model.Video;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFilmActivityTrailersFragment extends Fragment
        implements TrailerAdapter.OnPlayItemClickListener {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivityTrailersFragment.class.getName();
    /** Youtube base URL for video watching */
    private static final String YOUTUBE_WATCH_BASE_URL = "http://www.youtube.com/watch?v=";
    /** Key for storing the list state */
    private static final String LIST_STATE_KEY = "list-state";

    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** DetailFilmActivity context */
    private Context mContext;
    /** Film Trailers Custom ArrayAdapter */
    private TrailerAdapter mTrailerAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** List state */
    private Parcelable mListState;

    /** Film Trailer RecyclerView (Carousel) */
    @BindView(R.id.rv_film_trailers_carousel_detail_view) RecyclerView mTrailerRecyclerView;


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate Views
        View rootView = inflater.inflate(R.layout.fragment_detail_film_trailers,
                container, false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Load & set GridLayout
        setTrailerRecyclerViewLayoutManager(rootView);

        // Load & set ArrayAdapter
        mTrailerAdapter = new TrailerAdapter(this);

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
     * Set Trailer's RecyclerView Layout
     *
     * @param   rootView    GridView
     */
    private void setTrailerRecyclerViewLayoutManager(View rootView) {
        layoutManager = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Set-up and load RecyclerView's ArrayAdapter
     *
     * @param   filmTrailers    List of film-trailers retrieved from TMDB
     */
    public void updateAdapter(ArrayList<Video> filmTrailers) {
        mTrailerAdapter.setmTrailerList(filmTrailers);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mTrailerAdapter.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------|
    //                      Interface Methods (Adapter comm)                          |
    //--------------------------------------------------------------------------------|

    /**
     * Handles the Play click
     *
     * @param   id      Video id (Youtube)
     */
    @Override
    public void onItemClick(String id) {
        watchYoutubeVideo(id);
    }

    //--------------------------------------------------------------------------------|
    //                               Support Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Opens Youtube App (or web-browser) for displaying the trailer
     *
     * @param   id    Video id (Youtube)
     */
    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_WATCH_BASE_URL + id));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }

}
