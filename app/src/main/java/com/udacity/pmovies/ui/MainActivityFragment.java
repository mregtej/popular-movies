package com.udacity.pmovies.ui;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.pmovies.R;
import com.udacity.pmovies.comms.ConnectivityHandler;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.rest.TMDBApiClient;
import com.udacity.pmovies.rest.TMDBApiInterface;
import com.udacity.pmovies.tmdb_model.Film;
import com.udacity.pmovies.adapters.FilmsAdapter;
import com.udacity.pmovies.ui.utils.SharedPrefsUtils;
import com.udacity.pmovies.ui.widgets.AlertDialogHelper;
import com.udacity.pmovies.view_model.FavoriteMoviesViewModel;
import com.udacity.pmovies.view_model.TMDBViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PMovies MainActivityFragment
 *
 * TODO Implement MVP pattern
 */
public class MainActivityFragment extends Fragment implements FilmsAdapter.OnFilmItemClickListener {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class Name - Log TAG */
    private static final String TAG = MainActivity.class.getName();
    /** Key for storing the list state in savedInstanceState */
    private static final String LIST_STATE_KEY = "list-state";
    /** Key for storing the in savedInstanceState */
    private static final String PANEL_VIEW_KEY = "panel-view";

    private final static String MOST_POPULAR_STATE_MENU_ITEM_KEY = "most-popular-state";
    private final static String TOP_RATED_STATE_MENU_ITEM_KEY = "top-rated-state";
    private final static String FAVORITES_STATE_MENU_ITEM_KEY = "favorites-state";

    /** Key for retrieving Film parcelable object from intent */
    private static final String FILM_EXTRA = "film";
    /** Key for identifying if film is in favorite film list */
    private static final String IS_IN_FAVS_EXTRA = "is-in-favs";


    //--------------------------------------------------------------------------------|
    //                               Params                                           |
    //--------------------------------------------------------------------------------|

    /** Popular films Custom ArrayAdapter */
    private FilmsAdapter mFilmAdapter;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager layoutManager;
    /** Custom GridView (RecyclerView) */
    @BindView(R.id.rv_films_grid) RecyclerView recyclerView;
    /** Activity Context */
    private Context mContext;
    /** List state stored in savedInstanceState */
    private Parcelable mListState;

    /**  film list */
    private List<Film> mTopRatedFilms;
    /**  film list */
    private List<Film> mMostPopularFilms;
    /** Favorite film list */
    private List<Film> mFavoriteMovies;
    private int mPanelSelection;

    /** FavMovies ViewModel instance */
    private FavoriteMoviesViewModel mFavoriteMoviesViewModel;
    /** TMDB ViewModel instance */
    private TMDBViewModel mTmdbViewModel;
    /** TMDB API client */
    private TMDBApiInterface apiService;


    //--------------------------------------------------------------------------------|
    //                               Constructors                                     |
    //--------------------------------------------------------------------------------|

    /**
     * Empty constructor
     */
    public MainActivityFragment() { }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**************************************************************/
        /*                 FavoriteMoviesViewModel                    */
        /**************************************************************/
        // Create FavoriteMoviesViewModel Factory for param injection
        FavoriteMoviesViewModel.Factory favMov_factory = new FavoriteMoviesViewModel.Factory(
                getActivity().getApplication());
        // Get instance of FavoriteMoviesViewModel
        mFavoriteMoviesViewModel = ViewModelProviders.of(this, favMov_factory)
                    .get(FavoriteMoviesViewModel.class);

