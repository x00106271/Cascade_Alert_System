package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button loginButton;
    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "LoginActivity";

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
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // Listening to login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                if (emailText.equals("")) {
                    Toast.makeText(LoginActivity.this, "enter an email address",
                            Toast.LENGTH_LONG).show();
                } else if (passwordText.equals("")) {
                    Toast.makeText(LoginActivity.this, "enter your password",
                            Toast.LENGTH_LONG).show();
                } else {
                    BaseUser user = new BaseUser(emailText, passwordText);
                    mService.validateUser(user, new TableQueryCallback<BaseUser>() {

                        @Override
                        public void onCompleted(List<BaseUser> results, int count,
                                                Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                if (count == 1) {
                                    mService.setUserId(results.get(0).getId());
                                    mService.setAuthentication();
                                    Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainScreen);
                                } else {
                                    Toast.makeText(LoginActivity.this, "wrong login information!!try again...",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
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
}