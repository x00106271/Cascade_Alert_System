package services;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import java.net.MalformedURLException;
import java.util.List;

import activities.LoginActivity;
import models.Alert;
import models.Area;
import models.BaseUser;
import models.GPS;
import models.UserArea;

public class MobileService {

    //Mobile Services objects
    private MobileServiceClient mClient;
    private MobileServiceTable<BaseUser> mBaseUserTable;
    private MobileServiceTable<Area> mAreaTable;
    private MobileServiceTable<Alert> mAlertTable;
    private MobileServiceTable<UserArea> mUserAreaTable;
    private MobileServiceTable<GPS> mGPSTable;
    private Context mContext;
    private final String TAG = "CAS mobile services. ";
    private String mUserId;
    private String mAreaId;
    private List<UserArea> mAreaIds;
    private String mEmail;

    public MobileService(Context context) {
        mContext = context;
        try {
            mClient = new MobileServiceClient(Constants.MOBILE_SERVICE_URL,
                    Constants.MOBILE_SERVICE_APPLICATION_KEY, mContext);

            mBaseUserTable = mClient.getTable(BaseUser.class);
            mAreaTable=mClient.getTable(Area.class);
            mAlertTable=mClient.getTable(Alert.class);
            mUserAreaTable=mClient.getTable(UserArea.class);
            mGPSTable=mClient.getTable(GPS.class);

        } catch (MalformedURLException e) {
            Log.e(TAG, "There was an error creating the Mobile Service.  Verify the URL");
        }
    }

    public void setContext(Context context) {
        mClient.setContext(context);
    }
    public Activity getActivityContext() {
        return (Activity) mClient.getContext();
    }

                        /** Authentication Methods **/

    // validate a user on login screen
    public void validateUser(BaseUser user,TableQueryCallback<BaseUser> callback) {
        mEmail=user.getEmail();
        mBaseUserTable.where().field("email").eq(user.getEmail()).and().field("password")
        .eq(user.getPassword()).execute(callback);
        // .and().field("verified").eq(true)
    }

    // check if user details stored on device
    public boolean isUserAuthenticated() {
        SharedPreferences settings = mContext.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        if(settings!=null){
            String user=settings.getString("userid",null);
            String area=settings.getString("areaid",null);
            if (user != null && !user.equals("")) {
                mUserId=user;
                mAreaId=area;
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    // store user details on device
    public void setAuthentication(){
        SharedPreferences settings = mContext.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putString("userid", mUserId);
        preferencesEditor.putString("areaid", mAreaId);
        preferencesEditor.commit();
    }

    // clear device settings on logout
    public void logout(){
        SharedPreferences settings = mContext.getSharedPreferences("UserData", 0);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.clear();
        preferencesEditor.commit();
        // take user back to login screen
        Intent logoutIntent = new Intent(mContext, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(logoutIntent);
    }

    // set user id
    public void setUserId(String user){
        mUserId=user;
    }

    // set area id from DB
    public void setAreaId(TableQueryCallback<UserArea> callback){
        mUserAreaTable.where().field("userId").eq(mUserId).execute(callback);
    }

    public void setAreaId(String userid){
        final String id=userid;
        new Thread(new Runnable() {
            public void run(){
                try{
                    mAreaIds=mUserAreaTable.where().field("userId").eq(id).execute().get();
                    mAreaId=mAreaIds.get(0).getAreaId();
                    setAuthentication();
                }catch (Exception exception) {
                    Log.i(TAG," could not get area id");
                }
            }
        }).start();
    }

    public String getUserId(){
        return mUserId;
    }
    public String getAreaId(){
        return mAreaId;
    }

                        /** Other Methods **/

    // create user in register screen
    public void createUser(BaseUser user, TableOperationCallback<BaseUser> callback) {
        mBaseUserTable.insert(user, callback);
    }

    // fill spinner in alert creation screen
    public void getSpinnerList(TableQueryCallback<Area> callback) {
        mAreaTable.where().execute(callback);
    }

    // create alert in alert creation screen
    public void insertAlert(Alert alert, TableOperationCallback<Alert> callback) {
        mAlertTable.insert(alert, callback);
    }

    //fill main screen with alerts
    public void getAlerts(TableQueryCallback<Alert> callback) {
        mAlertTable.where().execute(callback);
    }

    // create gps in DB
    public void insertGPS(GPS gps, TableOperationCallback<GPS> callback) {
        mGPSTable.insert(gps, callback);
    }

    // check email unique in register screen/ check email exists for forgot password login screen
    public void checkEmail(String email,TableQueryCallback<BaseUser> callback) {
        mBaseUserTable.where().field("email").eq(email).execute(callback);
    }

}
