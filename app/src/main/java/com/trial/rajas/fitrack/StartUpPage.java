package com.trial.rajas.fitrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by rajas on 10/18/2017.
 */

public class StartUpPage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);
        Context context=this;
        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);

        AsyncCallback<Boolean> loginValidityCallback = new AsyncCallback<Boolean>(){
            @Override
            public void handleResponse( Boolean response ){
                if(response==true){
                    String loggedIn=Backendless.UserService.loggedInUser();
                    Backendless.Data.of( BackendlessUser.class ).findById(loggedIn, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser currentUser) {
                            Backendless.UserService.setCurrentUser(currentUser);
                            Intent checkLogin= new Intent(StartUpPage.this, HomeActivity.class);
                            startActivity(checkLogin);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "Not logged in yet.", Toast.LENGTH_LONG).show();
            }
            @Override
            public void handleFault( BackendlessFault fault ){
                Toast.makeText(getApplicationContext(), "NO.", Toast.LENGTH_LONG).show();
            }
        };
        Backendless.UserService.isValidLogin(loginValidityCallback);

        LinearLayout loginTitleLL=(LinearLayout) findViewById(R.id.loginTitleLinearLayout);
        LinearLayout loginButtonLL=(LinearLayout) findViewById(R.id.loginButtonLinearLayout);
        LinearLayout facebookButtonLL=(LinearLayout) findViewById(R.id.facebookLinearLayout);
        LinearLayout googleButtonLL=(LinearLayout) findViewById(R.id.googleLinearLayout);
        LinearLayout signUpButtonLL=(LinearLayout) findViewById(R.id.signUpLinearLayout);

        TextView loginTitleTV= (TextView) findViewById(R.id.loginTitleTextView);
        TextView loginButtonTV= (TextView) findViewById(R.id.loginButtonTextView);
        TextView facebookTV= (TextView) findViewById(R.id.facebookTextView);
        TextView googleTV= (TextView) findViewById(R.id.googleTextView);
        TextView signUpTV= (TextView) findViewById(R.id.signUpTextView);

        ImageView logo= (ImageView) findViewById(R.id.logoID);

        loginTitleLL.setBackgroundColor(Color.RED);
        loginButtonLL.setBackgroundColor(Color.RED);
        facebookButtonLL.setBackgroundColor(ContextCompat.getColor(context, R.color.facebookBlue));
        googleButtonLL.setBackgroundColor(ContextCompat.getColor(context, R.color.googlePlusBlue));
        signUpButtonLL.setBackgroundColor(Color.RED);

        loginTitleTV.setText("Login/Sign Up");
        loginButtonTV.setText("Login to FiTrack");
        facebookTV.setText("Continue with FaceBook");
        googleTV.setText("Continue with Google");
        signUpTV.setText("Sign Up");

        loginButtonLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginPageIntent= new Intent(StartUpPage.this, Login.class);
                startActivity(loginPageIntent);
            }
        });

        signUpButtonLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginPageIntent= new Intent(StartUpPage.this, SignUp.class);
                startActivity(loginPageIntent);
            }
        });

    }

}