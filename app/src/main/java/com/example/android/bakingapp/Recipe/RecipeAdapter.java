package com.example.android.bakingapp.Recipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingapp.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public List<Recipe> mRecipeList;
    private Context mContext;

    // data is passed into the constructor
    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecipeList = recipes;
        mContext = context;
    }

    public void addAll(List<Recipe> recipes) {
        mRecipeList.addAll(recipes);
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeList.get(position);

        holder.recipeNameTV.setText(currentRecipe.getName());
        if (!currentRecipe.getImageURL().isEmpty())
            Glide.with(mContext).load(currentRecipe.getImageURL()).into(holder.recipeImageView);
        else
            holder.recipeImageView.setImageResource(R.drawable.recipe_image);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView recipeImageView;
        TextView recipeNameTV;

        ViewHolder(View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipe_item_image);
            recipeNameTV = itemView.findViewById(R.id.recipe_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Recipe getItem(int id) {
        return mRecipeList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}