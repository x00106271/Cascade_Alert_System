package activities;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;
import adaptors.AlertAdaptor;
import models.Alert;
import models.UserArea;
import services.MobileService;
import services.MobileServiceApp;

public class MainActivity extends ActionBarActivity {

    private AlertAdaptor mAdaptor;
    private final String TAG = "MainActivity";
    private MobileService mService;
    private MobileServiceApp mApplication;
    private ListView alertList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // get area id of user
        getAreaId();
        /*Toast.makeText(MainActivity.this, mService.getAreaId(),
                Toast.LENGTH_LONG).show();*/

        // adaptor for displaying alerts
        mAdaptor=new AlertAdaptor(MainActivity.this);
        alertList = (ListView) findViewById(R.id.listViewAlerts);
        alertList.setAdapter(mAdaptor);

        // Load the items from the Mobile Service
        refreshItemsFromTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_alert:
                Intent alertIntent=new Intent(MainActivity.this, DisplayCreateAlert.class);
                startActivity(alertIntent);
                return true;
            case R.id.action_messages:

                return true;
            case R.id.action_map:
                Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.action_refresh:
                refreshItemsFromTable();
                return true;
            case R.id.action_settings:
                // uses dropdown list so handled in menu_main.xml
                return true;
            case R.id.action_help:

                return true;
            case R.id.action_logout:
                mService.logout();
                finish();
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // add alerts
    private void refreshItemsFromTable() {
        mService.getAlerts(new TableQueryCallback<Alert>() {

            @Override
            public void onCompleted(List<Alert> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    mAdaptor.clear();
                    for (Alert item : results) {
                        mAdaptor.add(item);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "error loading alerts",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // get area ids of user
    public void getAreaId(){
        mService.setAreaId(new TableQueryCallback<UserArea>(){
            @Override
            public void onCompleted(List<UserArea> result,int count,Exception e,ServiceFilterResponse response){
                mService.setAreaId(result);
            };
        });
    }

}
