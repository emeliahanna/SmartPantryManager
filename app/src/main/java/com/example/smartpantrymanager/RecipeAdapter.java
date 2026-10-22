package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//manage recipe cards displayed in the RecyclerView
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    //store recipes and handle recipe selection
    private final ArrayList<Recipe> recipes;
    private final OnRecipeClickListener listener;

    //interface for handling clicks on recipe cards
    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    //receive recipe list and click listener
    public RecipeAdapter(ArrayList<Recipe> recipes, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    //create layout for each recipe card
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    //display recipe details in the correct card
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        //show recipe name, description and cooking time
        holder.textRecipeName.setText(recipe.getName());
        holder.textRecipeDescription.setText(recipe.getDescription());
        holder.textRecipeTime.setText("Cooking time: " + recipe.getCookingTime() + " minutes");

        //open selected recipe through click listener
        holder.itemView.setOnClickListener(v -> {
            listener.onRecipeClick(recipe);
        });
    }

    @Override
    //return number of recipes available
    public int getItemCount() {
        return recipes.size();
    }

    //hold references to views inside each recipe card
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView textRecipeName;
        TextView textRecipeDescription;
        TextView textRecipeTime;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            //connect text views to layout elements
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeDescription = itemView.findViewById(R.id.textRecipeDescription);
            textRecipeTime = itemView.findViewById(R.id.textRecipeTime);
        }
    }
}