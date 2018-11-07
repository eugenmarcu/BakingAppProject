package com.example.android.bakingapp.Widget.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.example.android.bakingapp.Database.AppDatabase;
import com.example.android.bakingapp.Database.RecipeEntry;

import java.util.List;

public class WidgetViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = WidgetViewModel.class.getSimpleName();

    private LiveData<List<RecipeEntry>> recipes;

    public WidgetViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the movies from the DataBase");
        recipes = database.recipeDao().loadRecipeNamesById("RCP_ID");
    }

    public LiveData<List<RecipeEntry>> getRecipe() {
        return recipes;
    }

}
