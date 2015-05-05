package services;


import android.app.Activity;
import android.app.Application;

public class MobileServiceApp extends Application {
    private MobileService mService;
    private Activity mCurrentActivity;

    public MobileServiceApp() {

    }

    public MobileService getMobileService() {
        if (mService == null) {
            mService = new MobileService(this);
        }
        return mService;
    }

    public void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

}
