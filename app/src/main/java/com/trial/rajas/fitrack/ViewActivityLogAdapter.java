package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rajas on 11/21/2017.
 */

public class ViewActivityLogAdapter extends ArrayAdapter<Activity> {
    private final ArrayList<Activity> activityList;

    public ViewActivityLogAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Activity> activityList) {
        super(context, resource, activityList);
        this.activityList = activityList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        TextView taskTextView = (TextView) view.findViewById(R.id.taskSpinnerTextView);
        TextView scoreTextView = (TextView) view.findViewById(R.id.scoreSpinnerTextView);
        TextView signTextView = (TextView) view.findViewById(R.id.signSpinnerTextView);

        String task=activityList.get(position).getTask();
        String sign=activityList.get(position).getSign();
        String score=activityList.get(position).getScore().toString();

        setTextSpecifications(task, sign, score, taskTextView, signTextView, scoreTextView);

        return view;
    }

    private void setTextSpecifications(String task, String sign, String score, TextView taskTextView, TextView signTextView, TextView scoreTextView) {
        taskTextView.setText(task);
        signTextView.setText(sign);
        scoreTextView.setText(score);

        taskTextView.setTextColor(Color.BLACK);
        signTextView.setTextColor(Color.BLACK);
        scoreTextView.setTextColor(Color.BLACK);

        taskTextView.setTextSize(25);
        signTextView.setTextSize(25);
        scoreTextView.setTextSize(25);
    }
}
