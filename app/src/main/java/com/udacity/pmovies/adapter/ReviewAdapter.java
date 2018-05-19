package com.udacity.pmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.pmovies.R;
import com.udacity.pmovies.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Log TAG - Class Name */
    private static final String TAG = ReviewAdapter.class.getSimpleName();


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** List of reviews - Model data ArrayList<Review> */
    private ArrayList<Review> mReviewList;
    /** Activity Context */
    private Context mContext;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty Constructor
     */
    public ReviewAdapter() {
        mReviewList = new ArrayList<>();
    }


    //--------------------------------------------------------------------------------|
    //                               Getters/Setters                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Sets the array of film-reviews
     *
     * @param mReviewList   array of film-reviews
     */
    public void setmReviewList(ArrayList<Review> mReviewList) {
        this.mReviewList = mReviewList;
    }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_review_layout, parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        if(mReviewList != null) {
            // Retrieve data from Model object
            Review review = mReviewList.get(position);

            // Set position-tag
            holder.reviewLayout.setTag(position);

            // Populate UI elements
            populateUIView(holder, review);
        }
    }

    @Override
    public int getItemCount() { return mReviewList.size(); }


    //--------------------------------------------------------------------------------|
    //                               ViewHolder                                       |
    //--------------------------------------------------------------------------------|

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_film_review_author) TextView filmReviewAuthor;
        @BindView(R.id.tv_film_review_content) TextView filmReviewContent;
        private final View reviewLayout;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewLayout = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    //--------------------------------------------------------------------------------|
    //                                UI Methods                                      |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder    ViewHolder (View container)
     * @param review    Review parcelable objetc
     */
    private void populateUIView(ReviewViewHolder holder, final Review review) {
        holder.filmReviewAuthor.setText(review.getAuthor());
        holder.filmReviewContent.setText(review.getContent());
    }

}
