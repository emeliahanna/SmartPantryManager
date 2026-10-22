
package com.example.smartpantrymanager;
//imports
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
    private final ArrayList<Ingredient> ingredients; //list pf pantry ingredients

    public interface OnIngredientClickListener { //interface to handle ingredient selection
        void onIngredientClick(Ingredient ingredient);
    }
    private final OnIngredientClickListener listener; //click listener

    //receive ingredient list and click listener
    public IngredientAdapter(ArrayList<Ingredient> ingredients, OnIngredientClickListener listener) {
        this.ingredients = ingredients;
        this.listener = listener;
    }

    @NonNull
    @Override //layout for each ingredient card
    public IngredientViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override //display ingredient details and handle user interactions
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient)); //open details when clicked
        holder.itemView.setOnLongClickListener(v -> { //show confirmation dialog when is long pressed
            new AlertDialog.Builder(v.getContext()).setTitle("Delete Ingredient").setMessage("Delete " + ingredient.getName() + "?").setPositiveButton("Delete", (dialog, which) -> {
                PantryDatabaseHelper databaseHelper = new PantryDatabaseHelper(v.getContext()); //delete ingredient from db
                int rowsDeleted = databaseHelper.deleteIngredient(ingredient.getId());
                databaseHelper.close();

                if (rowsDeleted > 0) { //update list when deletion succeeds
                    ingredients.remove(ingredient);
                    notifyDataSetChanged();

                    //confirm successful deletion
                    Toast.makeText(v.getContext(),"Ingredient deleted", Toast.LENGTH_SHORT).show();
                } else { //error if deletion fails
                    Toast.makeText(v.getContext(), "Could not delete ingredient", Toast.LENGTH_SHORT).show();
                }
            }).setNegativeButton("Cancel", null).show();
            return true;
        });
        //display ingredient name and quantity
        holder.textIngredientName.setText(ingredient.getName());
        holder.textIngredientQuantity.setText("Quantity: " + ingredient.getQuantity() + " " + ingredient.getUnit());

        String expiry = ingredient.getExpiryDate(); //return expiry date

        if (expiry == null || expiry.trim().isEmpty()) { //display fallback text when expiry date missing
            expiry = "Not specified";
        }

        holder.textIngredientExpiry.setText("Expiry: " + expiry); //display expiry date
    }

    @Override //return number of ingredients in list
    public int getItemCount() {
        return ingredients.size();
    }

    //hold references to views inside ingredient card
    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView textIngredientName;
        TextView textIngredientQuantity;
        TextView textIngredientExpiry;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            //connect java to xml layout
            textIngredientName = itemView.findViewById(R.id.textIngredientName);
            textIngredientQuantity = itemView.findViewById(R.id.textIngredientQuantity);
            textIngredientExpiry = itemView.findViewById(R.id.textIngredientExpiry);
        }
    }
}
