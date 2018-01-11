package com.trial.rajas.fitrack;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rajas on 11/16/2017.
 */

public class ViewDishesAdapter extends ArrayAdapter<String>{

    private ArrayList<String> dishList;

    public ViewDishesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> dishList) {
        super(context, resource, dishList);
        this.dishList = dishList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_dishes_row, parent, false);
        TextView dishTV= (TextView) view.findViewById(R.id.dishNameTextView);
        final String dishName=dishList.get(position);
        dishTV.setText(dishName);
        dishTV.setTextColor(Color.BLACK);
        dishTV.setTextSize(24);
        return view;
    }
}