        /**************************************************************/
        /*                        TMDBViewModel                       */
        /**************************************************************/
        // Create TMDB API client
        apiService = TMDBApiClient.getClient().create(TMDBApiInterface.class);
        // Create TMDBViewModel Factory for param injection
        TMDBViewModel.Factory tmdb_factory = new TMDBViewModel.Factory(
                getActivity().getApplication(), apiService, getString(R.string.TMDB_API_KEY));
        // Get instance of TMDBViewModel
        mTmdbViewModel = ViewModelProviders.of(this, tmdb_factory)
                .get(TMDBViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        recyclerView.setHasFixedSize(true);
        // Save Actvity Context
        mContext = rootView.getContext();
        // Load & set GridLayout
        setRecyclerViewLayoutManager(rootView);
        // Load & set ArrayAdapter
        mFilmAdapter = new FilmsAdapter(this);
        // First Initialization - Load film sorting criteria from SharedPreferences
        if(savedInstanceState == null) {
            loadPanelSelectionFromSP();
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check network connectivity
        if(!ConnectivityHandler.checkConnectivity(getActivity())) {
            displayConnectivityAlertDialog();
        } else {
            // Check empty API_KEY
            if (getString(R.string.TMDB_API_KEY).isEmpty()) {
                displayNoApiKeyAlertDialog();
            } else {
                if (mListState != null) {
                    layoutManager.onRestoreInstanceState(mListState);
                }
                // Get API Config - Global access (Singleton pattern)
                mTmdbViewModel.getAPIConfiguration();
                // Get Film Genres - Global access (Singleton pattern)
                mTmdbViewModel.getGenres();
                // Get most popular and top rated movies (LiveData Observer)
                getMostPopularMovies();
                getTopRatedMovies();
                // Subscribe to FavoriteMoviesViewModel for receiving favorite movie list
                subscribeToFavoriteMoviesViewModel();

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save list state
        mListState = layoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(LIST_STATE_KEY, mListState);
        savedInstanceState.putInt(PANEL_VIEW_KEY, mPanelSelection);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        // Retrieve list state and list/item positions
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            mPanelSelection = savedInstanceState.getInt(PANEL_VIEW_KEY);
        }
    }


    //--------------------------------------------------------------------------------|
    //                                  Setters                                       |
    //--------------------------------------------------------------------------------|

    /**
     * Set favorite movie list (retrieved via FavoriteMoviesViewModel)
     *
     * @param mFavMovies    favorite movie list
     */
    public void setmFavMovies(List<Film> mFavMovies) {
        this.mFavoriteMovies = mFavMovies;
    }

    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Display an AlertDialog to warn user that there is no Internet connectivity
     */
    private void displayConnectivityAlertDialog() {
        AlertDialog connectivityDialog = AlertDialogHelper.createMessage(
                mContext,
                this.getResources().getString(R.string.network_failure),
                this.getResources().getString(R.string.network_user_choice),
                this.getResources().getString(R.string.network_user_choice_wifi),
                this.getResources().getString(R.string.network_user_choice_3g),
                this.getResources().getString(R.string.network_user_choice_no),
                true
        );
        connectivityDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                this.getResources().getString(R.string.network_user_choice_wifi),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(
                                android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                });
        connectivityDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                this.getResources().getString(R.string.network_user_choice_3g),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent
                                (android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
                    }
                });
        connectivityDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                this.getResources().getString(R.string.network_user_choice_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
        connectivityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        });
        connectivityDialog.show();
    }

    /**
     * Display an AlertDialog to warn user that no API was found.
     *
     * TODO Allow users to add it manually (EditText + storage in SharedPreferences?
     */
    private void displayNoApiKeyAlertDialog() {
        AlertDialog noApiKeyDialog = AlertDialogHelper.createMessage(
                mContext,
                this.getResources().getString(R.string.no_api_key_failure),
                this.getResources().getString(R.string.no_api_key_user_choice),
                this.getResources().getString(R.string.no_api_key_open_website),
                true
        );
        noApiKeyDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                this.getResources().getString(R.string.no_api_key_open_website),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = "http://www.themoviedb.org/faq/api?language=en";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        getActivity().finish();
                    }
                });
        noApiKeyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        });
        noApiKeyDialog.show();
    }

    /**
     * Set-up and load RecyclerView's ArrayAdapter
     *
     * @param   panel_selection
     */
    public void updateAdapter(int panel_selection) {
        mPanelSelection = panel_selection;
        switch(panel_selection) {
            case GlobalsPopularMovies.MOST_POPULAR_PANEL_VIEW:
                mFilmAdapter.setFilmList(mMostPopularFilms);
                break;
            case GlobalsPopularMovies.TOP_RATED_PANEL_VIEW:
                mFilmAdapter.setFilmList(mTopRatedFilms);
                break;
            case GlobalsPopularMovies.FAVORITE_FILMS_PANEL_VIEW:
                // TODO Add custom
                mFilmAdapter.setFilmList(mFavoriteMovies);
                break;
        }
        recyclerView.setAdapter(mFilmAdapter);
        mFilmAdapter.notifyDataSetChanged();
    }

    /**
     * Loads film sorting criteria (Top Rated, Most Popular or Favorites) from SharedPreferences
     */
    private void loadPanelSelectionFromSP(){
        boolean mMostPopularMenuItemChecked = SharedPrefsUtils.getFromSP(mContext,
                MOST_POPULAR_STATE_MENU_ITEM_KEY, true);
        boolean mTopRatedMenuItemChecked = SharedPrefsUtils.getFromSP(mContext,
                TOP_RATED_STATE_MENU_ITEM_KEY, false);
        boolean mFavoritesMenuItemChecked = SharedPrefsUtils.getFromSP(mContext,
                FAVORITES_STATE_MENU_ITEM_KEY, false);
        if(mMostPopularMenuItemChecked) {
            mPanelSelection = GlobalsPopularMovies.MOST_POPULAR_PANEL_VIEW;
        } else if(mTopRatedMenuItemChecked) {
            mPanelSelection = GlobalsPopularMovies.TOP_RATED_PANEL_VIEW;
        } else if(mFavoritesMenuItemChecked) {
            mPanelSelection = GlobalsPopularMovies.FAVORITE_FILMS_PANEL_VIEW;
        }
    }

    /**
     * Set RecyclerView's LayoutManager.
     * Different layouts for portrait and landscape mode
     *
     * @param   rootView    Activity view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        switch(this.getResources().getConfiguration().orientation) {
            case GlobalsPopularMovies.LANDSCAPE_VIEW: // Landscape Mode
                layoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        GlobalsPopularMovies.GRIDVIEW_LANDSCAPE_NUMBER_OF_COLUMNS);
                break;
            case GlobalsPopularMovies.PORTRAIT_VIEW: // Portrait Mode
            default:
                layoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        GlobalsPopularMovies.GRIDVIEW_PORTRAIT_NUMBER_OF_COLUMNS);
                break;
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    private void subscribeToFavoriteMoviesViewModel() {
        mFavoriteMoviesViewModel.getAllFavMovies().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(@Nullable List<Film> favMovies) {
                if (favMovies == null || favMovies.isEmpty()) {
                    return;
                } else {
                    mFavoriteMovies = favMovies;
                    if(mPanelSelection == GlobalsPopularMovies.FAVORITE_FILMS_PANEL_VIEW) {
                        updateAdapter(mPanelSelection);
                    }
                }
            }
        });
    }

    private void getMostPopularMovies(){
        mTmdbViewModel.getMostPopularMovies().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(@Nullable List<Film> films) {
                if(films == null || films.isEmpty()) { return; }
                else {
                    if(mMostPopularFilms == null || mMostPopularFilms.isEmpty()) {
                        // Initialization phase
                        mMostPopularFilms = films;
                        if(mPanelSelection == GlobalsPopularMovies.MOST_POPULAR_PANEL_VIEW) {
                            updateAdapter(mPanelSelection);
                        }
                    } else {
                        mMostPopularFilms = films;
                    }
                }
            }
        });
    }

    private void getTopRatedMovies(){
        mTmdbViewModel.getTopRatedMovies().observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(@Nullable List<Film> films) {
                if(films == null || films.isEmpty()) { return; }
                else {
                    if(mTopRatedFilms == null || mTopRatedFilms.isEmpty()) {
                        // Initialization phase
                        mTopRatedFilms = films;
                        if(mPanelSelection == GlobalsPopularMovies.TOP_RATED_PANEL_VIEW) {
                            updateAdapter(mPanelSelection);
                        }
                    } else {
                        mTopRatedFilms = films;
                    }
                }
            }
        });
    }

    //--------------------------------------------------------------------------------|
    //                           Adapter --> Fragment comm                            |
    //--------------------------------------------------------------------------------|

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(mContext, DetailFilmActivity.class);
        Film film = mFilmAdapter.getFilm(position);
        if(film != null) {
            i.putExtra(FILM_EXTRA, film);
            i.putExtra(IS_IN_FAVS_EXTRA, isFilmInFavs(film.getId()));
        }
        this.startActivity(i);
    }


    //--------------------------------------------------------------------------------|
    //                                Support Methods                                 |
    //--------------------------------------------------------------------------------|

    /**
     * Checks if current film is already included in favorite film list.
     *
     * @param   film_id     Film ID (uid)
     * @return  true, if film is in favorite film list; false, otherwise
     */
    private boolean isFilmInFavs(int film_id) {
        if(mFavoriteMovies != null) {
            for (Film favMovie : mFavoriteMovies) {
                if (film_id == favMovie.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

}
