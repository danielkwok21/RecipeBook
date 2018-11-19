package com.example.danie.recipebook.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.danie.recipebook.Adapters.RecipeRecyclerAdapter;
import com.example.danie.recipebook.ContentProvider.RecipeProvider;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecipeRecyclerAdapter recipeRecyclerAdapter;
    ContentResolver contentResolver;

    EditText searchBox;
    Button search;
    Button createNewRecipe;
    List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(onDbChange()){
            getRecipesFromCP();
        }

        initRecyclerView();
        initComponents();
    }

    private boolean onDbChange(){
        contentResolver = getApplicationContext().getContentResolver();

        contentResolver.registerContentObserver(RecipeProvider.CONTENT_URI, true, new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Log.d(TAG, "onChange: DB changed");
            }
        });
        return true;
    }

    private void getRecipesFromCP(){
        Log.d(TAG, "getRecipesFromCP: ");
        Recipe recipe;

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
            Log.d(TAG, "getRecipesFromCP: No recipes");
        }
    }

    private List<Recipe> searchRecipes(String searchString){
        List<Recipe> queryRecipes = new ArrayList<>();

        for(Recipe r: recipes){
            String name = r.getName().toLowerCase();
            searchString = searchString.toLowerCase();

            if(name.equals(searchString)){
                queryRecipes.add(r);
            }
        }
        return queryRecipes;
    }

    private void initComponents(){
        createNewRecipe = findViewById(R.id.main_create_recipe_btn);
        searchBox = findViewById(R.id.main_search_et);
        search = findViewById(R.id.main_search_btn);

        createNewRecipe.setOnClickListener((v)->{
            Intent i = new Intent(this, CreateRecipe.class);
            startActivity(i);
        });

        search.setOnClickListener((v)->{
            recipes = searchRecipes(searchBox.getText().toString());
            initRecyclerView();
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
