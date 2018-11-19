package com.example.danie.recipebook;

import java.io.Serializable;

public class Recipe implements Serializable {
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
