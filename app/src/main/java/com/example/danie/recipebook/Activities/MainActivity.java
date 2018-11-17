package com.example.danie.recipebook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;

import com.example.danie.recipebook.Adapters.RecipeRecyclerAdapter;
import com.example.danie.recipebook.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Integer> id;
    RecipeRecyclerAdapter recipeRecyclerAdapter;

    Integer[] testid = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
    }

    private void initComponents(){
        recyclerView = findViewById(R.id.recipes_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        id = Arrays.asList(testid);
        recipeRecyclerAdapter = new RecipeRecyclerAdapter(id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeRecyclerAdapter);
    }
}
