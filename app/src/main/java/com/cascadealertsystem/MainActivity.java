package com.cascadealertsystem;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;
import java.util.List;

import Model.Alert;

public class MainActivity extends ActionBarActivity {

    Button alert,message,maps;
    private AlertAdaptor mAdaptor;
    private MobileServiceTable<Alert> mTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alert=(Button) findViewById(R.id.alertBtn);
        message=(Button) findViewById(R.id.messageBtn);
        maps=(Button) findViewById(R.id.mapsBtn);
        mAdaptor=new AlertAdaptor(this,R.layout.alert_row);
        ListView alertList = (ListView) findViewById(R.id.listViewAlerts);
        alertList.setAdapter(mAdaptor);

        // for mobile services
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            MobileServiceClient mClient = new MobileServiceClient(
                    "https://cascade.azure-mobile.net/",
                    "TVkGQDWdaeNNbNKirTHAknOUmHqWgu76",
                    this);
            mTable = mClient.getTable("Alerts",Alert.class);
        } catch (MalformedURLException e) {
            Toast.makeText(MainActivity.this, "error loading mobile services", Toast.LENGTH_LONG).show();
        }

        // Load the items from the Mobile Service
        refreshItemsFromTable();

        // Listening to alert button pressed
        alert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent alertIntent=new Intent(MainActivity.this,DisplayCreateAlert.class);
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

        return super.onOptionsItemSelected(item);
    }

    private void refreshItemsFromTable() {

        // add alerts

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final List<Alert> results =
                            mTable.where().field("Active").
                                    eq(true).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdaptor.clear();

                            for(Alert item : results){
                                mAdaptor.add(item);
                            }
                        }
                    });
                } catch (Exception e){

                }

                return null;
            }
        }.execute();

    }

}
