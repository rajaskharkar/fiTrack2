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

public class MyMatchesAdapter extends ArrayAdapter<String>{

    private ArrayList<String> opponentList;

    public MyMatchesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> opponentList) {
        super(context, resource, opponentList);
        this.opponentList = opponentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_matches_row, parent, false);
        TextView opponent= (TextView) view.findViewById(R.id.opponentName);
        final String opponentName=opponentList.get(position);
        opponent.setText(opponentName);
        opponent.setTextColor(Color.BLACK);
        opponent.setTextSize(24);

        ImageView removeFriend=(ImageView) view.findViewById(R.id.removeOpponent);

        removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getContext());
                alert.setTitle("Remove Friend");
                alert.setMessage("Are you sure you want to delete your match with "+opponentName+"?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        });
        return view;
    }
}
