package com.trial.rajas.fitrack;

import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
        import android.support.v4.widget.ListViewAutoScrollHelper;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
        import java.util.List;

public class HomeActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //Set drawer layout
        DrawerLayout mainDrawer= (DrawerLayout) findViewById(R.id.mainDrawer);
        ArrayList<String> drawerItems= new ArrayList<String>();

        fillDrawer(drawerItems);

        ListView drawerListView=(ListView) findViewById(R.id.drawerList);
        ArrayAdapter<String> drawerAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerListView.setAdapter(drawerAdapter);
        //Make drawerListView clickable
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        //Initialize views
        LinearLayout top= InitializeLayouts.initializeLinearLayout(findViewById(R.id.topLayout));
        LinearLayout middle=InitializeLayouts.initializeLinearLayout(findViewById(R.id.middleLayout));
        LinearLayout bottom=InitializeLayouts.initializeLinearLayout(findViewById(R.id.bottomLayout));

        TextView titleText= InitializeLayouts.initializeTextView(findViewById(R.id.titleText));
        TextView addActivityText= InitializeLayouts.initializeTextView(findViewById(R.id.addActivity));
        TextView scoreText=InitializeLayouts.initializeTextView(findViewById(R.id.scoreText));

        setBackgroundColors(top,middle,bottom);
        setTextData(titleText, addActivityText);

        Integer score=0;
        setScoreTextSpecs(scoreText, score);

        bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAddActivity();
            }
        });
    }

    public void onBackPressed(){
        Toast.makeText(getApplicationContext(), "Try logging out.", Toast.LENGTH_LONG).show();
    }

    private void fillDrawer(ArrayList<String> drawerItems) {
        addToDrawerItems(drawerItems, "Add Activity");
        addToDrawerItems(drawerItems, "Friends");
        addToDrawerItems(drawerItems, "FitSesh");
        addToDrawerItems(drawerItems, "FooDiary");
        addToDrawerItems(drawerItems, "The Fridge");
        addToDrawerItems(drawerItems, "LoginPage");
        addToDrawerItems(drawerItems, "Log out");
    }

    private void setScoreTextSpecs(TextView scoreText, Integer score) {
        scoreText.setText(score.toString());
        scoreText.setTextColor(Color.BLACK);
    }

    private void setTextData(TextView titleText, TextView addActivityText) {
        titleText.setText("FitCount");
        addActivityText.setText("Add activity");
    }

    private void setBackgroundColors(LinearLayout top, LinearLayout middle, LinearLayout bottom) {
        top.setBackgroundColor(Color.RED);
        middle.setBackgroundColor(Color.WHITE);
        bottom.setBackgroundColor(Color.RED);
    }



    private void addToDrawerItems(ArrayList<String> drawerItems, String item) {
        drawerItems.add(item);
    }

    private void selectItem(int position) {
        switch(position){
            case 0:
                startAddActivity();
                break;
            case 1:
                startFriendsActivity();
                break;
            case 2:
                startFitSeshActivity();
                break;
            case 3:
                startFooDiaryActivity();
                break;
            case 4:
                startFridgeActivity();
                break;
            case 5:
                Intent loginPageStart= new Intent(HomeActivity.this, LoginPage.class);
                startActivity(loginPageStart);
                break;
            case 6:
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getApplicationContext(), fault.getCode().toString(), Toast.LENGTH_LONG).show();
                    }
                });

                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {}
                    public void onFinish() {
                        Intent logOutIntent= new Intent(HomeActivity.this, LoginPage.class);
                        startActivity(logOutIntent);
                    }
                }.start();
        }
    }

    private void startFridgeActivity() {
        Intent fridgeActivityIntent= new Intent(HomeActivity.this, FridgeActivity.class);
        startActivity(fridgeActivityIntent);
    }

    public void startFooDiaryActivity() {
        Intent fooDiaryActivityIntent= new Intent(HomeActivity.this, FooDiaryActivity.class);
        startActivity(fooDiaryActivityIntent);
    }

    public void startFitSeshActivity() {
        Intent fitSeshActivityIntent= new Intent(HomeActivity.this, FitSeshActivity.class);
        startActivity(fitSeshActivityIntent);

    }

    public void startFriendsActivity() {
        Intent friendsActivityIntent= new Intent(HomeActivity.this, FriendsActivity.class);
        startActivity(friendsActivityIntent);
    }

    public void startAddActivity(){
        Intent addActivityIntent= new Intent(HomeActivity.this, AddActivity.class);
        startActivity(addActivityIntent);
    }
}

//        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("Alert message to be shown");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//        new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.show();