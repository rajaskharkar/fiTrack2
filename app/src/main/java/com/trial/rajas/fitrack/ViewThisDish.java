package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.google.gson.JsonArray;

import java.util.ArrayList;

/**
 * Created by rajas on 1/10/2018.
 */

public class ViewThisDish extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        String s = getIntent().getStringExtra("dish");
        Integer dishPosition=Integer.parseInt(s);
        setContentView(R.layout.view_dishes);
        LinearLayout titleLL=(LinearLayout) findViewById(R.id.titleViewDishesLinearLayout);
        LinearLayout viewThisDishLL=(LinearLayout) findViewById(R.id.viewThisDishLinearLayout);
        TextView titleTV=(TextView) findViewById(R.id.titleViewDishesTextView);
        TextView viewThisDishTV=(TextView) findViewById(R.id.viewThisDishTextView);
        ListView dishesLV=(ListView) findViewById(R.id.viewDishesListView);
        titleLL.setBackgroundColor(Color.RED);
        viewThisDishLL.setBackgroundColor(Color.RED);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser=Backendless.UserService.CurrentUser();
        String dishDataString=currentUser.getProperty("dishes").toString();
        final ArrayList<Dish> dishArrayList=JSONConversion.getDishArrayList(dishDataString);
        Dish dish= dishArrayList.get(dishPosition);
        titleTV.setText(dish.name);
        DishIngredientAdapter ingredientAdapter = new DishIngredientAdapter(this, R.layout.view_dishes_row, dish.ingredientArrayList);
        dishesLV.setAdapter(ingredientAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewThisDish.this, ViewDishes.class));
        finish();
    }
}
