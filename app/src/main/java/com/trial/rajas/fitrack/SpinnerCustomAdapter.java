package com.trial.rajas.fitrack;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rajas on 10/16/17.
 */

public class SpinnerCustomAdapter extends ArrayAdapter<Activity> {

    private ArrayList<Activity> activityArrayList;

    public SpinnerCustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Activity> activityArrayList) {
        super(context, resource, activityArrayList);
        this.activityArrayList = activityArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        LinearLayout viewLL= (LinearLayout) view.findViewById(R.id.spinnerLayout);
        TextView taskTextView = (TextView) view.findViewById(R.id.taskSpinnerTextView);
        TextView scoreTextView = (TextView) view.findViewById(R.id.scoreSpinnerTextView);
        Activity activity = activityArrayList.get(position);
        taskTextView.setText(activity.getTask());
        scoreTextView.setText(activity.getScore()+"");
        taskTextView.setTextSize(18);
        scoreTextView.setTextSize(18);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, parent);
    }
}
