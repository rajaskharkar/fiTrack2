package com.trial.rajas.fitrack;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity{

    public ArrayList<String> taskList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity2);
        Context context=this;

        LinearLayout addLinearLayout=InitializeLayouts.initializeLinearLayout(findViewById(R.id.addLinearLayout));
        LinearLayout viewLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.viewLinearLayout));
        LinearLayout titleLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.titleSpace));
        LinearLayout selectActivityLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.selectActivityLinearLayout));
        LinearLayout blankLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.blankSpace));
        LinearLayout pleaseAddLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.pleaseAddLinearLayout));
        LinearLayout saveActivityLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.saveActivityLinearLayout));
        LinearLayout currentScoreLL=InitializeLayouts.initializeLinearLayout(findViewById(R.id.currentScoreLinearLayout));
        LinearLayout enterActivityLinearLayout=InitializeLayouts.initializeLinearLayout(findViewById(R.id.enterActivityLinearLayout));

        TextView titleTextView= InitializeLayouts.initializeTextView(findViewById(R.id.titleText));
        TextView pleaseAddTextView= InitializeLayouts.initializeTextView(findViewById(R.id.pleaseAddTextView));
        TextView saveActivityTextView= InitializeLayouts.initializeTextView(findViewById(R.id.saveActivityTextView));
        TextView currentScoreTextView= InitializeLayouts.initializeTextView(findViewById(R.id.currentScoreTextView));
        TextView viewTextView= InitializeLayouts.initializeTextView(findViewById(R.id.viewTextView));
        TextView addActivityTextView= InitializeLayouts.initializeTextView(findViewById(R.id.addActivityTextView));

        Spinner spin= (Spinner) findViewById(R.id.spin);

        ArrayList<String> taskList=new ArrayList<>();
        ArrayList<Integer> scoreList= new ArrayList<>();
        ArrayList<Activity> activityList= new ArrayList<>();

        fillActivityList(activityList);
        addToTaskAndScoreList(activityList, taskList, scoreList);

        SpinnerCustomAdapter spinnerCustomAdapter= new SpinnerCustomAdapter(this, R.layout.row, activityList);
        spin.setAdapter(spinnerCustomAdapter);

        setBackGroundColors(titleLL, pleaseAddLL, currentScoreLL, saveActivityLL, addLinearLayout, viewLL, context);
        setTextStrings(titleTextView, pleaseAddTextView, currentScoreTextView, saveActivityTextView, addActivityTextView, viewTextView);
    }

    private void setTextStrings(TextView titleTextView, TextView pleaseAddTextView, TextView currentScoreTextView, TextView saveActivityTextView, TextView addActivityTextView, TextView viewTextView) {
        titleTextView.setText("Add Activity");
        pleaseAddTextView.setText("Add activity or select: ");
        currentScoreTextView.setText("Current Score: X");
        saveActivityTextView.setText("Save Activity");
        addActivityTextView.setText("Add!");
        viewTextView.setText("View log");
    }

    private void setBackGroundColors(LinearLayout titleLL, LinearLayout pleaseAddLL, LinearLayout currentScoreLL, LinearLayout saveActivityLL, LinearLayout addLinearLayout, LinearLayout viewLL, Context context) {
        titleLL.setBackgroundColor(Color.RED);
        pleaseAddLL.setBackgroundColor(ContextCompat.getColor(context, R.color.DarkRed));
        currentScoreLL.setBackgroundColor(Color.RED);
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
        activityList.add(new Activity("Go to the gym", 50, "pos"));
        activityList.add(new Activity("Eat a pizza", 25, "neg"));
        activityList.add(new Activity("Go for a run", 30, "pos"));
    }
}