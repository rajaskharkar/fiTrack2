package com.trial.rajas.fitrack;

import android.app.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.gson.JsonArray;

import java.util.ArrayList;

import static weborb.util.ThreadContext.context;

/**
 * Created by rajas on 11/16/2017.
 */

public class FridgeListAdapter extends ArrayAdapter<Ingredient>{

    private ArrayList<Ingredient> ingredientList;

    public FridgeListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Ingredient> ingredientList) {
        super(context, resource, ingredientList);
        this.ingredientList = ingredientList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(final int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fridge_list_row, parent, false);
        TextView name= (TextView) view.findViewById(R.id.ingredientRowNameTextView);
        TextView quantity= (TextView) view.findViewById(R.id.ingredientRowQuantityTextView);
        TextView unit= (TextView) view.findViewById(R.id.ingredientRowUnitTextView);
        ImageView remove=(ImageView) view.findViewById(R.id.removeIngredientRow);
        final Ingredient ingredient=ingredientList.get(position);
        name.setText(ingredient.name);
        quantity.setText(ingredient.quantity.toString());
        unit.setText(ingredient.unit);

        name.setTextSize(24);
        quantity.setTextSize(24);
        unit.setTextSize(24);
        name.setTextColor(Color.BLACK);
        quantity.setTextColor(Color.BLACK);
        unit.setTextColor(Color.BLACK);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendlessUser currentUser=Backendless.UserService.CurrentUser();
                final Context context=getContext();
                String fridgeDataString=currentUser.getProperty("fridge").toString();
                JsonArray ingredientJsonArray= JSONConversion.getFridgeJsonArrayFromJSONString(fridgeDataString);
                ingredientJsonArray.remove(position);
                String uploadString= ingredientJsonArray.toString();
                currentUser.setProperty("fridge", uploadString);
                Backendless.UserService.update(currentUser, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                        Intent reload= new Intent(context, FridgeActivity.class);
                        context.startActivity(reload);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(context, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}