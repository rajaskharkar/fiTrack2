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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.Persistence;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.io.BackendlessUserFactory;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.servercode.BackendlessService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);
        final Context context = this;
        LinearLayout titleLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.titleFriends));
        LinearLayout addFriendLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.addFriendLinearLayout));
        final LinearLayout startSeshLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.startSeshLinearLayout));
        TextView titleFriendsTV = InitializeLayouts.initializeTextView(findViewById(R.id.titleFriendsTextView));
        TextView addFriendTV = InitializeLayouts.initializeTextView(findViewById(R.id.addFriendTextView));
        TextView startSeshTV = InitializeLayouts.initializeTextView(findViewById(R.id.startSeshTextView));
        setBackGroundColors(titleLL, addFriendLL, startSeshLL, context);
        setTextStrings(titleFriendsTV, addFriendTV, startSeshTV);

        final ListView friendsLV = (ListView) findViewById(R.id.friendsListView);
        final ArrayList<String> currentUserFriendsAL = getFriendsfromBackendless();

        final FriendsListAdapter friendsListAdapter = new FriendsListAdapter(this, R.layout.friends_list_row, currentUserFriendsAL);
        friendsLV.setAdapter(friendsListAdapter);

        addFriendLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Add Friend");
                alert.setMessage("Enter friend's name");
                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String searchUser = input.getText().toString();
                        BackendlessUser currentUser= Backendless.UserService.CurrentUser();
                        String currentUserName = currentUser.getProperty("username").toString();
                        if(searchUser.equals(currentUserName)){
                            Toast.makeText(getApplicationContext(), "Lol did you really just try to add yourself?", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            addFriend(currentUserFriendsAL, searchUser);
                        }

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancelled.
                    }
                });
                alert.show();
            }
        });

        friendsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int selectedFriendPosition=position;
                startSeshLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedFriendPosition==-1){
                            Toast.makeText(getApplicationContext(), "Select a friend to start a match with!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final String friendName=currentUserFriendsAL.get(selectedFriendPosition);
                            final BackendlessUser currentUser= Backendless.UserService.CurrentUser();
                            final String currentUserName = currentUser.getProperty("username").toString();
                            //Get matches arrayList
                            String currentUserMatches=currentUser.getProperty("matches").toString();
                            final ArrayList<String> currentUserMatchList= JSONConversion.getMatchListFromJSONString(currentUserMatches);
                            if(currentUserMatchList.contains(friendName)){
                                Toast.makeText(getApplicationContext(), "You already have an ongoing match with this friend!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                HashMap match = new HashMap();
                                //name
                                match.put("player1_name",currentUserName);
                                match.put("player2_name", friendName);
                                //score
                                match.put("player1_score", 0);
                                match.put("player2_score", 0);
                                //log
                                JSONObject firstLog= new JSONObject();
                                JSONArray firstLogJSONArray= new JSONArray();
                                String firstLogUploadString=firstLogJSONArray.toJSONString();
                                try {
                                    firstLog.put("player1_log", " ");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                match.put("player1_log", firstLogUploadString);
                                match.put("player2_log", firstLogUploadString);
                                Backendless.Data.of("Matches").save(match, new AsyncCallback<Map>() {
                                    @Override
                                    public void handleResponse(Map response) {

                                        String whereClause = "username= '" + friendName + "'";
                                        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                                        queryBuilder.setWhereClause(whereClause);

                                        Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
                                            @Override
                                            public void handleResponse(List<BackendlessUser> response) {
                                                if (response.isEmpty()) {
                                                    Toast.makeText(getApplicationContext(), "This person does not have an account on this app! ASK THEM TO JOIN :D", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    currentUserMatchList.add(friendName);
                                                    BackendlessUser friendOpponent= response.get(0); //get friend
                                                    String friendMatchesString= friendOpponent.getProperty("matches").toString();
                                                    ArrayList<String> friendMatchList= JSONConversion.getMatchListFromJSONString(friendMatchesString);
                                                    friendMatchList.add(currentUserName);

                                                    //upload ArrayLists as JSONArrays to Backendless
                                                    uploadMatchListToBackendless(currentUserMatchList, currentUser);
                                                    uploadMatchListToBackendless(friendMatchList, friendOpponent);
                                                    Toast.makeText(getApplicationContext(), "Match started!", Toast.LENGTH_SHORT).show();
                                                    Intent fitSeshActivityIntent= new Intent(FriendsActivity.this, FitSeshActivity.class);
                                                    fitSeshActivityIntent.putExtra("friend", friendName);
                                                    fitSeshActivityIntent.putExtra("previous_activity", "FriendsActivity");
                                                    startActivity(fitSeshActivityIntent);
                                                }
                                            }

                                            @Override
                                            public void handleFault(BackendlessFault fault) {
                                                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

        startSeshLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Select a friend!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadMatchListToBackendless(ArrayList<String> arrayListToUpload, BackendlessUser user) {
        JsonArray matchesJSONUploadArray = new JsonArray();
        for (String friend : arrayListToUpload) {
            JsonObject matchJSON = new JsonObject();
            matchJSON.addProperty("match", friend);
            matchesJSONUploadArray.add(matchJSON);
        }
        String matchesUploadString = matchesJSONUploadArray.toString();
        user.setProperty("matches", matchesUploadString);
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(getApplicationContext(), "Match started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<String> getFriendsfromBackendless() {
        ArrayList<String> friends = new ArrayList<>();
        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        JsonParser parser = new JsonParser();
        String friendsJSONString = currentUser.getProperty("friends").toString();
        if (friendsJSONString.equals("[]")) {
            Toast.makeText(getApplicationContext(), "You have no friends yet. Please add some.", Toast.LENGTH_LONG).show();
        } else {
            JsonArray friendsJSONArray = parser.parse(friendsJSONString).getAsJsonArray();
            for (JsonElement friendsJsonElement : friendsJSONArray) {
                JsonObject json = friendsJsonElement.getAsJsonObject();
                String friend = json.get("friend").getAsString();
                friends.add(friend);
            }
        }
        return friends;
    }

    private void addFriend(final ArrayList<String> currentUserFriendsAL, String searchUser) {

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        final BackendlessUser currentUser = Backendless.UserService.CurrentUser();

        String whereClause = "username= '" + searchUser + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
            @Override
            public void handleResponse(List<BackendlessUser> response) {
                if (response.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "This person does not have an account on this app! ASK THEM TO JOIN :D", Toast.LENGTH_SHORT).show();
                } else {
                    BackendlessUser newFriend = response.get(0); //get friend
                    String newFriendName = newFriend.getProperty("username").toString(); //get friend's name
                    String currentUserName = currentUser.getProperty("username").toString(); //get user's name
                    String newFriendFriends = newFriend.getProperty("friends").toString(); //get friend's friends

                    //get both User's friend's lists from the JSON String and convert them to arrayLists
                    ArrayList<String> newFriendFriendsAL=JSONConversion.getListFromJSONString(newFriendFriends);
                    //adding each other
                    newFriendFriendsAL.add(currentUserName);
                    currentUserFriendsAL.add(newFriendName);

                    //upload the JSONs and update users
                    uploadFriendsToBackendless(currentUserFriendsAL, currentUser);
                    uploadFriendsToBackendless(newFriendFriendsAL,newFriend);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), "Boom", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void uploadFriendsToBackendless(ArrayList<String> friendsAL, BackendlessUser user) {
        JsonArray friendsJSONUploadArray = new JsonArray();
        for (String friend : friendsAL) {
            JsonObject friendJSON = new JsonObject();
            friendJSON.addProperty("friend", friend);
            friendsJSONUploadArray.add(friendJSON);
        }
        String friendsUploadString = friendsJSONUploadArray.toString();
        user.setProperty("friends", friendsUploadString);
        Backendless.UserService.update(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(getApplicationContext(), "Friend added!", Toast.LENGTH_LONG).show();
                finish();
                Intent intent=new Intent(FriendsActivity.this, FriendsActivity.class);
                startActivity(intent);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTextStrings(TextView titleFriendsTV, TextView addFriendTV, TextView startSeshTV) {
        titleFriendsTV.setText("Friends");
        addFriendTV.setText("Add Friend");
        startSeshTV.setText("Start Sesh!");
    }

    private void setBackGroundColors(LinearLayout titleLL, LinearLayout addFriendLL, LinearLayout startSeshLL, Context context) {
        titleLL.setBackgroundColor(Color.RED);
        addFriendLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        startSeshLL.setBackgroundColor(Color.RED);
    }

    public void onBackPressed() {
        Intent goHome = new Intent(FriendsActivity.this, HomeActivity.class);
        startActivity(goHome);
    }
}
