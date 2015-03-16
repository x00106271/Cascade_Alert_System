package com.cascadealertsystem;

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
import Model.BaseUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;


public class LoginActivity extends Activity {

    private MobileServiceTable<BaseUser> mTable;
    private String emailText, passwordText;
    private EditText email, password;
    private TextView registerScreen, forgotPassword;
    private Button loginButton;

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
            mTable = mClient.getTable(BaseUser.class);
        } catch (MalformedURLException e) {
            Toast.makeText(LoginActivity.this, "error loading cascade alert system", Toast.LENGTH_LONG).show();
        }

        // Listening to login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                if(emailText.equals("")) {
                    Toast.makeText(LoginActivity.this, "enter an email address",
                            Toast.LENGTH_LONG).show();
                }
                else if(passwordText.equals("")){
                    Toast.makeText(LoginActivity.this, "enter your password",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    validateUser(emailText,passwordText);
                }
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent regScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regScreen);
                finish();
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
    public void validateUser(String email,String pass) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... emails) {
                String retPassword="";
                String email = emails[0];
                try {
                    List<BaseUser> results =mTable.where().field("email").eq(email).execute().get();
                    if(results==null){
                        // do nothing
                    }
                    else{
                        retPassword=results.get(0).getPassword();
                    }
                } catch (Exception e) {

                }
                return retPassword;
            }
            protected void onPostExecute(String pass){
                if(pass.equals(passwordText)){
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainScreen);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "password is incorrect!!",
                    Toast.LENGTH_LONG).show();
                }
            }
        }.execute(email);
    }
}

