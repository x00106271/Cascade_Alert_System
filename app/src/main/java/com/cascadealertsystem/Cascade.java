package com.cascadealertsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import activities.LoginActivity;
import activities.MainActivity;
import services.MobileService;
import services.MobileServiceApp;


public class Cascade extends Activity {

    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "OpeningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // if authenticated go to main otherwise go to login
        /*if(mService.isUserAuthenticated()){
            Intent mainScreen = new Intent(Cascade.this, MainActivity.class);
            startActivity(mainScreen);
            finish();
        }
        else{
            Intent loginScreen = new Intent(Cascade.this, LoginActivity.class);
            startActivity(loginScreen);
            finish();
        }*/
        Intent loginScreen = new Intent(Cascade.this, LoginActivity.class);
        startActivity(loginScreen);
        finish();

    }

}
