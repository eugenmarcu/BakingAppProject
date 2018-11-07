package com.example.android.bakingapp.Recipe.Step;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public List<Step> mStepList;
    private Context mContext;

    // data is passed into the constructor
    public StepAdapter(Context context, List<Step> steps) {
        this.mInflater = LayoutInflater.from(context);
        this.mStepList = steps;
        mContext = context;
    }

    public void addAll(List<Step> steps) {
        mStepList.addAll(steps);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step  currentStep = mStepList.get(position);

        //holder.number.setText(currentStep.getId());
        holder.description.setText(currentStep.getDescription());
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    // convenience method for getting data at click position
    public Step getItem(int id) {
        return mStepList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onStepItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView number;
        TextView description;

        ViewHolder(View itemView) {
            super(itemView);
            //number = itemView.findViewById(R.id.step_item_number);
            description = itemView.findViewById(R.id.step_item_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onStepItemClick(view, getAdapterPosition());
        }
    }


}
