package com.cascadealertsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class Cascade extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        // bring user to login screen
        Intent loginScreen = new Intent(getApplicationContext(), activities.LoginActivity.class);
        startActivity(loginScreen);
        finish();
    }

}
