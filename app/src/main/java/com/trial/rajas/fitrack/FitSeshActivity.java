package com.trial.rajas.fitrack;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by rajas on 10/12/2017.
 */

public class FitSeshActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitsesh_activity);
        Context context=this;

        LinearLayout titleLL=(LinearLayout) findViewById(R.id.fitSeshTitleLinearLayout);
        LinearLayout versusLL=(LinearLayout) findViewById(R.id.versusLinearLayout);
        LinearLayout optionsLL=(LinearLayout) findViewById(R.id.optionsLinearLayout);
        LinearLayout playerActivityLL=(LinearLayout) findViewById(R.id.playerActivityLinearLayout);
        LinearLayout allActivityLL=(LinearLayout) findViewById(R.id.allActivityLinearLayout);

        TextView titleTV=(TextView) findViewById(R.id.titleFitSeshTextView);
        TextView versusTV=(TextView) findViewById(R.id.versusTextView);
        TextView p1NameTV=(TextView) findViewById(R.id.p1NameTextView);
        TextView p1ScoreTV=(TextView) findViewById(R.id.p1ScoreTextView);
        TextView p2NameTV=(TextView) findViewById(R.id.p2NameTextView);
        TextView p2ScoreTV=(TextView) findViewById(R.id.p2ScoreTextView);
        TextView resetTV=(TextView) findViewById(R.id.resetTextView);
        TextView endTV=(TextView) findViewById(R.id.endTextView);
        TextView extendTV=(TextView) findViewById(R.id.extendTextView);
        TextView p1ActivityTV=(TextView) findViewById(R.id.p1ActivityTextView);
        TextView p2ActivityTV=(TextView) findViewById(R.id.p2ActivityTextView);
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

        titleTV.setText("FitSesh");
        versusTV.setText("vs. Person");
        p1NameTV.setText("Player 1");
        p1ScoreTV.setText("100");
        p2NameTV.setText("Player 2");
        p2ScoreTV.setText("200");
        resetTV.setText("Reset");
        endTV.setText("End");
        extendTV.setText("Extend");
        p1ActivityTV.setText("Player 1 Log");
        p2ActivityTV.setText("Player 2 Log");
        allActivityTV.setText("All Activities Log");

    }
}
