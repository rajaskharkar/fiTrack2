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
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajas on 11/25/2017.
 */

public class MyMatches extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_matches_layout);
        Context context = this;
        LinearLayout titleLL= InitializeLayouts.initializeLinearLayout(findViewById(R.id.titleMyMatchesLL));
        LinearLayout bottomLL= InitializeLayouts.initializeLinearLayout(findViewById(R.id.myMatchesBottomBlankLL));
        TextView titleTV= InitializeLayouts.initializeTextView(findViewById(R.id.titleMyMatchesTV));
        ListView matchesLV= (ListView) findViewById(R.id.myMatchesListView);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser= Backendless.UserService.CurrentUser();
        String currentUserMatches= currentUser.getProperty("matches").toString();
        final ArrayList<String> matchesAL=JSONConversion.getMatchListFromJSONString(currentUserMatches);
        String currentUserName= currentUser.getProperty("username").toString();

        final MyMatchesAdapter myMatchesAdapter = new MyMatchesAdapter(this, R.layout.my_matches_row, matchesAL);
        matchesLV.setAdapter(myMatchesAdapter);

        titleLL.setBackgroundColor(Color.RED);
        bottomLL.setBackgroundColor(Color.RED);
        titleTV.setText(currentUserName+"'s Matches");

        matchesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String opponentName=matchesAL.get(position);
                String whereClause = "username= '" +opponentName+ "'";
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(whereClause);

                Backendless.Data.of(BackendlessUser.class).find(queryBuilder, new AsyncCallback<List<BackendlessUser>>() {
                    @Override
                    public void handleResponse(List<BackendlessUser> response) {
                        if (response.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "This person does not have an account on this app! ASK THEM TO JOIN :D", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent goToFitSesh= new Intent(MyMatches.this, FitSeshActivity.class);
                            goToFitSesh.putExtra("friend", opponentName);
                            goToFitSesh.putExtra("previous_activity", "MyMatches");
                            startActivity(goToFitSesh);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goHome=new Intent(MyMatches.this,HomeActivity.class);
        startActivity(goHome);
        finish();
    }
}
