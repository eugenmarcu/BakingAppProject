package com.example.android.bakingapp.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.Window;
import android.view.WindowManager;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.Step.Step;
import com.example.android.bakingapp.Fragments.StepFragment;

public class StepActivity extends AppCompatActivity {

    Step currentStep;
    Recipe currentRecipe;

    private static final String STEP_KEY = "step";
    private static final String RECIPE_KEY = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if the device is in landscape mode video will play on fullscreen
        if (getResources().getBoolean(R.bool.isLandscape) && !getResources().getBoolean(R.bool.isTablet)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_step);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                currentRecipe = bundle.getParcelable(RECIPE_KEY);
                currentStep = bundle.getParcelable(STEP_KEY);
                StepFragment stepFragment = new StepFragment();
                Bundle args = new Bundle();
                args.putParcelable(RECIPE_KEY, currentRecipe);
                args.putParcelable(STEP_KEY, currentStep);
                stepFragment.setArguments(args);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
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
