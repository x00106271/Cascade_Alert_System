package services;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import java.net.MalformedURLException;
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
    public void validateUser(BaseUser user,TableQueryCallback<BaseUser> callback) {
        mBaseUserTable.where().field("email").eq(user.getEmail()).and().field("verified").eq(true).and().field("password")
                .eq(user.getPassword()).execute(callback);
    }

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

}
