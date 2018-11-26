package com.example.danie.recipebook.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.danie.recipebook.ContentProvider.RecipeProvider;
import com.example.danie.recipebook.Contract;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Util;

public class CreateRecipe extends AppCompatActivity {
    private static final String TAG = "CreateRecipe";

    EditText recipeName;
    EditText instructions;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        initComponents();
    }

    private void initComponents(){
        recipeName = findViewById(R.id.create_recipe_name_et);
        instructions = findViewById(R.id.create_recipe_instructions_et);
        create = findViewById(R.id.create_recipe_create_btn);

        create.setOnClickListener((v)->{
            if(uploadToDB()){
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean uploadToDB(){
        ContentValues values = new ContentValues();

        try{
            String name = recipeName.getText().toString();
            String instruction = instructions.getText().toString();

            if(!name.isEmpty()){
                values.put(RecipeProvider.NAME, name);
            }else{
                throw new Exception();
            }

            if(!instruction.isEmpty()){
                values.put(RecipeProvider.INSTRUCTIONS, instruction);
            }else{
                throw new Exception();
            }

            getContentResolver().insert(Contract.CONTENT_URI, values);
            Util.Toast(getBaseContext(), "Recipe created!");

            return true;
        }catch(Exception e){
            Util.Toast(getBaseContext(), "Unable to create recipe!Fields cannot be empty");
            return false;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: CreateRecipe");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: CreateRecipe");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: CreateRecipe");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: CreateRecipe");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: CreateRecipe");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: CreateRecipe");
    }
}
