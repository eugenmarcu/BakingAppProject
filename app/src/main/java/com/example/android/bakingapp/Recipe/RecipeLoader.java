package com.example.android.bakingapp.Recipe;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.bakingapp.QueryUtils;

import java.util.List;

/**
 * Loads a list of recipes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = RecipeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link RecipeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public RecipeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Recipe> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of recipes.
        List<Recipe> recipesList = QueryUtils.fetchRecipeData(mUrl);
        return recipesList;
    }

}
