package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by rajas on 10/12/2017.
 */

public class FitSeshActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitsesh_activity);
        Context context=this;

        final String opponentName= getIntent().getStringExtra("friend");
        if (opponentName != null) {
            Toast.makeText(getApplicationContext(), "Works: "+opponentName, Toast.LENGTH_SHORT).show();
        }

        LinearLayout titleLL=(LinearLayout) findViewById(R.id.fitSeshTitleLinearLayout);
        LinearLayout versusLL=(LinearLayout) findViewById(R.id.versusLinearLayout);
        LinearLayout optionsLL=(LinearLayout) findViewById(R.id.optionsLinearLayout);
        LinearLayout playerActivityLL=(LinearLayout) findViewById(R.id.playerActivityLinearLayout);
        LinearLayout allActivityLL=(LinearLayout) findViewById(R.id.allActivityLinearLayout);

        TextView titleTV=(TextView) findViewById(R.id.titleFitSeshTextView);
        final TextView versusTV=(TextView) findViewById(R.id.versusTextView);
        final TextView p1NameTV=(TextView) findViewById(R.id.p1NameTextView);
        final TextView p1ScoreTV=(TextView) findViewById(R.id.p1ScoreTextView);
        final TextView p2NameTV=(TextView) findViewById(R.id.p2NameTextView);
        final TextView p2ScoreTV=(TextView) findViewById(R.id.p2ScoreTextView);
        TextView resetTV=(TextView) findViewById(R.id.resetTextView);
        TextView endTV=(TextView) findViewById(R.id.endTextView);
        TextView extendTV=(TextView) findViewById(R.id.extendTextView);
        final TextView p1ActivityTV=(TextView) findViewById(R.id.p1ActivityTextView);
        final TextView p2ActivityTV=(TextView) findViewById(R.id.p2ActivityTextView);
        TextView allActivityTV=(TextView) findViewById(R.id.allActivityTextView);

        versusLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));

        versusTV.setTextSize(18);
        p1NameTV.setTextSize(26);
        p1ScoreTV.setTextSize(22);
        p2NameTV.setTextSize(26);
        p2ScoreTV.setTextSize(22);
        p1ActivityTV.setTextSize(26);
        p2ActivityTV.setTextSize(26);

        titleLL.setBackgroundColor(Color.RED);
        optionsLL.setBackgroundColor(Color.RED);
        playerActivityLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        allActivityLL.setBackgroundColor(Color.RED);
        p1NameTV.setTextColor(Color.BLACK);
        p1ScoreTV.setTextColor(Color.BLACK);
        p2NameTV.setTextColor(Color.BLACK);
        p2ScoreTV.setTextColor(Color.BLACK);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser= Backendless.UserService.CurrentUser();
        String currentUserName= currentUser.getProperty("username").toString();

        String whereClause = "(player1_name= '"+currentUserName+"' or player1_name= '"+opponentName+"') and (player2_name= '"+currentUserName+"' or player2_name= '"+opponentName+"')";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of("Matches").find(queryBuilder, new AsyncCallback<List<Map>>() {
            @Override
            public void handleResponse(List<Map> matchList) {
                Map match=matchList.get(0);
                String player1_name= match.get("player1_name").toString();
                String player2_name= match.get("player2_name").toString();
                String player1_score= match.get("player1_score").toString();
                String player2_score= match.get("player2_score").toString();
                versusTV.setText("vs. ");
                p1NameTV.setText(player1_name);
                p1ScoreTV.setText(player1_score);
                p2NameTV.setText(player2_name);
                p2ScoreTV.setText(player2_score);
                p1ActivityTV.setText(player1_name+"'s Log");
                p2ActivityTV.setText(player2_name+"'s Log");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //What we're looking for: (currentUserName || opponent) && (currentUserName || opponent)
        titleTV.setText("Match");
        resetTV.setText("Reset");
        endTV.setText("End");
        extendTV.setText("Extend");
        allActivityTV.setText("All Activities Log");

    }

    @Override
    public void onBackPressed() {
        String value= getIntent().getStringExtra("previous_activity");
        if(value.equals("FriendsActivity")){
            Intent intent=new Intent(FitSeshActivity.this, FriendsActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent=new Intent(FitSeshActivity.this, MyMatches.class);
            startActivity(intent);
            finish();
        }
    }
}
