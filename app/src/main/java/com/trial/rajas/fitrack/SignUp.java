package com.trial.rajas.fitrack;

import android.content.Context;
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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by rajas on 10/31/2017.
 */

public class SignUp extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        Context context=this;

        LinearLayout signUpTitleLL= (LinearLayout) findViewById(R.id.signUpTitleLinearLayout);
        LinearLayout userNameLL= (LinearLayout) findViewById(R.id.userNameSignUpLinearLayout);
        LinearLayout passwordLL= (LinearLayout) findViewById(R.id.passwordSignUpLinearLayout);
        LinearLayout confirmPasswordLL= (LinearLayout) findViewById(R.id.signUpTitleLinearLayout);
        LinearLayout signUpFinalLL= (LinearLayout) findViewById(R.id.signUpFinalLinearLayout);

        TextView signUpTitleTV= (TextView) findViewById(R.id.signUpTitleTextView);
        TextView userNameSignUpTV= (TextView) findViewById(R.id.userNameSignUpTextView);
        TextView passwordSignUpTV= (TextView) findViewById(R.id.passwordSignUpTextView);
        TextView confirmPasswordSignUpTV= (TextView) findViewById(R.id.confirmPasswordSignUpTextView);
        TextView signUpFinalTV= (TextView) findViewById(R.id.signUpFinalTextView);

        signUpTitleTV.setText("Sign Up");
        userNameSignUpTV.setText("Enter UserName: ");
        passwordSignUpTV.setText("Set Password: ");
        confirmPasswordSignUpTV.setText("Confirm Password: ");
        signUpFinalTV.setText("Sign Up!");

        final EditText userNameSignUpET= (EditText) findViewById(R.id.userNameSignUpEditText);
        final EditText passwordSignUpET= (EditText) findViewById(R.id.passwordSignUpEditText);
        final EditText confirmPasswordSignUpET= (EditText) findViewById(R.id.confirmPasswordSignUpEditText);

        userNameSignUpET.setHint("User Name");
        passwordSignUpET.setHint("Password");
        confirmPasswordSignUpET.setHint("Enter Password again");

        signUpTitleLL.setBackgroundColor(Color.RED);
        signUpFinalLL.setBackgroundColor(Color.RED);

        userNameSignUpTV.setTextColor(Color.BLACK);
        passwordSignUpTV.setTextColor(Color.BLACK);
        confirmPasswordSignUpTV.setTextColor(Color.BLACK);

        Backendless.initApp(this, BackendlessCredentials.APP_ID, BackendlessCredentials.SECRET_KEY);

        signUpFinalLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String username=userNameSignUpET.getEditableText().toString();
                final String password=passwordSignUpET.getEditableText().toString();
                String confirmPassword=confirmPasswordSignUpET.getEditableText().toString();
                final Integer score=0;
                //Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
                if(!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(), "Passwords don't match! Please make sure the passwords entered are the same.", Toast.LENGTH_LONG).show();
                }
                else{
                    //create new account
                    BackendlessUser user= new BackendlessUser();

                    //save username and password as user data
                    user.setProperty("username", username);
                    user.setPassword(password);
                    user.setProperty("score", score);
                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText(getApplicationContext(), "You registered!", Toast.LENGTH_LONG).show();
                            User newUser=new User();
                            newUser.createNewUser(username, password, score);
                            Intent goHome= new Intent(SignUp.this, HomeActivity.class);
                            startActivity(goHome);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(getApplicationContext(), "Registration failed.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
