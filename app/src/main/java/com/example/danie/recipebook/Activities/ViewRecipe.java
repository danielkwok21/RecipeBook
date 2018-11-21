package com.example.danie.recipebook.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.danie.recipebook.Adapters.RecipeRecyclerAdapter;
import com.example.danie.recipebook.ContentProvider.RecipeProvider;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Recipe;
import com.example.danie.recipebook.Util;

public class ViewRecipe extends AppCompatActivity {
    private static final String TAG = "ViewRecipe";

    TextView recipeName;
    TextView instructions;
    Button delete;

    Recipe thisRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Intent i = getIntent();
        thisRecipe = (Recipe)i.getSerializableExtra(RecipeRecyclerAdapter.SER_KEY);

        initComponent();
    }

    private void initComponent(){
        recipeName = findViewById(R.id.view_recipe_name_tv);
        instructions = findViewById(R.id.view_recipe_instruction_tv);
        delete = findViewById(R.id.view_recipe_delete_btn);

        recipeName.setText(thisRecipe.getName());
        instructions.setText(thisRecipe.getInstructions());

        delete.setOnClickListener((v)->{
            if(deleteRecipe()){
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean deleteRecipe(){
        String[] args = {thisRecipe.getName()};

        try{
            getContentResolver().delete(RecipeProvider.CONTENT_URI, "name=?", args);
            Util.Toast(getBaseContext(), "Recipe deleted!");

            return true;
        }catch(Exception e){
            Util.Toast(getBaseContext(), "Unable to delete recipe!");
            Log.d(TAG, "deleteRecipe: Error: "+e);
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ViewRecipe");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ViewRecipe");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ViewRecipe");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ViewRecipe");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ViewRecipe");
    }
}
