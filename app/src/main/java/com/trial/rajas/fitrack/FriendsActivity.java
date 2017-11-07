package com.trial.rajas.fitrack;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendsActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_activity);
        Context context=this;
        LinearLayout titleLL= InitializeLayouts.initializeLinearLayout(findViewById(R.id.titleFriends));
        LinearLayout addFriendLL= InitializeLayouts.initializeLinearLayout(findViewById(R.id.addFriendLinearLayout));
        LinearLayout startSeshLL= InitializeLayouts.initializeLinearLayout(findViewById(R.id.startSeshLinearLayout));
        TextView titleFriendsTV= InitializeLayouts.initializeTextView(findViewById(R.id.titleFriendsTextView));
        TextView addFriendTV= InitializeLayouts.initializeTextView(findViewById(R.id.addFriendTextView));
        TextView startSeshTV= InitializeLayouts.initializeTextView(findViewById(R.id.startSeshTextView));
        setBackGroundColors(titleLL, addFriendLL, startSeshLL, context);
        setTextStrings(titleFriendsTV, addFriendTV, startSeshTV);
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
}
