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

public class ViewDishes extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.view_dishes);
        LinearLayout titleLL=(LinearLayout) findViewById(R.id.titleViewDishesLinearLayout);
        LinearLayout viewThisDishLL=(LinearLayout) findViewById(R.id.viewThisDishLinearLayout);
        TextView titleTV=(TextView) findViewById(R.id.titleViewDishesTextView);
        TextView viewThisDishTV=(TextView) findViewById(R.id.viewThisDishTextView);
        ListView dishesLV=(ListView) findViewById(R.id.viewDishesListView);
        titleLL.setBackgroundColor(Color.RED);
        viewThisDishLL.setBackgroundColor(Color.RED);
        titleTV.setText("Dishes Added");
        viewThisDishTV.setText("Tap on a dish to view required ingredients");

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser=Backendless.UserService.CurrentUser();
        String dishDataString=currentUser.getProperty("dishes").toString();
        final ArrayList<Dish> dishArrayList=JSONConversion.getDishArrayList(dishDataString);
        ArrayList<String> dishNamesAL= new ArrayList<>();
        for(Dish dish: dishArrayList){
            dishNamesAL.add(dish.name);
        }
        ViewDishesAdapter fridgeListAdapter = new ViewDishesAdapter(this, R.layout.view_dishes_row, dishNamesAL);
        dishesLV.setAdapter(fridgeListAdapter);

        dishesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(ViewDishes.this, ViewThisDish.class);
                Integer position2=position;
                intent.putExtra("dish", position2.toString());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewDishes.this, FridgeActivity.class));
        finish();
    }
}
