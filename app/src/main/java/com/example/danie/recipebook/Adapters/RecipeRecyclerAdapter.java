package com.example.danie.recipebook.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.danie.recipebook.Activities.ViewRecipe;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Recipe;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.RecipeViewHolder>{
    private static final String TAG = "RecipeRecyclerAdapter";
    public static final String SER_KEY = "hello";
    private List<Recipe> recipes;

    public RecipeRecyclerAdapter(List<Recipe> recipes){
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //inflate layout
        View view = layoutInflater.inflate(R.layout.recycler_view_layout, viewGroup, false);

        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(context, view);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.tv.setText(recipes.get(i).getName());
        recipeViewHolder.thisRecipe = recipes.get(i);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        Recipe thisRecipe;

        public RecipeViewHolder(Context c, View v) {
            super(v);
            tv = v.findViewById(R.id.recyclerview_recipe_name);
            tv.setOnClickListener((tv)->{
                Intent i = new Intent(c, ViewRecipe.class);
                Bundle b = new Bundle();
                b.putSerializable(SER_KEY, thisRecipe);
                i.putExtras(b);
                c.startActivity(i);
            });
        }
    }
}
