package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;

import models.BaseUser;
import services.MobileService;
import services.MobileServiceApp;


public class LoginActivity extends Activity {

    private String emailText, passwordText;
    private EditText email, password;
    private TextView registerScreen, forgotPassword;
    private ImageButton loginButton;
    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        registerScreen = (TextView) findViewById(R.id.link_to_register);
        forgotPassword = (TextView) findViewById(R.id.link_to_forgot_login);
        loginButton = (ImageButton) findViewById(R.id.btnLogin);
        email = (EditText) findViewById(R.id.email_login);
        password = (EditText) findViewById(R.id.password_login);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // Listening to login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                login();
            }
        });

        // keyboards log in on done button
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    handled = true;
                }
                return handled;
            }
        });

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                registerScreen.setEnabled(false);
                // Switching to Register screen
                Intent regScreen = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regScreen);
                finish();
            }
        });

        // listening to forgot password link
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                forgotPassword.setEnabled(false);
                emailText = email.getText().toString().trim();
                if (emailText.equals("")) {
                    Toast.makeText(LoginActivity.this, "You must enter an email address for us to find your lost password",
                            Toast.LENGTH_LONG).show();
                    forgotPassword.setEnabled(true);
                } else {
                    mService.checkEmail(emailText, new TableQueryCallback<BaseUser>() {

                        @Override
                        public void onCompleted(List<BaseUser> results, int count,
                                                Exception exception, ServiceFilterResponse response) {
                            if (count == 0) {
                                Toast.makeText(LoginActivity.this, "That email address does not exist in the system",
                                        Toast.LENGTH_LONG).show();
                                forgotPassword.setEnabled(true);
                            } else {
                                Toast.makeText(LoginActivity.this, "An email has been sent to your email address with your password",
                                        Toast.LENGTH_LONG).show();
                                forgotPassword.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });
    }

    // log in method
    public void login() {
        emailText = email.getText().toString().trim();
        passwordText = password.getText().toString().trim();
        if (emailText.equals("")) {
            Toast.makeText(LoginActivity.this, "enter an email address",
                    Toast.LENGTH_LONG).show();
        } else if (passwordText.equals("")) {
            Toast.makeText(LoginActivity.this, "enter your password",
                    Toast.LENGTH_LONG).show();
        } else {
            loginButton.setEnabled(false);
            BaseUser user = new BaseUser(emailText, passwordText);
            mService.validateUser(user, new TableQueryCallback<BaseUser>() {

                @Override
                public void onCompleted(List<BaseUser> results, int count,
                                        Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        if (count == 1) {
                            if (results.get(0).isVerified()) {
                                // get user area id
                                mService.setAreaId(results.get(0).getId());
                                // get user id and set authentication
                                mService.setUserId(results.get(0).getId());
                                // start main screen
                                Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(mainScreen);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "sorry you have not yet been verified by the administrator!",
                                        Toast.LENGTH_LONG).show();
                                loginButton.setEnabled(true);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "wrong login information!!try again...",
                                    Toast.LENGTH_LONG).show();
                            loginButton.setEnabled(true);
                        }
                    }
                }
            });
        }
    }
}