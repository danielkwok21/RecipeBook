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

        initRecyclerView(recipes);
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

    //sets global var recipes
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
    /*
    * search recipe list via string provided
    * */
    private List<Recipe> searchRecipe(String searchString){
        Log.d(TAG, "searchRecipe: ");
        List<Recipe> queryRecipes = new ArrayList<>();

        for(Recipe r: recipes){
            String name = r.getName().toLowerCase();
            searchString = searchString.toLowerCase();

            if(name.contains(searchString)){
                Log.d(TAG, "searchRecipe: R: "+r);
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
            List<Recipe> searched_recipes;

            //if search for string in recipe list if string provided
            //if search box empty, returns the full list
            if(!searchBox.getText().toString().isEmpty()){
                searched_recipes = searchRecipe(searchBox.getText().toString());
                initRecyclerView(searched_recipes);
            }else{
                initRecyclerView(recipes);
            }
        });
    }

    private void initRecyclerView(List<Recipe> recipes){
        recyclerView = findViewById(R.id.main_recipes_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recipeRecyclerAdapter = new RecipeRecyclerAdapter(recipes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: MainActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: MainActivity");
    }
}
