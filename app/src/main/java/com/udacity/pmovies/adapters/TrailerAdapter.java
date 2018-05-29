package com.udacity.pmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pmovies.R;
import com.udacity.pmovies.tmdb_model.Video;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView.Adapter for populating the carousel of movie trailers.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Log TAG - Class Name */
    private static final String TAG = TrailerAdapter.class.getSimpleName();


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** List of trailers - Model data List<Video> */
    private ArrayList<Video> mTrailerList;
    /** Play listener */
    private OnPlayItemClickListener mPlayListener;
    /** Activity Context */
    private Context mContext;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty Constructor
     */
    public TrailerAdapter() {
        mPlayListener = null;
    }

    /**
     * Constructor which sets the trailer click-listener
     * (comm with DetailMainActivityTrailersFragment)
     *
     * @param listener  Trailer click-listener
     */
    public TrailerAdapter(OnPlayItemClickListener listener) {
        mPlayListener = listener;
    }

    /**
     * Constructor which sets the array of trailers and the trailer click-listener
     * (comm with DetailMainActivityTrailersFragment)
     *
     * @param trailers  Array of trailers
     * @param listener  Trailer click-listener
     */
    public TrailerAdapter(ArrayList<Video> trailers, OnPlayItemClickListener listener) {
        mTrailerList = trailers;
        mPlayListener = listener;
    }


    //--------------------------------------------------------------------------------|
    //                               Getters/Setters                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Sets the array of film-trailers
     *
     * @param mTrailerList   Array of film-trailers
     */
    public void setmTrailerList(ArrayList<Video> mTrailerList) {
        this.mTrailerList = mTrailerList;
    }

    /**
     * Gets the array of film-trailers
     */
    public ArrayList<Video> getmTrailerList() { return mTrailerList; }

    /**
     * Sets the trailer click-listener (comm with DetailMainActivityTrailersFragment)
     *
     * @param mPlayListener     Trailer click-listener
     */
    public void setmPlayListener(OnPlayItemClickListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_trailer_layout, parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        if(mTrailerList != null) {
            // Retrieve data from Model object
            Video trailer = mTrailerList.get(position);

            // Set position-tag
            holder.trailerLayout.setTag(position);

            // Populate UI elements
            populateUIView(holder, trailer);
        }
    }

    @Override
    public int getItemCount() { return mTrailerList.size(); }


    //--------------------------------------------------------------------------------|
    //                               ViewHolder                                       |
    //--------------------------------------------------------------------------------|

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_film_trailer) ImageView filmTrailer;
        @BindView(R.id.tv_film_trailer_title) TextView filmTrailerTextView;
        private final View trailerLayout;

        public TrailerViewHolder(View v) {
            super(v);
            trailerLayout = v;
            ButterKnife.bind(this, v);
        }
    }


    //--------------------------------------------------------------------------------|
    //                                UI Methods                                      |
    //--------------------------------------------------------------------------------|

    /**
     * Populate UI view elements
     *
     * @param holder    ViewHolder (View container)
     * @param trailer   Trailer parcelable object
     */
    private void populateUIView(TrailerViewHolder holder, final Video trailer) {
        // TODO Choose trailer image - Youtube Android Player API integration?
        Picasso
                .with(mContext)
                .load(R.drawable.im_image_play_trailer)
                .fit()
                .centerCrop()
                .error(R.drawable.im_image_not_available)
                .into(holder.filmTrailer);
        // Set title of film
        holder.filmTrailerTextView.setText(trailer.getName());
        holder.filmTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayListener.onItemClick(trailer.getKey());
            }
        });
    }

    //--------------------------------------------------------------------------------|
    //                    Interface methods (Fragment comm)                           |
    //--------------------------------------------------------------------------------|

    /**
     * Play Listener interface
     */
    public interface OnPlayItemClickListener {
        void onItemClick(String id);
    }

}
