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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        LinearLayout perspectiveLL = (LinearLayout) findViewById(R.id.perspectiveLinearLayout);
        LinearLayout randomizeLL = (LinearLayout) findViewById(R.id.randomizeLinearLayout);
        LinearLayout cookThisLL = (LinearLayout) findViewById(R.id.cookThisLinearLayout);
        LinearLayout caloriesLL = (LinearLayout) findViewById(R.id.caloriesDisplayLinearLayout);
        TextView titleTV= (TextView) findViewById(R.id.dishNameTextView);
        final TextView perspectiveTV= (TextView) findViewById(R.id.perspectiveTextView);
        TextView randomizeTV= (TextView) findViewById(R.id.randomizeTextView);
        TextView cookThisTV= (TextView) findViewById(R.id.cookThisTextView);
        TextView caloriesTV= (TextView) findViewById(R.id.caloriesDisplayTextView);
        ListView ingredientsLV= (ListView) findViewById(R.id.ingredientsNeededListView);
        titleLL.setBackgroundColor(Color.RED);
        perspectiveLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        caloriesLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        randomizeLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        cookThisLL.setBackgroundColor(Color.RED);
        titleTV.setTextSize(24);
        caloriesTV.setTextSize(24);
        perspectiveTV.setTextSize(16);
        randomizeTV.setText("Randomize");
        cookThisTV.setText("Cook this!");

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser= Backendless.UserService.CurrentUser();
        String fridgeDataString= currentUser.getProperty("fridge").toString();
        String dishesDataString= currentUser.getProperty("dishes").toString();
        final ArrayList<Ingredient> fridgeArrayList=JSONConversion.getFridgeListFromJSONString(fridgeDataString);
        ArrayList<Dish> dishArrayList=JSONConversion.getDishArrayList(dishesDataString);
        final ArrayList<String> ingredientsAvailableNamesAL= new ArrayList<>();
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
            final Dish selectedDish=dishesAvailable.get(index);
            titleTV.setText(selectedDish.name);
            ArrayList<Ingredient> ingredientsWithCalories= CalorieCounter.getCalorieCount(selectedDish, caloriesTV, currentUser);
            FridgeListAdapter fridgeListAdapter = new FridgeListAdapter(this, R.layout.fridge_list_row, ingredientsWithCalories);
            ingredientsLV.setAdapter(fridgeListAdapter);

            String activitySetString=currentUser.getProperty("activity_set").toString();
            ArrayList<Activity> activitySetAL= JSONConversion.getListFromJSONStringForActivity(activitySetString);
            final ArrayList<Activity> positiveActivities= new ArrayList<>();

            for(Activity activity: activitySetAL){
                if(activity.sign.equals("+")){
                    if(activity.action.equals("true")){
                        positiveActivities.add(activity);
                    }
                }
            }
            perspectiveLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findPerspectiveAndDisplay(perspectiveTV, positiveActivities, selectedDish, currentUser);
                }
            });
            findPerspectiveAndDisplay(perspectiveTV, positiveActivities, selectedDish, currentUser);

            cookThisLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer index=-1;
                    for(Ingredient ingredient: selectedDish.ingredientArrayList){
                        if(ingredientsAvailableNamesAL.contains(ingredient.name)){
                            index=ingredientsAvailableNamesAL.indexOf(ingredient.name);
                        }
                        if (index != -1) {
                            Ingredient ingredientInFridge=fridgeArrayList.get(index);
                            ingredientInFridge.quantity=ingredientInFridge.quantity-ingredient.quantity;
                        }
                    }
                    Gson gson= new Gson();
                    JsonArray jsonArray= gson.toJsonTree(fridgeArrayList).getAsJsonArray();
                    String upload= jsonArray.toString();
                    currentUser.setProperty("fridge", upload);
                    Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText(context, "Happy Cooking!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CookDish.this, FridgeActivity.class));
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(context, fault.getMessage(), Toast.LENGTH_LONG);
                        }
                    });
                }
            });
        }

        randomizeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void findPerspectiveAndDisplay(TextView perspectiveTV, ArrayList<Activity> positiveActivities, Dish selectedDish, BackendlessUser currentUser) {
        Activity activity= getRandomActivity(positiveActivities);
        HashMap caloriesHashmap= CalorieCounter.getDishCalorieCount(selectedDish, currentUser);
        Float totalCalories=Float.parseFloat(caloriesHashmap.get("calories").toString());
        Float activityScoreFloat=Float.parseFloat(activity.score.toString());
        Float activityValue=totalCalories/(4*activityScoreFloat);
        perspectiveTV.setText("Perspective: To burn this dish, you'll need to do "+activityValue+" hours of '"+activity.task+"'");
    }


    private Activity getRandomActivity(ArrayList<Activity> positiveActivities) {
        Random random= new Random();
        Integer index= random.nextInt(positiveActivities.size());
        Activity randomActivity= positiveActivities.get(index);
        return randomActivity;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CookDish.this,FridgeActivity.class));
        finish();
    }
}
