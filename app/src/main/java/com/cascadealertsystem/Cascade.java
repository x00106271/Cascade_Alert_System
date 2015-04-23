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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    private static int SPLASH_TIME_OUT = 2000; // Splash screen timer
    private TextView text;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* create a full screen window for the logo image*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.activity_opening_screen);

        // create background image
        ImageView iv_background = (ImageView) findViewById(R.id.iv_background);
        BitmapWorkerTask task = new BitmapWorkerTask(iv_background);
        task.execute(R.drawable.splashscreenlogo);

        text=(TextView) findViewById(R.id.splash_text);
        btn=(Button) findViewById(R.id.splash_btn);
        btn.setVisibility(View.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
                btn.setVisibility(View.INVISIBLE);
                begin();
            }
        });
    }

    // method that starts once background image has been loaded
    public void begin(){
        if(checkPlayServices()){ // check play services on device
            if(isNetworkOnline()) { // check network active on device
                if (gpsActive()) { // check gps active on device

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
                    text.setText("Please turn GPS on and then press the Retry button below");
                    btn.setVisibility(View.VISIBLE);
                }
            }
            else{
                text.setText("Please turn on the internet and then press the Retry button below");
                btn.setVisibility(View.VISIBLE);
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
    public boolean gpsActive(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            return false;
        }
        else{
            return true;
        }
    }

    // asynch class for running background image (must be not on main thread)
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
