package com.trial.rajas.fitrack;

import android.app.Dialog;
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
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by rajas on 10/12/2017.
 */

public class FridgeActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context=this;
        setContentView(R.layout.fridge_activity);
        LinearLayout titleLL=(LinearLayout) findViewById(R.id.fridgeTitleLinearLayout);
        LinearLayout cookLL=(LinearLayout) findViewById(R.id.cookLinearLayout);
        LinearLayout addItemLL=(LinearLayout) findViewById(R.id.addToFridgeLinearLayout);
        LinearLayout resetLL=(LinearLayout) findViewById(R.id.resetFridgeLinearLayout);
        LinearLayout viewDishesLL=(LinearLayout) findViewById(R.id.viewDishesFridgeLinearLayout);
        LinearLayout addDishLL=(LinearLayout) findViewById(R.id.addDishLinearLayout);
        TextView titleTV=(TextView) findViewById(R.id.fridgeTitleTextView);
        TextView cookTV=(TextView) findViewById(R.id.cookTextView);
        TextView addItemTV=(TextView) findViewById(R.id.addToFridgeTextView);
        TextView resetTV=(TextView) findViewById(R.id.resetFridgeTextView);
        TextView viewDishesTV=(TextView) findViewById(R.id.viewDishesFridgeTextView);
        TextView addDishTV=(TextView) findViewById(R.id.addDishTextView);
        ListView fridgeLV= (ListView) findViewById(R.id.fridgeListView);
        titleLL.setBackgroundColor(Color.RED);
        addItemLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        addDishLL.setBackgroundColor(Color.RED);
        cookLL.setBackgroundColor(Color.RED);
        resetLL.setBackgroundColor(Color.RED);
        resetLL.setBackgroundColor(Color.RED);
        viewDishesLL.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
        titleTV.setText("The Fridge");
        addItemTV.setText("Add to Fridge");
        cookTV.setText("Tell me what to cook!");
        resetTV.setText("Reset");
        viewDishesTV.setText("View Dishes");
        addDishTV.setText("Add Dish");

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser= Backendless.UserService.CurrentUser();

        addItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(FridgeActivity.this, AddIngredient.class);
                startActivity(intent);
            }
        });

        //get fridge list
        String fridgeDataString= currentUser.getProperty("fridge").toString();
        ArrayList<Ingredient> ingredientList= JSONConversion.getFridgeListFromJSONString(fridgeDataString);

        FridgeListAdapter fridgeListAdapter = new FridgeListAdapter(this, R.layout.fridge_list_row, ingredientList);
        fridgeLV.setAdapter(fridgeListAdapter);

        addDishLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDishIntent= new Intent(FridgeActivity.this, AddDish.class);
                startActivity(addDishIntent);
            }
        });

        cookLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FridgeActivity.this, CookDish.class));
            }
        });

        viewDishesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FridgeActivity.this, ViewDishes.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(FridgeActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
