package com.udacity.pmovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.pmovies.R;
import com.udacity.pmovies.adapters.ReviewAdapter;
import com.udacity.pmovies.tmdb_model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFilmActivityReviewsFragment extends Fragment {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private static final String TAG = DetailFilmActivityReviewsFragment.class.getName();

    /** Key for storing the list state */
    private static final String LIST_STATE_KEY = "list-state";
    /** Key for storing the Adapter data */
    private static final String REVIEW_ADAPTER_KEY = "review-adapter";

    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** DetailFilmActivity context */
    private Context mContext;
    /** TMDBFilm Reviews Custom ArrayAdapter */
    private ReviewAdapter mReviewAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** List state */
    private Parcelable mListState;

    /** TMDBFilm Reviews RecyclerView (Carousel) */
    @BindView(R.id.rv_film_reviews_detail_view) RecyclerView mReviewsRecyclerView;


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate Views
        View rootView = inflater.inflate(R.layout.fragment_detail_film_reviews,
                container, false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Load & set GridLayout
        setReviewRecyclerViewLayoutManager(rootView);

        // Load & set ArrayAdapter
        mReviewAdapter = new ReviewAdapter();
        if(savedInstanceState != null) {
            updateAdapter(savedInstanceState.getParcelableArrayList(REVIEW_ADAPTER_KEY));
        }

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
        savedInstanceState.putParcelableArrayList(REVIEW_ADAPTER_KEY,
                mReviewAdapter.getmReviewList());
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
     * Set Review's RecyclerView Layout
     *
     * @param   rootView    GridView
     */
    private void setReviewRecyclerViewLayoutManager(View rootView) {
        layoutManager = new LinearLayoutManager(
                rootView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Set-up and load RecyclerView's ArrayAdapter
     *
     * @param   filmReviews    List of film-reviews retrieved from TMDB
     */
    public void updateAdapter(ArrayList<Review> filmReviews) {
        mReviewAdapter.setmReviewList(filmReviews);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
        mReviewAdapter.notifyDataSetChanged();
    }

}
