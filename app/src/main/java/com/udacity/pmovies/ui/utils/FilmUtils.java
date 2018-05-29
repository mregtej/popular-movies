package com.udacity.pmovies.ui.utils;

import com.udacity.pmovies.database_model.FavFilm;
import com.udacity.pmovies.tmdb_model.TMDBFilm;

import java.util.ArrayList;
import java.util.List;

public class FilmUtils {

    public static ArrayList<TMDBFilm> convertArrayOfFavFilmsToArrayOfTMDBFilms(List<FavFilm> favFilmList) {
        ArrayList<TMDBFilm> tmdbFilmList = new ArrayList<TMDBFilm>(favFilmList.size());
        for(FavFilm favFilm : favFilmList) {
            tmdbFilmList.add(new TMDBFilm(
                    favFilm.getPosterPath(),
                    favFilm.isAdult(),
                    favFilm.getOverview(),
                    favFilm.getReleaseDate(),
                    favFilm.getId(),
                    favFilm.getOriginalTitle(),
                    favFilm.getOriginalLanguage(),
                    favFilm.getTitle(),
                    favFilm.getBackdropPath(),
                    favFilm.getPopularity(),
                    favFilm.getVoteCount(),
                    favFilm.getVideo(),
                    favFilm.getVoteAverage(),
                    TextUtils.convertCsvStringToListOfInt(favFilm.getGenreIds())
            ));
        }
        return tmdbFilmList;
    }
}
