package com.udacity.pmovies.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.pmovies.application.PMoviesExecutors;
import com.udacity.pmovies.database_model.FavFilm;
import com.udacity.pmovies.database_model.FavoriteMoviesDao;
import com.udacity.pmovies.database_model.FavoriteMoviesRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesContentProvider extends ContentProvider {

    /** The match code for all dir in the FavFilm table. */
    private static final int CODE_FAV_FILM_DIR = 1;

    /** The URI for the FavMovies table. */
    public static final Uri URI_FAV_MOVIES = Uri.parse(
            "content://" + FavoriteMoviesContract.AUTHORITY + "/"
                    + FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME);

    /** The match code for an item in the FavFilm table. */
    private static final int CODE_FAV_FILM_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(FavoriteMoviesContract.AUTHORITY,
                FavoriteMoviesContract.FAV_FILM_PATH, CODE_FAV_FILM_DIR);
        MATCHER.addURI(FavoriteMoviesContract.AUTHORITY,
                FavoriteMoviesContract.FAV_FILM_PATH + "/#", CODE_FAV_FILM_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_FAV_FILM_DIR || code == CODE_FAV_FILM_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            FavoriteMoviesDao favMovies = FavoriteMoviesRoomDatabase.getInstance(context,
                    new PMoviesExecutors()).favoriteMoviesDao();
            final Cursor cursor;
            if (code == CODE_FAV_FILM_DIR) {
                cursor = favMovies.getFavoriteMoviesViaCP();
            } else {
                cursor = favMovies.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_FILM_DIR:
                return "vnd.android.cursor.dir/" + FavoriteMoviesContract.AUTHORITY + "." +
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;
            case CODE_FAV_FILM_ITEM:
                return "vnd.android.cursor.item/" + FavoriteMoviesContract.AUTHORITY + "." +
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_FILM_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = FavoriteMoviesRoomDatabase.getInstance(context, new PMoviesExecutors()).
                        favoriteMoviesDao().insert(FavFilm.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_FAV_FILM_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_FILM_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAV_FILM_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = FavoriteMoviesRoomDatabase.getInstance(context, new PMoviesExecutors()).
                        favoriteMoviesDao().deleteByIdViaCP(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_FILM_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_FAV_FILM_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final FavFilm favFilm = FavFilm.fromContentValues(values);
                favFilm.setId((int) ContentUris.parseId(uri));
                final int count = FavoriteMoviesRoomDatabase.getInstance(context, new PMoviesExecutors()).
                        favoriteMoviesDao().update(favFilm);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final FavoriteMoviesRoomDatabase database = FavoriteMoviesRoomDatabase.getInstance(context,
                new PMoviesExecutors());
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (MATCHER.match(uri)) {
            case CODE_FAV_FILM_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final FavoriteMoviesRoomDatabase database = FavoriteMoviesRoomDatabase.getInstance(context,
                        new PMoviesExecutors());
                final List<FavFilm> favFilms = new ArrayList<>(values.length);
                for (int i = 0; i < values.length; i++) {
                    favFilms.add(FavFilm.fromContentValues(values[i]));
                }
                return database.favoriteMoviesDao().insertAll(favFilms).length;
            case CODE_FAV_FILM_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}
