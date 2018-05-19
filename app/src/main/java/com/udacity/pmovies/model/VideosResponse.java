package com.udacity.pmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideosResponse implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private ArrayList<Video> results;

    private VideosResponse(Parcel in){
        this.id = in.readInt();
        results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideosResponse> CREATOR = new Creator<VideosResponse>() {
        @Override
        public VideosResponse createFromParcel(Parcel in) {
            return new VideosResponse(in);
        }

        @Override
        public VideosResponse[] newArray(int size) {
            return new VideosResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(results);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
