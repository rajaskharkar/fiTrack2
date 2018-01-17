package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
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
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        //Set drawer layout
        DrawerLayout mainDrawer = (DrawerLayout) findViewById(R.id.mainDrawer);
        ArrayList<String> drawerItems = new ArrayList<String>();

        fillDrawer(drawerItems);

        ListView drawerListView = (ListView) findViewById(R.id.drawerList);
        ArrayAdapter<String> drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems);
        drawerListView.setAdapter(drawerAdapter);
        //Make drawerListView clickable
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        //Initialize views
        LinearLayout top = InitializeLayouts.initializeLinearLayout(findViewById(R.id.topLayout));
        LinearLayout middle = InitializeLayouts.initializeLinearLayout(findViewById(R.id.middleLayout));
        LinearLayout bottom = InitializeLayouts.initializeLinearLayout(findViewById(R.id.bottomLayout));
        LinearLayout suggestionsLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.suggestionsLayout));

        TextView titleText = InitializeLayouts.initializeTextView(findViewById(R.id.titleText));
        TextView addActivityText = InitializeLayouts.initializeTextView(findViewById(R.id.addActivity));
        TextView scoreText = InitializeLayouts.initializeTextView(findViewById(R.id.scoreText));
        TextView suggestionsTV = InitializeLayouts.initializeTextView(findViewById(R.id.suggestionsText));

        setBackgroundColors(top, middle, bottom, suggestionsLL);
        setTextData(titleText, addActivityText, suggestionsTV);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        String scoreGet = currentUser.getProperty("score").toString();
        setScoreTextSpecs(scoreText, scoreGet);

        bottom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAddActivity();
            }
        });

        suggestionsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendation(currentUser);
            }
        });

    }

    private void recommendation(BackendlessUser currentUser) {
//        String lastUpdated = currentUser.getProperty("updated").toString();
//        String[] split = lastUpdated.split("\\s+");
//        String updateDate = split[2];
//        String updateTime = split[3];
//        String lastUpdatedDate = currentUser.getProperty("last_updated_date").toString();
//        //if(lastUpdatedDate.equals(updateDate)){
//        currentUser.setProperty("last_updated_date", updateDate);
        String scoreString = currentUser.getProperty("score").toString();
        Integer score = Integer.parseInt(scoreString);
        String pastScores = currentUser.getProperty("past_scores").toString();
        JsonParser parser = new JsonParser();
        ArrayList<Integer> scoreAL = new ArrayList<Integer>();
        JsonArray scoresJsonArray = parser.parse(pastScores).getAsJsonArray(); //friendsJSONString
        for (JsonElement jsonElement : scoresJsonArray) {
            JsonObject json = jsonElement.getAsJsonObject();
            String scoreToAddString = json.get("score").getAsString();
            Integer scoreInteger = Integer.parseInt(scoreToAddString);
            scoreAL.add(scoreInteger);
        }
        if (scoreAL.size() < 7) {
            scoreAL.add(score);
        } else {
            scoreAL.remove(0);
            scoreAL.add(score);
        }
        Gson gson = new Gson();
        JsonArray scoreUploadJsonArray = new JsonArray();
        for(Integer scoreInteger: scoreAL){
            JsonObject json= new JsonObject();
            json.addProperty("score", scoreInteger);
            scoreUploadJsonArray.add(json);
        }
        String scoreUploadString= scoreUploadJsonArray.toString();
        currentUser.setProperty("past_scores", scoreUploadString);
        Integer result = 0;
        for (Integer i : scoreAL) {
            result = result + i;
        }
        Integer average = result / 7;
        ArrayList<Integer> scoreDifferencesAL = new ArrayList<>();
        for (int i = scoreAL.size()-1; i > 0; i--) {
            Integer difference = scoreAL.get(i) - scoreAL.get(i - 1);
            scoreDifferencesAL.add(difference);
        }
        Integer resultDifference = 0;
        for (Integer difference : scoreDifferencesAL) {
            resultDifference = resultDifference + difference;
        }
        Integer differenceAverage = resultDifference / scoreDifferencesAL.size();
        String activitySetDataString = currentUser.getProperty("activity_set").toString();
        ArrayList<Activity> activitySetAL = JSONConversion.getListFromJSONStringForActivity(activitySetDataString);
        ArrayList<Activity> suggestedActivitiesAL = new ArrayList<>();
        Intent viewSuggestions = new Intent(HomeActivity.this, ActivitySuggestionsActivity.class);
        if (score <= average) {
            if ((score + 100) < differenceAverage) {
                for (Activity activity : activitySetAL) {
                    if (activity.sign.equals("+")) {
                        if (activity.score >= 100) {
                            suggestedActivitiesAL.add(activity);
                        }
                    }
                }
                updateCurrentUserAndStartActivity(currentUser, "You're slacking! Become more active!", viewSuggestions, suggestedActivitiesAL);
                //Add comment
                //Suggest activities over 100 with + symbol
            } else {
                for (Activity activity : activitySetAL) {
                    if (activity.sign.equals("+")) {
                        suggestedActivitiesAL.add(activity);
                    }
                }
                updateCurrentUserAndStartActivity(currentUser, "You're on the right track, here are some activities for today.", viewSuggestions, suggestedActivitiesAL);
                //suggest any activities with + symbol
            }
        } else if (score > (average + 100)) {
            if (differenceAverage > (score - 100)) {
                for (Activity activity : activitySetAL) {
                    if (activity.sign.equals("-")) {
                        if (activity.score >= 100) {
                            suggestedActivitiesAL.add(activity);
                        }
                    }
                }
                updateCurrentUserAndStartActivity(currentUser, "You're doing amazing! You can have fun today!.", viewSuggestions, suggestedActivitiesAL);
                //suggest activity in - with 100+
            } else {
                for (Activity activity : activitySetAL) {
                    if (activity.sign.equals("-")) {
                        suggestedActivitiesAL.add(activity);
                    }
                }
                updateCurrentUserAndStartActivity(currentUser, "You're doing great! You can slow down a little", viewSuggestions, suggestedActivitiesAL);
                //suggest any negative activity
            }
        } else {
            for (Activity activity : activitySetAL) {
                if (activity.sign.equals("+")) {
                    suggestedActivitiesAL.add(activity);
                }
            }
            updateCurrentUserAndStartActivity(currentUser, "You're on the right track, here are some activities for today.", viewSuggestions, suggestedActivitiesAL);
            //suggest any + activity
        }
        //}
    }

    private void updateCurrentUserAndStartActivity(BackendlessUser currentUser, final String comment, final Intent viewSuggestions, ArrayList<Activity> suggestedActivitiesAL) {
        Gson gson = new Gson();
        JsonArray activitiesJsonArray = gson.toJsonTree(suggestedActivitiesAL).getAsJsonArray();
        final String activitiesString= activitiesJsonArray.toString();
        Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                viewSuggestions.putExtra("comment", comment);
                viewSuggestions.putExtra("data", activitiesString);
                startActivity(viewSuggestions);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(HomeActivity.this, fault.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Try logging out.", Toast.LENGTH_LONG).show();
    }

    private void fillDrawer(ArrayList<String> drawerItems) {
        addToDrawerItems(drawerItems, "Add Activity");
        addToDrawerItems(drawerItems, "Friends");
        addToDrawerItems(drawerItems, "My Matches");
        addToDrawerItems(drawerItems, "FooDiary");
        addToDrawerItems(drawerItems, "The Fridge");
        addToDrawerItems(drawerItems, "LoginPage");
        addToDrawerItems(drawerItems, "Log out");
    }

    private void setScoreTextSpecs(TextView scoreText, String score) {
        //scoreText.setText(score.toString());
        scoreText.setText(score);
        scoreText.setTextColor(Color.BLACK);
    }

    private void setTextData(TextView titleText, TextView addActivityText, TextView suggestionsText) {
        titleText.setText("FitCount");
        addActivityText.setText("Add activity");
        suggestionsText.setText("Activity Suggestions!");
    }

    private void setBackgroundColors(LinearLayout top, LinearLayout middle, LinearLayout bottom, LinearLayout suggestions) {
        top.setBackgroundColor(Color.RED);
        middle.setBackgroundColor(Color.WHITE);
        bottom.setBackgroundColor(Color.RED);
        suggestions.setBackgroundColor(ContextCompat.getColor(this, R.color.DarkRed));
    }


    private void addToDrawerItems(ArrayList<String> drawerItems, String item) {
        drawerItems.add(item);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                startAddActivity();
                break;
            case 1:
                startFriendsActivity();
                break;
            case 2:
                startMyMatchesActivity();
                break;
            case 3:
                startFooDiaryActivity();
                break;
            case 4:
                startFridgeActivity();
                break;
            case 5:
                Intent loginPageStart = new Intent(HomeActivity.this, LoginPage.class);
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
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent logOutIntent = new Intent(HomeActivity.this, LoginPage.class);
                        startActivity(logOutIntent);
                    }
                }.start();
        }
    }

    private void startMyMatchesActivity() {
        Intent myMatchesIntent = new Intent(HomeActivity.this, MyMatches.class);
        startActivity(myMatchesIntent);
    }

    private void startFridgeActivity() {
        Intent fridgeActivityIntent = new Intent(HomeActivity.this, FridgeActivity.class);
        startActivity(fridgeActivityIntent);
    }

    public void startFooDiaryActivity() {
        Intent fooDiaryActivityIntent = new Intent(HomeActivity.this, FooDiaryActivity.class);
        startActivity(fooDiaryActivityIntent);
    }

    public void startFriendsActivity() {
        Intent friendsActivityIntent = new Intent(HomeActivity.this, FriendsActivity.class);
        startActivity(friendsActivityIntent);
    }

    public void startAddActivity() {
        Intent addActivityIntent = new Intent(HomeActivity.this, AddActivity.class);
        startActivity(addActivityIntent);
        finish();
    }
}