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

public class FriendsListAdapter extends ArrayAdapter<String>{

    private ArrayList<String> friendsList;
    public ArrayList<String> updatedFriendsList=friendsList;

    public FriendsListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<String> friendsList) {
        super(context, resource, friendsList);
        this.friendsList = friendsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friends_list_row, parent, false);
        TextView friend= (TextView) view.findViewById(R.id.friendName);
        final String friendName=friendsList.get(position);
        friend.setText(friendName);
        friend.setTextColor(Color.BLACK);
        friend.setTextSize(24);

        ImageView removeFriend=(ImageView) view.findViewById(R.id.removeFriend);

        removeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getContext());
                alert.setTitle("Remove Friend");
                alert.setMessage("Are you sure you want to remove "+friendName+" as your friend?");
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
