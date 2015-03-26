package activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.List;
import adaptors.AlertAdaptor;
import models.Alert;
import services.MobileService;
import services.MobileServiceApp;

public class MainActivity extends ActionBarActivity {

    private Button alert,message,maps;
    private AlertAdaptor mAdaptor;
    private final String TAG = "MainActivity";
    private MobileService mService;
    private MobileServiceApp mApplication;
    private ListView alertList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alert=(Button) findViewById(R.id.alertBtn);
        message=(Button) findViewById(R.id.messageBtn);
        maps=(Button) findViewById(R.id.mapsBtn);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // adaptor for displaying alerts
        mAdaptor=new AlertAdaptor(MainActivity.this);
        alertList = (ListView) findViewById(R.id.listViewAlerts);
        alertList.setAdapter(mAdaptor);

        // Load the items from the Mobile Service
        refreshItemsFromTable();

        // Listening to alert button pressed
        alert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent alertIntent=new Intent(MainActivity.this, DisplayCreateAlert.class);
                startActivity(alertIntent);
            }
        });

        // Listening to message button pressed
        message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        // Listening to map button pressed
        maps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent mapIntent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }

        if (id == R.id.menu_refresh) {
            refreshItemsFromTable();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

}
