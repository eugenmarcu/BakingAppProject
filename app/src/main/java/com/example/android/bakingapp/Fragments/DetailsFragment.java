package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Activities.StepActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Ingredient.Ingredient;
import com.example.android.bakingapp.Recipe.Ingredient.IngredientAdapter;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.Step.Step;
import com.example.android.bakingapp.Recipe.Step.StepAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment implements IngredientAdapter.ItemClickListener, StepAdapter.ItemClickListener {


    @BindView(R.id.ingredients_rv)
    RecyclerView ingredients_rv;
    @BindView(R.id.steps_rv)
    RecyclerView steps_rv;
    @BindView(R.id.recipe_name)
    TextView recipeNameTV;

    List<Ingredient> mIngredientsList;
    List<Step> mStepList;
    IngredientAdapter ingredientAdapter;
    StepAdapter stepAdapter;

    private static final String RECIPE_KEY = "recipe";
    private static final String STEP_KEY = "step";
    Recipe currentRecipe;
    Context mContext;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentRecipe = getArguments().getParcelable(RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mContext = container.getContext();
        ButterKnife.bind(this, view);

        if(savedInstanceState!=null){
            currentRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        recipeNameTV.setText(currentRecipe.getName());

        mIngredientsList = currentRecipe.getIngredients();
        ingredients_rv.setLayoutManager(new LinearLayoutManager(mContext));
        ingredientAdapter = new IngredientAdapter(mContext, mIngredientsList);
        ingredientAdapter.setClickListener(DetailsFragment.this);
        ingredients_rv.setAdapter(ingredientAdapter);

        mStepList = currentRecipe.getSteps();
        steps_rv.setLayoutManager(new LinearLayoutManager(mContext));
        stepAdapter = new StepAdapter(mContext, mStepList);
        stepAdapter.setClickListener(DetailsFragment.this);
        steps_rv.setAdapter(stepAdapter);

        return view;
    }

    @Override
    public void onIngredientItemClick(View view, int position) {

    }

    @Override
    public void onStepItemClick(View view, int position) {
        if(!getResources().getBoolean(R.bool.isTablet)) {
            Intent intent = new Intent(mContext, StepActivity.class);
            Step currentStep = stepAdapter.getItem(position);
            intent.putExtra(STEP_KEY, currentStep);
            intent.putExtra(RECIPE_KEY, currentRecipe);
            startActivity(intent);
        } else {
            Step currentStep = stepAdapter.getItem(position);
            StepFragment stepFragment = new StepFragment();
            Bundle argsStep = new Bundle();
            argsStep.putParcelable(RECIPE_KEY, currentRecipe);
            argsStep.putParcelable(STEP_KEY, currentStep);
            stepFragment.setArguments(argsStep);
            FragmentManager fragmentManagerStep = getFragmentManager();
            fragmentManagerStep.beginTransaction()
                    .replace(R.id.step_frame, stepFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, currentRecipe);
    }


}
