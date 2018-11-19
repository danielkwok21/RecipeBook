package com.example.danie.recipebook.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.danie.recipebook.ContentProvider.RecipeProvider;
import com.example.danie.recipebook.R;
import com.example.danie.recipebook.Util;

public class CreateRecipe extends AppCompatActivity {

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

            getContentResolver().insert(RecipeProvider.CONTENT_URI, values);
            Util.Toast(getBaseContext(), "Recipe created!");

            return true;
        }catch(Exception e){
            Util.Toast(getBaseContext(), "Unable to create recipe!Fields cannot be empty");
            return false;
        }
    }
}
