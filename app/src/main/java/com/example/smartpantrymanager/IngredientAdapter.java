
package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final ArrayList<Ingredient> ingredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.textIngredientName.setText(ingredient.getName());
        holder.textIngredientQuantity.setText(
                "Quantity: " + ingredient.getQuantity() + " " + ingredient.getUnit());

        String expiry = ingredient.getExpiryDate();

        if (expiry == null || expiry.trim().isEmpty()) {
            expiry = "Not specified";
        }

        holder.textIngredientExpiry.setText("Expiry: " + expiry);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView textIngredientName;
        TextView textIngredientQuantity;
        TextView textIngredientExpiry;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            textIngredientName = itemView.findViewById(R.id.textIngredientName);

            textIngredientQuantity = itemView.findViewById(R.id.textIngredientQuantity);

            textIngredientExpiry = itemView.findViewById(R.id.textIngredientExpiry);
        }
    }
}
