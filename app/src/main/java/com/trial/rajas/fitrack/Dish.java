package com.trial.rajas.fitrack;

import java.util.ArrayList;

/**
 * Created by rajas on 1/7/2018.
 */

public class Dish {

    public Dish(String name, ArrayList<Ingredient> ingredientArrayList) {
        this.name = name;
        this.ingredientArrayList = ingredientArrayList;
    }

    String name;
    ArrayList<Ingredient> ingredientArrayList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredientArrayList() {
        return ingredientArrayList;
    }

    public void setIngredientArrayList(ArrayList<Ingredient> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }
}
