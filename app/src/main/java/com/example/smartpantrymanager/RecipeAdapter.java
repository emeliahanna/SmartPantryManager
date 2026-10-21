package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    //variables
    private final ArrayList<Recipe> recipes;
    private final OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(ArrayList<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.textRecipeName.setText(recipe.getName());
        holder.textRecipeDescription.setText(recipe.getDescription());
        holder.textRecipeTime.setText("Cooking time: " + recipe.getCookingTime() + " minutes");
        holder.itemView.setOnClickListener(v -> {
            listener.onRecipeClick(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView textRecipeName;
        TextView textRecipeDescription;
        TextView textRecipeTime;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeDescription = itemView.findViewById(R.id.textRecipeDescription);
            textRecipeTime = itemView.findViewById(R.id.textRecipeTime);
        }
    }
}
