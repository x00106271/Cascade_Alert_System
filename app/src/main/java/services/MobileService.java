package services;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.net.MalformedURLException;
import java.util.List;
import activities.MainActivity;
import models.Alert;
import models.Area;
import models.BaseUser;

public class MobileService {

    //Mobile Services objects
    private MobileServiceClient mClient;
    private MobileServiceTable<BaseUser> mBaseUserTable;
    private MobileServiceTable<Area> mAreaTable;
    private MobileServiceTable<Alert> mAlertTable;
    private Context mContext;
    private final String TAG = "CAS mobile services. ";

    public MobileService(Context context) {
        mContext = context;
        try {
            mClient = new MobileServiceClient(Constants.MOBILE_SERVICE_URL,
                    Constants.MOBILE_SERVICE_APPLICATION_KEY, mContext);

            mBaseUserTable = mClient.getTable(BaseUser.class);
            mAreaTable=mClient.getTable(Area.class);
            mAlertTable=mClient.getTable(Alert.class);

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

    // validate a user on login screen
    public void validateUser(final String email,String pass) {
        final String password=pass;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... emails) {
                String retPassword="";
                String email = emails[0];
                try {
                    List<BaseUser> results =mBaseUserTable.where().field("email").eq(email).execute().get();
                    // List<BaseUser> results =mBaseUserTable.where().field("email").eq(email).and().field("verified").eq(true).execute().get();
                    if(results==null){
                        // do nothing
                    }
                    else{
                        retPassword=results.get(0).getPassword();
                    }
                } catch (Exception e) {

                }
                return retPassword;
            }
            protected void onPostExecute(String pass){
                if(pass.equals(password)){
                    Intent mainScreen = new Intent(mContext, MainActivity.class);
                    mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(mainScreen);
                }
                else{
                    Toast.makeText(mContext.getApplicationContext(), "password is incorrect!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute(email);
    }

    // create a new user in register screen
    public void createUser(String emailText,String passwordText,boolean ver,String firstNameText,String lastNameText,Date dobText,String phoneText,String referText){
        final BaseUser user=new BaseUser(emailText,passwordText,ver,firstNameText,lastNameText,dobText,0,phoneText,referText);
        // Insert the new item
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... par) {
                String done="";
                try {
                    mBaseUserTable.insert(user).get();
                    done="true";
                } catch (Exception e) {
                    done="false";
                }
                return done;
            }
            protected void onPostExecute(String done){
                if(done.equals("true")){
                    Toast.makeText(mContext, "User created", Toast.LENGTH_LONG).show();
                    Intent mainScreen = new Intent(mContext, MainActivity.class);
                    mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(mainScreen);
                }
                else{
                    Toast.makeText(mContext, "an error occured...try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    // fill spinner in alert creation screen
    public void getSpinnerList(TableQueryCallback<Area> callback) {
        mAreaTable.where().execute(callback);
    }

    // create alert in alert creation screen
    public void insertAlert(Alert alert, TableOperationCallback<Alert> callback) {
        mAlertTable.insert(alert, callback);
    }

}
