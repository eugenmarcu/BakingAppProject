package com.example.android.bakingapp.Widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.android.bakingapp.Database.AppDatabase;
import com.example.android.bakingapp.Database.RecipeEntry;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Widget.ViewModel.WidgetViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.Widget.BakingAppWidget.updateAppWidget;

/**
 * Activity that will be used to set up the content of the widget
 */
public class WidgetConfigurationActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    List<String> recipesNames;
    @BindView(R.id.recipe_spinner)
    Spinner spinner;
    @BindView(R.id.add_widget_button)
    Button addWidgetButton;
    private String selectedRecipe;
    private int mAppWidgetId;

    private static final String RECIPE_KEY = "recipe";
    private AppDatabase mDb;
    private WidgetViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());
        //Find the widget Id from the Intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        //If the appWidget Id is invalid finish
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        recipesNames = new ArrayList<>();
        recipesNames.add(0, getString(R.string.appwidget_text));
        //Get the recipes names from the Database
        viewModel = ViewModelProviders.of(this).get(WidgetViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<List<RecipeEntry>>() {
            @Override
            public void onChanged(@Nullable List<RecipeEntry> recipeEntries) {
                populateSpinner(recipeEntries);
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            spinner.setPrompt(recipesNames.get(spinner.getSelectedItemPosition()));
        } else {
            selectedRecipe = recipesNames.get(spinner.getSelectedItemPosition());
        }
    }

    private void addWidget(Context context, String recipe) {
        //if the spinner had first line selected that is the prompt line disable the addWidget Button
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.you_choose, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(RECIPE_KEY, recipe);
            editor.apply();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            updateAppWidget(context, appWidgetManager, mAppWidgetId, recipe);
            // Pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_widget_button) {
            addWidget(WidgetConfigurationActivity.this, spinner.getSelectedItem().toString());
        }
    }

    private void populateSpinner(List<RecipeEntry> recipes){
        for (RecipeEntry recipe: recipes){
            //The recipe name is located in the ingredient field
            recipesNames.add(recipe.getIngredient());
        }

        //Populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(WidgetConfigurationActivity.this,
                android.R.layout.simple_spinner_item, recipesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(WidgetConfigurationActivity.this);
        addWidgetButton.setOnClickListener(WidgetConfigurationActivity.this);
    }

}

