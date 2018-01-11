package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by rajas on 1/6/2018.
 */

public class AddDish extends AppCompatActivity {

    public static ArrayList<Ingredient> ingredientAL= new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.add_dish);
        LinearLayout addDishLL=(LinearLayout) findViewById(R.id.titleAddDishLinearLayout);
        LinearLayout saveDishLL=(LinearLayout) findViewById(R.id.saveDishLinearLayout);
        LinearLayout saveIngredientLL=(LinearLayout) findViewById(R.id.saveIngredientLinearLayout);
        TextView titleTV=(TextView) findViewById(R.id.titleAddDishTextView);
        TextView enterDishTV=(TextView) findViewById(R.id.enterDishNameTextView);
        TextView saveDishTV=(TextView) findViewById(R.id.saveDishTextView);
        TextView addIngredientsTV=(TextView) findViewById(R.id.addDishIngredientsTextView);
        TextView saveIngredientsTV=(TextView) findViewById(R.id.saveIngredientTextView);
        final EditText enterDishET=(EditText) findViewById(R.id.enterDishNameEditText);
        final EditText nameET=(EditText) findViewById(R.id.nameIngredientDishET);
        final EditText quantityET=(EditText) findViewById(R.id.quantityIngredientDishET);
        final EditText unitET=(EditText) findViewById(R.id.unitIngredientDishET);
        ListView ingredientLV=(ListView) findViewById(R.id.dishIngredientListView);
        addDishLL.setBackgroundColor(Color.RED);
        saveDishLL.setBackgroundColor(Color.RED);
        saveIngredientLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        titleTV.setText("Add Dish");
        enterDishTV.setText("Enter the name of your Dish: ");
        addIngredientsTV.setText("Add ingredients needed: ");
        saveDishTV.setText("Save this dish!");
        saveIngredientsTV.setText("Save Ingredient");
        enterDishTV.setTextColor(Color.BLACK);
        addIngredientsTV.setTextColor(Color.BLACK);
        enterDishTV.setTextSize(24);
        addIngredientsTV.setTextSize(24);

        final DishIngredientAdapter dishIngredientAdapter = new DishIngredientAdapter(this, R.layout.fridge_list_row, ingredientAL);
        ingredientLV.setAdapter(dishIngredientAdapter);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser=Backendless.UserService.CurrentUser();

        saveDishLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get dishes jsonarray
                String dishData= currentUser.getProperty("dishes").toString();
                JsonArray dishJsonArray=JSONConversion.getDishJsonArray(dishData);
                //add data to one json
                String dishName= enterDishET.getText().toString();
                if(dishName.equals("") || ingredientAL.isEmpty()){
                    Toast.makeText(context, "Enter the name of the dish and at least one ingredient.", Toast.LENGTH_SHORT).show();
                }
                else{
                    JsonObject dishJson=new JsonObject();
                    dishJson.addProperty("name", dishName);
                    JsonArray ingredientJsonArray= new JsonArray();
                    for (Ingredient ingredient: ingredientAL){
                        JsonObject ingredientJsonObject= new JsonObject();
                        ingredientJsonObject.addProperty("name", ingredient.name);
                        ingredientJsonObject.addProperty("quantity", ingredient.quantity.toString());
                        ingredientJsonObject.addProperty("unit", ingredient.unit);
                        ingredientJsonArray.add(ingredientJsonObject);
                    }
                    String ingredientJsonArrayAsString= ingredientJsonArray.toString();
                    dishJson.addProperty("ingredients",ingredientJsonArrayAsString);
                    dishJsonArray.add(dishJson);
                    String dishJsonArrayAsString=dishJsonArray.toString();
                    currentUser.setProperty("dishes", dishJsonArrayAsString);
                    Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Intent goBack= new Intent(AddDish.this, FridgeActivity.class);
                            startActivity(goBack);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        saveIngredientLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameET.getText().toString();
                String quantity=quantityET.getText().toString();
                String unit=unitET.getText().toString();
                if(name.equals("") || quantity.equals("") || unit.equals("")){
                    Toast.makeText(context, "Enter all details", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        Float quantityFloat=Float.parseFloat(quantity);
                        Ingredient ingredient= new Ingredient(name, quantityFloat, unit);
                        ingredientAL.add(ingredient);
                        dishIngredientAdapter.notifyDataSetChanged();
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(context, "Enter a number please.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AddDish.this);
        alert.setTitle("Cancel Dish?");
        alert.setMessage("Are you sure you don't want to save this dish?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent goBack=new Intent(AddDish.this, FridgeActivity.class);
                finish();
                startActivity(goBack);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });
        alert.show();
    }
}
