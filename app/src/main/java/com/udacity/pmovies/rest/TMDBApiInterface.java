package com.udacity.pmovies.rest;

import com.udacity.pmovies.tmdb_model.APIConfigurationResponse;
import com.udacity.pmovies.tmdb_model.FilmResponse;
import com.udacity.pmovies.tmdb_model.GenresResponse;
import com.udacity.pmovies.tmdb_model.ReviewsResponse;
import com.udacity.pmovies.tmdb_model.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApiInterface {
    @GET("configuration")
    Call<APIConfigurationResponse> getConfiguration(@Query("api_key") String apiKey);

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<FilmResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<FilmResponse> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<FilmResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
