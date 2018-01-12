package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.utils.JSONObjectConverter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity{

    public ArrayList<String> taskList;

    AsyncCallback<BackendlessUser> updateScoreCallback= new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {
            Toast.makeText(getApplicationContext(), "Activity added! Your score is "+response.getProperty("score").toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    AsyncCallback<BackendlessUser> saveActivityCallback= new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {
            Toast.makeText(getApplicationContext(), "Activity saved!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    AsyncCallback<BackendlessUser> getActivities= new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {

        }

        @Override
        public void handleFault(BackendlessFault fault) {
            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity2);
        Context context=this;

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser= Backendless.UserService.CurrentUser();

        LinearLayout addLinearLayout=InitializeLayouts.initializeLinearLayout(findViewById(R.id.addLinearLayout));
        LinearLayout viewLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.viewLinearLayout));
        LinearLayout titleLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.titleSpace));
        LinearLayout selectActivityLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.selectActivityLinearLayout));
        LinearLayout blankLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.blankSpace));
        LinearLayout pleaseAddLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.pleaseAddLinearLayout));
        LinearLayout saveActivityLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.saveActivityLinearLayout));
        final LinearLayout currentScoreLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.currentScoreLinearLayout));
        LinearLayout enterActivityLinearLayout=InitializeLayouts.initializeLinearLayout(findViewById(R.id.enterActivityLinearLayout));

        TextView titleTextView= InitializeLayouts.initializeTextView(findViewById(R.id.titleText));
        TextView pleaseAddTextView= InitializeLayouts.initializeTextView(findViewById(R.id.pleaseAddTextView));
        TextView saveActivityTextView= InitializeLayouts.initializeTextView(findViewById(R.id.saveActivityTextView));
        final TextView currentScoreTextView= InitializeLayouts.initializeTextView(findViewById(R.id.currentScoreTextView));
        TextView viewTextView= InitializeLayouts.initializeTextView(findViewById(R.id.viewTextView));
        TextView addActivityTextView= InitializeLayouts.initializeTextView(findViewById(R.id.addActivityTextView));

        final EditText activityET=InitializeLayouts.initializeEditText(findViewById(R.id.activityEditText));
        final EditText scoreET=InitializeLayouts.initializeEditText(findViewById(R.id.scoreEditText));

        final Spinner activitySpinner= (Spinner) findViewById(R.id.activitySpin);
        final Spinner signSpinner= (Spinner) findViewById(R.id.signSpin);


        String spinArray[]={"+","-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinArray);
        signSpinner.setAdapter(adapter);
        signSpinner.setSelection(0);

        ArrayList<String> taskList=new ArrayList<>();
        ArrayList<Integer> scoreList= new ArrayList<>();
        final ArrayList<Activity> activityList= new ArrayList<>();

        //fillActivityList(activityList);
        addToTaskAndScoreList(activityList, taskList, scoreList);

        final String scoreString= currentUser.getProperty("score").toString();

        setBackGroundColors(titleLL, pleaseAddLL, currentScoreLL, saveActivityLL, addLinearLayout, viewLL, context);
        setTextStrings(titleTextView, pleaseAddTextView, currentScoreTextView, saveActivityTextView, addActivityTextView, viewTextView, scoreString);
        activityET.setTextColor(Color.BLACK);
        scoreET.setTextColor(Color.BLACK);
        activityET.setTextSize(26);
        scoreET.setTextSize(26);

        JsonParser parser = new JsonParser();
        String activityJSONString=currentUser.getProperty("activity_set").toString();
        final JsonArray activityJSONArray = parser.parse(activityJSONString).getAsJsonArray();

        for(JsonElement activityJsonElement: activityJSONArray){
            JsonObject json= activityJsonElement.getAsJsonObject();
            String task=json.get("task").getAsString();
            String sign;
            try{
                sign=json.get("sign").getAsString();
            }
            catch(NumberFormatException ex){
                sign=json.get("sign").getAsString();
            }
            Integer activityScore=json.get("score").getAsInt();
            Activity newActivity= new Activity(task, sign, activityScore);
            activityList.add(newActivity);
        }

        SpinnerCustomAdapter spinnerCustomAdapter= new SpinnerCustomAdapter(this, R.layout.row, activityList);
        activitySpinner.setAdapter(spinnerCustomAdapter);

        addLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoreString2= currentUser.getProperty("score").toString();
                Integer score=Integer.valueOf(scoreString2);
                String activityFromET= activityET.getText().toString();
                if(!activityFromET.equals("")){
                    String sign= signSpinner.getSelectedItem().toString();
                    String scoreStringFromET= scoreET.getText().toString();
                    if(scoreStringFromET.equals("")){
                        Toast.makeText(getApplicationContext(), "Enter a score", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Integer updateScore= Integer.valueOf(scoreStringFromET);
                        if(sign.equals("+")){
                            score=score+updateScore;
                        }
                        else{
                            score=score-updateScore;
                        }
                        Activity activity= new Activity(activityFromET, sign, updateScore);
                        addActivityToLog(activity,currentUser);
                        editScoreInMatches(currentUser, updateScore, sign);
                        addToMatchLog(currentUser, activity);
                    }
                }
                else{
                    Integer position= activitySpinner.getSelectedItemPosition();
                    if(position!=-1){
                        Activity activity= activityList.get(position);
                        Integer updateScore=activity.getScore();
                        String sign=activity.getSign();
                        if(sign.equals("+")){
                            score=score+updateScore;
                        }
                        else{
                            score=score-updateScore;
                        }
                        addActivityToLog(activity, currentUser);
                        editScoreInMatches(currentUser, updateScore, sign);
                        addToMatchLog(currentUser, activity);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Select or enter an Activity.", Toast.LENGTH_LONG).show();
                    }
                }
                currentUser.setProperty("score",score);
                currentScoreTextView.setText("Current Score: "+score);
                Backendless.UserService.update(currentUser, updateScoreCallback);

            }
        });

        saveActivityLL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String activityFromET= activityET.getText().toString();
                String scoreFromET= scoreET.getText().toString();
                Integer scoreFromEditText=Integer.valueOf(scoreFromET);
                String sign= signSpinner.getSelectedItem().toString();
                if(!activityFromET.equals("")){
                    Activity activityToAdd=new Activity(activityFromET, sign, scoreFromEditText);
                    activityList.add(activityToAdd);
                    JsonObject newActivity=new JsonObject();
                    newActivity.addProperty("task", activityFromET);
                    newActivity.addProperty("sign", sign);
                    newActivity.addProperty("score", scoreFromEditText);
                    activityJSONArray.add(newActivity);
                    String activityJSONString= activityJSONArray.toString();
                    currentUser.setProperty("activity_set",activityJSONString);
                    Backendless.UserService.update(currentUser, saveActivityCallback);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a new activity to save it.", Toast.LENGTH_LONG).show();
                }
            }
        });

        viewLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AddActivity.this, ViewActivityLog.class);
                intent.putExtra("name", "default");
                startActivity(intent);
            }
        });
    }

    private void addToMatchLog(BackendlessUser currentUser, final Activity activity) {
        final String name=currentUser.getProperty("username").toString();
        final String whereClause = "player1_name= '"+name+"' or player2_name= '"+name+"'";
        final DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        Backendless.Data.of("Matches").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> response) {
                for(Map match: response){
                    String player1_name= match.get("player1_name").toString();
                    //String player2_name= match.get("player2_name").toString();
                    if(player1_name.equals(name)){
                        addingActivityInMatchLog(match, "player1_log", activity);
                    }
                    else{
                        addingActivityInMatchLog(match, "player2_log", activity);
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddActivity.this, "Error while adding to match logs: "+ fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addingActivityInMatchLog(Map match, String player_log, Activity activity) {
        String player1_log=match.get(player_log).toString();
        ArrayList<Activity> activitiesAL=JSONConversion.getListFromJSONStringForActivity(player1_log);
        activitiesAL.add(activity);
        Gson gson=new Gson();
        JsonElement element = gson.toJsonTree(activitiesAL, new TypeToken<ArrayList<Activity>>() {}.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        String activityLogStringToUpload= jsonArray.toString();
        match.put(player_log, activityLogStringToUpload);
        Backendless.Persistence.of("Matches").save(match, new AsyncCallback<Map>() {
            @Override
            public void handleResponse(Map response) {
                //Toast.makeText(AddActivity.this, "Something's being saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AddActivity.this, "Error while saving Match: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editScoreInMatches(BackendlessUser currentUser, final Integer updateScore, final String sign) {
        String currentUserMatches= currentUser.getProperty("matches").toString();
        final ArrayList<String> matchesAL=JSONConversion.getMatchListFromJSONString(currentUserMatches);
        final String currentUserName= currentUser.getProperty("username").toString();
        //get matches with friends
        for(String opponent: matchesAL){
            String whereClause = "(player1_name= '"+currentUserName+"' or player1_name= '"+opponent+"') and (player2_name= '"+currentUserName+"' or player2_name= '"+opponent+"')";
            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);

            Backendless.Data.of("Matches").find(queryBuilder, new AsyncCallback<List<Map>>() {
                @Override
                public void handleResponse(List<Map> matchList) {
                    if (!matchList.isEmpty()) {
                        Map match = matchList.get(0);
                        String player1_name = match.get("player1_name").toString();
                        String player2_name = match.get("player2_name").toString();
                        String player1_score = match.get("player1_score").toString();
                        String player2_score = match.get("player2_score").toString();
                        if (currentUserName.equals(player1_name)) {
                            Integer newScore=updateMatchScore(sign, updateScore,player1_score);
                            match.put("player1_score", newScore);
                        } else {
                            Integer newScore=updateMatchScore(sign, updateScore,player2_score);
                            match.put("player2_score", newScore);
                        }
                        Backendless.Persistence.of("Matches").save(match, new AsyncCallback<Map>() {
                            @Override
                            public void handleResponse(Map response) {
                                //Toast.makeText(getApplicationContext(), "Activity added!",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }

                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });       
        }
    }

    private Integer updateMatchScore(String sign, Integer updateScore, String player_score) {
        Integer score=0;
        Integer player_scoreInt=Integer.valueOf(player_score);
        if(sign.equals("+")){
            score=updateScore+player_scoreInt;
        }
        else{
            score=player_scoreInt-updateScore;
        }
        return score;
    }

    private void addActivityToLog(Activity activity, BackendlessUser currentUser) {
        //get activity_log from backendless
        JsonParser parser = new JsonParser();
        String activityLogString=currentUser.getProperty("activity_log").toString();
        ArrayList<Activity> listToReturn= JSONConversion.getListFromJSONStringForActivity(activityLogString);

        //add activity to activity log
        listToReturn.add(activity);

        //uploadactivity log to backendless.. convert list to json.
        Gson gson= new Gson();
        JsonElement element = gson.toJsonTree(listToReturn, new TypeToken<ArrayList<Activity>>() {}.getType());
        JsonArray jsonArray = element.getAsJsonArray();
        String activityLogStringToUpload= jsonArray.toString();
        currentUser.setProperty("activity_log", activityLogStringToUpload);
    }

    private void setTextStrings(TextView titleTextView, TextView pleaseAddTextView, TextView currentScoreTextView, TextView saveActivityTextView, TextView addActivityTextView, TextView viewTextView, String scoreString) {
        titleTextView.setText("Add Activity");
        pleaseAddTextView.setText("Add activity or select: ");
        currentScoreTextView.setText("Current Score: "+scoreString);
        saveActivityTextView.setText("Save");
        addActivityTextView.setText("Add Activity!");
        viewTextView.setText("View log");
    }

    private void setBackGroundColors(LinearLayout titleLL, LinearLayout pleaseAddLL, LinearLayout currentScoreLL, LinearLayout saveActivityLL, LinearLayout addLinearLayout, LinearLayout viewLL, Context context) {
        titleLL.setBackgroundColor(Color.RED);
        pleaseAddLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        currentScoreLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        saveActivityLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        addLinearLayout.setBackgroundColor(Color.RED);
        viewLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
    }

    private void addToTaskAndScoreList(ArrayList<Activity> activityList, ArrayList<String> taskList, ArrayList<Integer> scoreList) {
        for (Activity activity: activityList){
            taskList.add(activity.task);
            scoreList.add(activity.score);
        }
    }

    private void fillActivityList(ArrayList<Activity> activityList) {
        activityList.add(new Activity("Go to the gym", "+", 50));
        activityList.add(new Activity("Eat a pizza", "-", 25));
        activityList.add(new Activity("Go for a run", "+", 30));
    }

    public void onBackPressed(){
        Intent goHome= new Intent(AddActivity.this, HomeActivity.class);
        startActivity(goHome);
    }
}