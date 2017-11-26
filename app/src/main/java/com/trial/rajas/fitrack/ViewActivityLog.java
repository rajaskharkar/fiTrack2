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

public class ViewActivityLog extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity_log);
        final Context context = this;
        LinearLayout titleLL = InitializeLayouts.initializeLinearLayout(findViewById(R.id.viewActivityLogTitleLL));
        TextView titleTV = InitializeLayouts.initializeTextView(findViewById(R.id.viewActivityLogTitleTV));
        ListView activityLV=(ListView) findViewById(R.id.viewActivityLogListView);

        titleTV.setText("Your Activity Log");
        titleLL.setBackgroundColor(Color.RED);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        BackendlessUser currentUser = Backendless.UserService.CurrentUser();

        String activityLogString=currentUser.getProperty("activity_log").toString();
        ArrayList<Activity> activityArrayList= JSONConversion.getListFromJSONStringForActivity(activityLogString);
        ViewActivityLogAdapter activityListAdapter = new ViewActivityLogAdapter(this, R.layout.row, activityArrayList);
        activityLV.setAdapter(activityListAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent goBack= new Intent(ViewActivityLog.this, AddActivity.class);
        startActivity(goBack);
    }
}
