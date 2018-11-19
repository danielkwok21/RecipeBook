package com.example.danie.recipebook;

public class Recipe {
    String name;
    String instructions;

    public Recipe(String name, String instructions){
        this.name = name;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }
}
