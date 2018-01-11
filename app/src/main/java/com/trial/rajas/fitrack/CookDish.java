package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by rajas on 1/8/2018.
 */

public class CookDish extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.cook_dish);
        LinearLayout titleLL = (LinearLayout) findViewById(R.id.dishNameLinearLayout);
        LinearLayout randomizeLL = (LinearLayout) findViewById(R.id.randomizeLinearLayout);
        LinearLayout cookThisLL = (LinearLayout) findViewById(R.id.cookThisLinearLayout);
        TextView titleTV= (TextView) findViewById(R.id.dishNameTextView);
        TextView randomizeTV= (TextView) findViewById(R.id.randomizeTextView);
        TextView cookThisTV= (TextView) findViewById(R.id.cookThisTextView);
        ListView ingredientsLV= (ListView) findViewById(R.id.ingredientsNeededListView);
        titleLL.setBackgroundColor(Color.RED);
        randomizeLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        cookThisLL.setBackgroundColor(Color.RED);
        titleTV.setTextSize(24);
        randomizeTV.setText("Randomize");
        cookThisTV.setText("Cook this!");

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser= Backendless.UserService.CurrentUser();
        String fridgeDataString= currentUser.getProperty("fridge").toString();
        String dishesDataString= currentUser.getProperty("dishes").toString();
        ArrayList<Ingredient> fridgeArrayList=JSONConversion.getFridgeListFromJSONString(fridgeDataString);
        ArrayList<Dish> dishArrayList=JSONConversion.getDishArrayList(dishesDataString);
        ArrayList<String> ingredientsAvailableNamesAL= new ArrayList<>();
        ArrayList<Dish> dishesAvailable= new ArrayList<>();
        for(Ingredient ingredient: fridgeArrayList){
            ingredientsAvailableNamesAL.add(ingredient.name);
        }
        for(Dish dish: dishArrayList){
            ArrayList<String> ingredientNamesAL=new ArrayList<>();
            for(Ingredient ingredient: dish.ingredientArrayList){
                ingredientNamesAL.add(ingredient.name);
            }
            ArrayList<Ingredient> usableIngredients= new ArrayList<>();
            if(ingredientsAvailableNamesAL.containsAll(ingredientNamesAL)){
                for (Ingredient ingredient: dish.ingredientArrayList){
                    //get object from arraylist where x
                    for(Ingredient ingredientFridge: fridgeArrayList){
                        if(ingredient.name.equals(ingredientFridge.name)){
                            if(ingredient.quantity<=ingredientFridge.quantity){
                                usableIngredients.add(ingredient);
                            }
                        }
                    }
                }
            }
            if(usableIngredients.size()==dish.ingredientArrayList.size()){
                dishesAvailable.add(dish);
            }
        }

        if(dishesAvailable.isEmpty()){
            titleTV.setText("No dish found! Please add more dishes or stock up your fridge!");
        }
        else{
            Random random= new Random();
            int index= random.nextInt(dishesAvailable.size());
            Dish selectedDish=dishesAvailable.get(index);
            titleTV.setText(selectedDish.name);
            FridgeListAdapter fridgeListAdapter = new FridgeListAdapter(this, R.layout.fridge_list_row, selectedDish.ingredientArrayList);
            ingredientsLV.setAdapter(fridgeListAdapter);
        }

        randomizeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CookDish.this,FridgeActivity.class));
        finish();
    }
}
