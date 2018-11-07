package com.example.android.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Activities.DetailsActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.Recipe.Recipe;
import com.example.android.bakingapp.Recipe.RecipeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment implements RecipeAdapter.ItemClickListener {

    private List<Recipe> mRecipeList;
    @BindView(R.id.rv_list)
    RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private static final String RECIPE_KEY = "recipe";
    private Context mContext;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeList = getArguments().getParcelableArrayList(RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mContext = container.getContext();
        ButterKnife.bind(this, view);

        if (!getResources().getBoolean(R.bool.isTablet))
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new RecipeAdapter(mContext, mRecipeList);
        adapter.setClickListener(MainFragment.this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(mContext, DetailsActivity.class);
        Recipe currentRecipe = adapter.getItem(position);
        intent.putExtra(RECIPE_KEY, currentRecipe);
        startActivity(intent);
    }

}
