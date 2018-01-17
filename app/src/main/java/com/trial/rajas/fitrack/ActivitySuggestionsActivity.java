package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;

/**
 * Created by rajas on 11/21/2017.
 */

public class ActivitySuggestionsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        final Context context = this;
        LinearLayout titleLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.activitySuggestionsTitleLL));
        LinearLayout commentLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.commentLL));
        TextView titleTV = InitializeLayouts.initializeTextView(findViewById(R.id.activitySuggestionsTitleTV));
        TextView commentTV = InitializeLayouts.initializeTextView(findViewById(R.id.commentTV));
        ListView activityLV=(ListView) findViewById(R.id.activitySuggestionsListView);

        titleTV.setText("Suggested Activities");
        titleLL.setBackgroundColor(Color.RED);
        commentLL.setBackgroundColor(Color.RED);

        String commentString = getIntent().getStringExtra("comment");
        commentTV.setText(commentString);
        String dataString = getIntent().getStringExtra("data");

        ArrayList<Activity> activityArrayList= JSONConversion.getListFromJSONStringForActivity(dataString);
        ViewActivityLogAdapter activityListAdapter = new ViewActivityLogAdapter(this, R.layout.row, activityArrayList);
        activityLV.setAdapter(activityListAdapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivitySuggestionsActivity.this, HomeActivity.class));
        finish();
    }
}
