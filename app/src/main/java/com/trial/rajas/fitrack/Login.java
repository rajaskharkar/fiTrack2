package com.trial.rajas.fitrack;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by rajas on 10/31/2017.
 */

public class Login extends AppCompatActivity {

    AsyncCallback<BackendlessUser> loginCallBackActual= new AsyncCallback<BackendlessUser>() {
        @Override
        public void handleResponse(BackendlessUser response) {
            Toast.makeText(getApplicationContext(), "Successfully logged in!", Toast.LENGTH_SHORT).show();
            Intent goHome= new Intent(Login.this, HomeActivity.class);
            startActivity(goHome);
        }

        @Override
        public void handleFault(BackendlessFault fault) {
            String exception= fault.getCode().toString();
            if(exception.equals("IllegalArgumentException"))
                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            else if(exception.equals("3003"))
                Toast.makeText(getApplicationContext(), "Enter the right password and/or username!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), exception, Toast.LENGTH_LONG).show();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        LinearLayout userNameLL= (LinearLayout) findViewById(R.id.userNameLinearLayout);
        LinearLayout passwordLL= (LinearLayout) findViewById(R.id.passwordLinearLayout);
        LinearLayout login2LL= (LinearLayout) findViewById(R.id.loginTitle2LinearLayout);
        final LinearLayout loginFinalLL= (LinearLayout) findViewById(R.id.loginFinalLinearLayout);

        TextView userNameTV= (TextView) findViewById(R.id.userNameTextView);
        TextView passwordTV= (TextView) findViewById(R.id.passwordTextView);
        TextView login2TV= (TextView) findViewById(R.id.loginTitle2TextView);
        TextView loginFinalTV= (TextView) findViewById(R.id.loginFinalTextView);

        final EditText userNameET= (EditText) findViewById(R.id.userNameEditText);
        final EditText passwordET= (EditText) findViewById(R.id.passwordEditText);

        setTextStrings(userNameTV, passwordTV, login2TV, loginFinalTV);
        setTextHints(userNameET, passwordET);
        setTextBlack(userNameTV, passwordTV, userNameET, passwordET);
        setBackgroundColors(login2LL, loginFinalLL);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);
        String username= userNameET.getEditableText().toString();
        String password= passwordET.getEditableText().toString();



        loginFinalLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginFinalLL.setClickable(false);
                String username= userNameET.getEditableText().toString();
                String password= passwordET.getEditableText().toString();
                Backendless.UserService.login(username, password, loginCallBackActual, true);

            }
        });
    }

    private void setBackgroundColors(LinearLayout login2LL, LinearLayout loginFinalLL) {
        login2LL.setBackgroundColor(Color.RED);
        loginFinalLL.setBackgroundColor(Color.RED);
    }

    private void setTextHints(EditText userNameET, EditText passwordET) {
        userNameET.setHint("User Name");
        passwordET.setHint("Password");
    }

    private void setTextBlack(TextView userNameTV, TextView passwordTV, EditText userNameET, EditText passwordET) {
        userNameTV.setTextColor(Color.BLACK);
        passwordTV.setTextColor(Color.BLACK);
        userNameET.setTextColor(Color.BLACK);
        passwordET.setTextColor(Color.BLACK);
    }

    private void setTextStrings(TextView userNameTV, TextView passwordTV, TextView login2TV, TextView loginFinalTV) {
        userNameTV.setText("Enter user name: ");
        passwordTV.setText("Enter password: ");
        login2TV.setText("Credentials");
        loginFinalTV.setText("Login!");
    }
}
