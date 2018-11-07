package com.example.android.bakingapp.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.bakingapp.Fragments.DetailsFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.Step.Step;
import com.example.android.bakingapp.Fragments.StepFragment;

public class DetailsActivity extends AppCompatActivity{

    private static final String RECIPE_KEY = "recipe";
    private static final String STEP_KEY = "step";
    Recipe currentRecipe;
    Step currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the device is in landscape mode video will play on fullscreen
        if (getResources().getBoolean(R.bool.isLandscape) && !getResources().getBoolean(R.bool.isTablet)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentRecipe = bundle.getParcelable(RECIPE_KEY);
        }
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_KEY, currentRecipe);
        detailsFragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.details_frame, detailsFragment)
                .commit();
        if(getResources().getBoolean(R.bool.isTablet)) {
            if (savedInstanceState == null) {
                    currentRecipe = bundle.getParcelable(RECIPE_KEY);
                    currentStep = currentRecipe.getSteps().get(0);
                    StepFragment stepFragment = new StepFragment();
                    Bundle argsStep = new Bundle();
                    argsStep.putParcelable(RECIPE_KEY, currentRecipe);
                    argsStep.putParcelable(STEP_KEY, currentStep);
                    stepFragment.setArguments(argsStep);
                    FragmentManager fragmentManagerStep = getSupportFragmentManager();
                    fragmentManagerStep.beginTransaction()
                            .replace(R.id.step_frame, stepFragment)
                            .commit();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
