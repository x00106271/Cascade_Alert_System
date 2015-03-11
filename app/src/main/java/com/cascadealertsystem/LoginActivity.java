package com.cascadealertsystem;
// THIS FILE BELOW MUST BE ON THE JAVA FILE WHO RUNS THE LAUNCH ACTIVITY
import com.microsoft.windowsazure.mobileservices.*;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import java.net.MalformedURLException;
import java.util.List;
import Model.User;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import static com.microsoft.windowsazure.mobileservices.table.query.QueryOperations.*;


public class LoginActivity extends Activity {

    private MobileServiceTable<User> mTable;
    String emailText, passwordText;
    EditText email, password;
    TextView registerScreen, forgotPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        registerScreen = (TextView) findViewById(R.id.link_to_register);
        forgotPassword = (TextView) findViewById(R.id.link_to_forgot_login);
        loginButton = (Button) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.email_login);
        password = (EditText) findViewById(R.id.password_login);

        // for mobile services
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            MobileServiceClient mClient = new MobileServiceClient(
                    "https://cascade.azure-mobile.net/",
                    "TVkGQDWdaeNNbNKirTHAknOUmHqWgu76",
                    this);
            mTable = mClient.getTable("User",User.class);
        } catch (MalformedURLException e) {
            Toast.makeText(LoginActivity.this, "error loading mobile services", Toast.LENGTH_LONG).show();
        }

        // Listening to login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                if (!emailText.equals("") || !passwordText.equals("")) {
                    validateUser(emailText);
                } else {
                    Toast.makeText(LoginActivity.this, "Enter your email address and password",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent regScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regScreen);
            }
        });

        // listening to forgot password link
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Toast.makeText(LoginActivity.this, "An email has been sent to your email address with your password",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // validate the user

    public void validateUser(String email) {
        /*new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... emails) {
                String retPassword="";
                String email = emails[0];
                try {
                    List<User> results =mTable.execute().get();
                    for(User user:results){
                        if(user.getEmail().equals(email)){
                            retPassword=user.getPassword();
                        }
                    }
                } catch (Exception e) {
                        retPassword=e.getMessage();
                }
                return retPassword;
            }
            protected void onPostExecute(String pass){
                Toast.makeText(LoginActivity.this,pass,
                        Toast.LENGTH_LONG).show();
                if(pass.equals(passwordText)){
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainScreen);
                    finish();
                }
                else{
                    //Toast.makeText(LoginActivity.this, "password is incorrect!!",
                            //Toast.LENGTH_LONG).show();
                }

            }
        }.execute(email);

    }
}*/
        Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainScreen);
        finish();
    }}

