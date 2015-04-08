package com.cascadealertsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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

        if(checkPlayServices()){

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

            // check if wifi is on- turn off if not
            turnOnWIFI();
            // check if gps turned on-send user to settings menu if not
            gpsSettingsMenu();

            Intent loginScreen = new Intent(Cascade.this, LoginActivity.class);
            startActivity(loginScreen);
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

    // if GPS is off send user to gps settings menu to turn it on
    public void gpsSettingsMenu(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
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
            }
        }
    }
}
