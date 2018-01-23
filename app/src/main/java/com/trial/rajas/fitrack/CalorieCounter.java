package com.trial.rajas.fitrack;

import android.text.BoringLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rajas on 1/21/2018.
 */

public class CalorieCounter {

    public static ArrayList<Ingredient> getCalorieCount(Dish dish, TextView textView, BackendlessUser currentUser){
        String fridgeDataString=currentUser.getProperty("fridge").toString();
        ArrayList<Ingredient> ingredientsInFridge=JSONConversion.getFridgeListFromJSONString(fridgeDataString);
        ArrayList<String> fridgeIngredientsName= new ArrayList<>();
        HashMap hashmap= CalorieCounter.getDishCalorieCount(dish, currentUser);
        Float totalCalories= Float.parseFloat(hashmap.get("calories").toString());
        Integer checkAllDishes=Integer.parseInt(hashmap.get("flag").toString());
        if(checkAllDishes==1){
            textView.setText("Minimum calories in this dish: "+totalCalories);
        }
        else{
            textView.setText("Calories in this dish: "+totalCalories);
        }
        return dish.ingredientArrayList;
    }

    public static HashMap getDishCalorieCount(Dish dish, BackendlessUser currentUser){
        String fridgeDataString=currentUser.getProperty("fridge").toString();
        ArrayList<Ingredient> ingredientsInFridge=JSONConversion.getFridgeListFromJSONString(fridgeDataString);
        ArrayList<String> fridgeIngredientsName= new ArrayList<>();
        Float totalCalories= new Float(0);
        HashMap hashmap= new HashMap();
        Integer checkAllDishes=0;
        for(Ingredient i: ingredientsInFridge){
            fridgeIngredientsName.add(i.name);
        }
        for(Ingredient ingredient: dish.ingredientArrayList){
            Integer index=fridgeIngredientsName.indexOf(ingredient.name);
            if(index!= -1){
                Ingredient ingredientInFridge= ingredientsInFridge.get(index);
                ingredient.calorie_count=ingredientInFridge.calorie_count*ingredient.quantity;
                ingredient.setCalorieCount(ingredientInFridge.calorie_count);
                totalCalories=totalCalories+ingredientInFridge.calorie_count;
            }
            else{
                checkAllDishes=1;
            }
        }
        hashmap.put("calories", totalCalories);
        hashmap.put("flag", checkAllDishes);
        return hashmap;
    }

    public static boolean isIngredientInFridge(Ingredient ingredient, BackendlessUser currentUser){
        Boolean check=new Boolean(false);
        String fridgeDataString=currentUser.getProperty("fridge").toString();
        ArrayList<Ingredient> ingredientsInFridge=JSONConversion.getFridgeListFromJSONString(fridgeDataString);
        ArrayList<String> fridgeIngredientsName= new ArrayList<>();
        for(Ingredient i: ingredientsInFridge){
            fridgeIngredientsName.add(i.name);
        }
        if (fridgeIngredientsName.contains(ingredient.name)){
            check=true;
        }
        else{
            check=false;
        }
        return check;
    }
}
