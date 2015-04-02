package com.cascadealertsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import activities.LoginActivity;
import activities.MainActivity;
import services.MobileService;
import services.MobileServiceApp;


public class Cascade extends Activity {

    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "OpeningActivity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

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

        // check device has google play services
        checkPlayServices();

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
