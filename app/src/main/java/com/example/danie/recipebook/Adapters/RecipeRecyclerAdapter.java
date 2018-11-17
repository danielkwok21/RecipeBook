package com.example.danie.recipebook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danie.recipebook.R;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecipeRecyclerAdapter.RecipeViewHolder>{
    private List<Integer> id;

    public RecipeRecyclerAdapter(List<Integer> id){
        this.id = id;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //inflate layout
        TextView textView = (TextView)layoutInflater.inflate(R.layout.recipe_name_layout, viewGroup, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(textView);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.tv.setText(id.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public RecipeViewHolder(@NonNull TextView tv) {
            super(tv);
            this.tv = tv;
        }
    }
}
