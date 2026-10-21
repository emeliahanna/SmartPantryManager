
package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final ArrayList<Ingredient> ingredients;


    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    private final OnIngredientClickListener listener;

    public IngredientAdapter(ArrayList<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
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

        holder.itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient));
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext()).setTitle("Delete Ingredient").setMessage("Delete " + ingredient.getName() + "?").setPositiveButton("Delete", (dialog, which) -> {
                PantryDatabaseHelper databaseHelper = new PantryDatabaseHelper(v.getContext());
                int rowsDeleted = databaseHelper.deleteIngredient(ingredient.getId());
                databaseHelper.close();

                if (rowsDeleted > 0) {
                    ingredients.remove(ingredient);
                    notifyDataSetChanged();

                    Toast.makeText(v.getContext(),"Ingredient deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Could not delete ingredient", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", null).show();
            return true;
        });

        holder.textIngredientName.setText(ingredient.getName());
        holder.textIngredientQuantity.setText("Quantity: " + ingredient.getQuantity() + " " + ingredient.getUnit());

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
