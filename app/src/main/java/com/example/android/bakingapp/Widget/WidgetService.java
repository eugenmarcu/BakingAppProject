package com.example.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.example.android.bakingapp.Database.AppDatabase;
import com.example.android.bakingapp.Database.RecipeEntry;
import com.example.android.bakingapp.R;

import java.util.List;

/**
 * Widget service and RemoteViewFactory that will create views for the widget
 */
public class WidgetService extends RemoteViewsService {
    private List<RecipeEntry> ingredientList;
    String recipeID;
    AppDatabase mDb;
    private static final String RECIPE_KEY = "recipe";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingRemoteViewFactory(this.getApplicationContext(), intent);
    }

    public void createIngredientList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ingredientsListSP = sharedPreferences.getString(RECIPE_KEY, "");
        recipeID = ingredientsListSP;
        mDb = AppDatabase.getInstance(getApplicationContext());
        List<RecipeEntry> recipes = mDb.recipeDao().loadRecipeById(recipeID);
        ingredientList = recipes;

    }

    public String getIngredientString(int position) {
        return ingredientList.get(position).getQuantity() + " "
                + ingredientList.get(position).getMeasure() + " "
                + ingredientList.get(position).getIngredient();
    }



    private class BakingRemoteViewFactory implements RemoteViewsFactory {
        Context mContext;

        public BakingRemoteViewFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            createIngredientList();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (ingredientList == null) return 0;
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(),
                    R.layout.ingredient_item_widget);
            views.setTextViewText(R.id.widget_item, getIngredientString(position));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
