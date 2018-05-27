package com.udacity.pmovies.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.pmovies.R;
import com.udacity.pmovies.globals.GlobalsPopularMovies;
import com.udacity.pmovies.ui.utils.SharedPrefsUtils;

/**
 * PMovies MainActivity
 */
public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------------------------------------|
    //                               Constants                                        |
    //--------------------------------------------------------------------------------|

    /** Class name - Log TAG */
    private final static String TAG = MainActivity.class.getName();
    /** Key for storing the checked state of Most Popular MenuItem on SP */
    private final static String MOST_POPULAR_STATE_MENU_ITEM_KEY = "most-popular-state";
    /** Key for storing the checked state of Top Rated MenuItem on SP */
    private final static String TOP_RATED_STATE_MENU_ITEM_KEY = "top-rated-state";
    /** Key for storing the checked state of Favorites MenuItem on SP */
    private final static String FAVORITES_STATE_MENU_ITEM_KEY = "favorites-state";


    //--------------------------------------------------------------------------------|
    //                                  Params                                        |
    //--------------------------------------------------------------------------------|

    /** Most Popular MenuItem */
    private MenuItem mMostPopularMenuItem;
    /** Top Rated MenuItem */
    private MenuItem mTopRatedMenuItem;
    /** Favorites MenuItem */
    private MenuItem mFavoriteFilmsMenuItem;


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        loadMenuItemCheckedStatesFromSP(menu);
        Log.d(TAG, "onCreateOptionsMenu()");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuSortByMostPopular:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPrefsUtils.saveInSp(this, MOST_POPULAR_STATE_MENU_ITEM_KEY,
                            true);
                    SharedPrefsUtils.saveInSp(this, TOP_RATED_STATE_MENU_ITEM_KEY,
                            false);
                    SharedPrefsUtils.saveInSp(this, FAVORITES_STATE_MENU_ITEM_KEY,
                            false);
                    updateMainActivityFragment(GlobalsPopularMovies.MOST_POPULAR_PANEL_VIEW);
                }
                return true;
            case R.id.menuSortByTopRated:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPrefsUtils.saveInSp(this, MOST_POPULAR_STATE_MENU_ITEM_KEY,
                            false);
                    SharedPrefsUtils.saveInSp(this, TOP_RATED_STATE_MENU_ITEM_KEY,
                            true);
                    SharedPrefsUtils.saveInSp(this, FAVORITES_STATE_MENU_ITEM_KEY,
                            false);
                    updateMainActivityFragment(GlobalsPopularMovies.TOP_RATED_PANEL_VIEW);
                }
                return true;
            case R.id.menuSortByFavorites:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    SharedPrefsUtils.saveInSp(this, MOST_POPULAR_STATE_MENU_ITEM_KEY,
                            false);
                    SharedPrefsUtils.saveInSp(this, TOP_RATED_STATE_MENU_ITEM_KEY,
                            false);
                    SharedPrefsUtils.saveInSp(this, FAVORITES_STATE_MENU_ITEM_KEY,
                            true);
                    updateMainActivityFragment(GlobalsPopularMovies.FAVORITE_FILMS_PANEL_VIEW);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Updates the MainActivityFragment view (GridView) depending on the user selection
     *
     * @param panel_selection   Panel sorting
     */
    private void updateMainActivityFragment(int panel_selection) {
        MainActivityFragment mainActivityFragment = (MainActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fr_main);
        if(mainActivityFragment != null) {
            mainActivityFragment.updateAdapter(panel_selection);
        }
    }

    /**
     * Reads the checked states of MenuItems from SharedPreferences
     *
     * @param menu  App menu
     */
    private void loadMenuItemCheckedStatesFromSP(@NonNull Menu menu) {
        mMostPopularMenuItem = menu.findItem(R.id.menuSortByMostPopular);
        mTopRatedMenuItem = menu.findItem(R.id.menuSortByTopRated);
        mFavoriteFilmsMenuItem = menu.findItem(R.id.menuSortByFavorites);
        boolean mMostPopularMenuItemChecked = SharedPrefsUtils.getFromSP(this,
                MOST_POPULAR_STATE_MENU_ITEM_KEY, true);
        boolean mTopRatedMenuItemChecked = SharedPrefsUtils.getFromSP(this,
                TOP_RATED_STATE_MENU_ITEM_KEY, false);
        boolean mFavoritesMenuItemChecked = SharedPrefsUtils.getFromSP(this,
                FAVORITES_STATE_MENU_ITEM_KEY, false);
        if(mMostPopularMenuItemChecked) { mMostPopularMenuItem.setChecked(true); }
        else if(mTopRatedMenuItemChecked) { mTopRatedMenuItem.setChecked(true); }
        else if(mFavoritesMenuItemChecked) { mFavoriteFilmsMenuItem.setChecked(true); }
    }

}
