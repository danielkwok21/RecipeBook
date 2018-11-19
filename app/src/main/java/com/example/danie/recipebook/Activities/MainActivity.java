package com.example.danie.recipebook.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.example.danie.recipebook.Adapters.RecipeRecyclerAdapter;
import com.example.danie.recipebook.ContentProvider.RecipeProvider;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecipeRecyclerAdapter recipeRecyclerAdapter;

    Button createNewRecipe;

    List<Recipe> recipes = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipes = getRecipeListFromCP();
        initRecyclerView();
        initComponents();

//        for(Recipe r: recipes){
//            Log.d(TAG, "name: "+r.getName());
//            Log.d(TAG, "instruction: "+r.getInstructions());
//        }
    }

    private List<Recipe> getRecipeListFromCP(){
        Recipe recipe;
        List<Recipe> recipes = new ArrayList<>();

        String URL = RecipeProvider.URL;
        Uri uri = Uri.parse(URL);
        Cursor c = managedQuery(uri, null, null, null, RecipeProvider.NAME);

        if(c.moveToFirst()){
            do{
                String name = c.getString(c.getColumnIndex(RecipeProvider.NAME));
                String instructions = c.getString(c.getColumnIndex(RecipeProvider.INSTRUCTIONS));

                recipe = new Recipe(name, instructions);
                recipes.add(recipe);
            }while(c.moveToNext());
        }else{
            Log.d(TAG, "getRecipeListFromCP: No recipes");
        }

        return recipes;
    }

    private void initComponents(){
        createNewRecipe = (Button)findViewById(R.id.main_create_recipe_btn);

        createNewRecipe.setOnClickListener((v)->{
            Intent i = new Intent(this, CreateRecipe.class);
            startActivity(i);
        });
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.main_recipes_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recipeRecyclerAdapter = new RecipeRecyclerAdapter(recipes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeRecyclerAdapter);
    }
}
