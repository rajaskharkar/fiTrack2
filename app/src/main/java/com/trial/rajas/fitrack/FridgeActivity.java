package com.trial.rajas.fitrack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by rajas on 10/12/2017.
 */

public class FridgeActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_activity);
        TextView proof=(TextView) findViewById(R.id.proofFriends);
        proof.setText("The Fridge works!");

    }
}
