package com.example.android.bakingapp.Recipe.Ingredient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public List<Ingredient> mIngredientList;
    private Context mContext;

    // data is passed into the constructor
    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.mInflater = LayoutInflater.from(context);
        this.mIngredientList = ingredients;
        mContext = context;
    }

    public void addAll(List<Ingredient> ingredients) {
        mIngredientList.addAll(ingredients);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient currentIngredient = mIngredientList.get(position);

        holder.quantity.setText(currentIngredient.getQuantity());
        holder.measure.setText(currentIngredient.getMeasure());
        holder.name.setText(currentIngredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    // convenience method for getting data at click position
    public Ingredient getItem(int id) {
        return mIngredientList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onIngredientItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView quantity;
        TextView measure;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.ingredient_item_quantity);
            measure = itemView.findViewById(R.id.ingredient_item_measure);
            name = itemView.findViewById(R.id.ingredient_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onIngredientItemClick(view, getAdapterPosition());
        }
    }


}



















