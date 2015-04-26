package services;


import android.app.Activity;
import android.app.Application;

public class MobileServiceApp extends Application {
    private MobileService mService;
    private Activity mCurrentActivity;
    private boolean mIsApplicationActive = false;

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

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setIsApplicationActive(boolean isApplicationActive) {
        mIsApplicationActive = isApplicationActive;
    }

    public boolean getIsApplicationActive() { return mIsApplicationActive; }



}
