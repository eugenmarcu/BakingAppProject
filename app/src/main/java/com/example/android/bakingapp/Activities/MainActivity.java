package com.example.android.bakingapp.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.AppExecutors;
import com.example.android.bakingapp.Database.AppDatabase;
import com.example.android.bakingapp.Database.RecipeEntry;
import com.example.android.bakingapp.Fragments.MainFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Ingredient.Ingredient;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.RecipeLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    public final String REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";//"http://go.udacity.com/android-baking-app-json";
    private List<Recipe> mRecipeList;
    @BindView(R.id.recipe_list_empty_view)
    TextView emptyView;
    private LoaderManager mLoaderManager;
    private static final int RECIPE_LOADER_ID = 101;
    private static final String RECIPE_KEY = "recipe";
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());
        mLoaderManager = getLoaderManager();

        if (!isInternetConnection()) {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_internet_connection);
        } else {
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            mLoaderManager.initLoader(RECIPE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new RecipeLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        mRecipeList = (List<Recipe>) o;
        saveToDatabase(mRecipeList);

        if (mRecipeList != null && mRecipeList.size() > 0) {
            //Creating  and populate the fragment that will display the recipe list.
            MainFragment mainFragment = new MainFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(RECIPE_KEY, (ArrayList<Recipe>) mRecipeList);
            mainFragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipes_frame, mainFragment)
                    .commit();
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_recipes_found);
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private boolean isInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void saveToDatabase(List<Recipe> recipeList) {
        if (recipeList != null) {
            try {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.recipeDao().deleteAllRecipes();
                    }
                });
                for (Recipe recipe : recipeList) {
                    //Insert recipe name
                    final RecipeEntry recipeNamesEntry = new RecipeEntry("RCP_ID", "", "", recipe.getName());
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.recipeDao().insertRecipe(recipeNamesEntry);
                        }
                    });

                    //Insert recipe ingredients
                    List<Ingredient> ingredientList = recipe.getIngredients();
                    for (Ingredient ingredient : ingredientList) {
                        final RecipeEntry recipeEntry = new RecipeEntry(recipe.getName(), ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient());
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.recipeDao().insertRecipe(recipeEntry);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Log.e("SaveToDatabase", e.getMessage());
            }
        }

    }

}
