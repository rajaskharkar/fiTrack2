package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by rajas on 1/3/2018.
 */

public class AddIngredient extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Context context=this;
        setContentView(R.layout.add_ingredient);
        LinearLayout titleLL=(LinearLayout) findViewById(R.id.addIngredientTitleLinearLayout);
        LinearLayout nameLL=(LinearLayout) findViewById(R.id.ingredientNameLinearLayout);
        LinearLayout quantityLL=(LinearLayout) findViewById(R.id.ingredientQuantityLinearLayout);
        LinearLayout unitLL=(LinearLayout) findViewById(R.id.ingredientUnitLinearLayout);
        LinearLayout addLL=(LinearLayout) findViewById(R.id.addIngredientLinearLayout);
        TextView titleTV=(TextView) findViewById(R.id.addIngredientTitleTextView);
        TextView nameTV=(TextView) findViewById(R.id.ingredientNameTextView);
        TextView quantityTV=(TextView) findViewById(R.id.ingredientQuantityTextView);
        TextView unitTV=(TextView) findViewById(R.id.ingredientUnitSignUpTextView);
        TextView addTV=(TextView) findViewById(R.id.addIngredientTextView);
        final EditText nameET=(EditText) findViewById(R.id.ingredientNameEditText);
        final EditText quantityET=(EditText) findViewById(R.id.ingredientQuantityEditText);
        final EditText unitET=(EditText) findViewById(R.id.ingredientUnitSignUpEditText);

        titleLL.setBackgroundColor(Color.RED);
        addLL.setBackgroundColor(Color.RED);

        nameTV.setTextColor(Color.BLACK);
        quantityTV.setTextColor(Color.BLACK);
        unitTV.setTextColor(Color.BLACK);

        titleTV.setText("Add Ingredient");
        nameTV.setText("Ingredient: ");
        quantityTV.setText("Quantity: ");
        unitTV.setText("Unit: ");
        addTV.setText("Add!");

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser= Backendless.UserService.CurrentUser();

        addLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get fridge list from BE
                String fridgeDataString=currentUser.getProperty("fridge").toString();
                final JsonArray ingredientJsonArray= JSONConversion.getFridgeJsonArrayFromJSONString(fridgeDataString);
                ArrayList<Ingredient> ingredientList= JSONConversion.getFridgeListFromJSONString(fridgeDataString);
                String name=nameET.getText().toString();
                String quantityString=quantityET.getText().toString();
                Float quantity= Float.parseFloat(quantityString);
                String unit=unitET.getText().toString();
                if(name.equals("") || quantity.equals("") || unit.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill in all the details",Toast.LENGTH_SHORT);
                }
                else{
                    JsonObject ingredientJSON= new JsonObject();
                    ingredientJSON.addProperty("name",name);
                    ingredientJSON.addProperty("quantity",quantity);
                    ingredientJSON.addProperty("unit",unit);
                    ingredientJsonArray.add(ingredientJSON);
                    String ingredientUploadString= ingredientJsonArray.toString();
                    currentUser.setProperty("fridge",ingredientUploadString);
                    Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText(getApplicationContext(), "Added to Fridge!",Toast.LENGTH_SHORT).show();
                            Intent goBack= new Intent(AddIngredient.this, FridgeActivity.class);
                            startActivity(goBack);
                            finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(getApplicationContext(), fault.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });
                }
            }
        });
    }
}
