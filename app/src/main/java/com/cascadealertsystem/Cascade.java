package com.cascadealertsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.ref.WeakReference;

import activities.LoginActivity;
import activities.MainActivity;
import services.MobileService;
import services.MobileServiceApp;


public class Cascade extends Activity {

    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "OpeningActivity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private WifiManager wifiManager;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* create a full screen window */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opening_screen);

        // create background image
        ImageView iv_background = (ImageView) findViewById(R.id.iv_background);
        BitmapWorkerTask task = new BitmapWorkerTask(iv_background);
        task.execute(R.drawable.splashscreenlogo);
        turnOnWIFI(); // turn on wifi if is off
    }

    // method that starts once background image has been loaded
    public void begin(){
        if(checkPlayServices()){ // check play services on device
            if(isNetworkOnline()) { // check network active on device
                if (gpsSettingsMenu()) { // check gps active on device

                    // for mobile services
                    mApplication = (MobileServiceApp) getApplication();
                    mApplication.setCurrentActivity(this);
                    mService = mApplication.getMobileService();

                    new Handler().postDelayed(new Runnable() { // timer
                        @Override
                        public void run() {
                            // if already authenticated go to main otherwise go to login
                            if (mService.isUserAuthenticated()) {
                                Intent mainScreen = new Intent(Cascade.this, MainActivity.class);
                                startActivity(mainScreen);
                                finish();
                            } else {
                                Intent loginScreen = new Intent(Cascade.this, LoginActivity.class);
                                startActivity(loginScreen);
                                finish();
                            }
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    Toast.makeText(Cascade.this, "please turn on your devices GPS", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else{
                Toast.makeText(Cascade.this, "you must have an internet connection to use this application", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else{
            finish();
        }
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
                Toast.makeText(Cascade.this, "this device is not supported by Google play services and cannot run this application", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    // turn wifi on if it is not
    public void turnOnWIFI(){
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
    }

    // check is network active
    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error checking for network connectivity: " + ex.getMessage());
            status = false;
        }
        return status;
    }

    // if GPS is off send user to gps settings menu to turn it on
    public boolean gpsSettingsMenu(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return false;
        }
        else{
            return true;
        }
    }

    // asynch class for running background image (must be off main thread)
    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.splashscreenlogo), size.x, size.y, true);
            return bmp;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
                begin();
            }
            else{
                begin();
            }
        }
    }
}
