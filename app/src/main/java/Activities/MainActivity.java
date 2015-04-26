package activities;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;
import adaptors.AlertAdaptor;
import models.Alert;
import models.BaseUser;
import models.Comment;
import services.MobileService;
import services.MobileServiceApp;

public class MainActivity extends ActionBarActivity {

    private AlertAdaptor mAdaptor;
    private final String TAG = "MainActivity";
    private MobileService mService;
    private MobileServiceApp mApplication;
    private ListView alertList;
    private ArrayList<Names> list;
    private ArrayList<Comment> list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // keyboard setting
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // adaptor for displaying alerts
        mAdaptor=new AlertAdaptor(MainActivity.this);
        alertList = (ListView) findViewById(R.id.listViewAlerts);
        alertList.setItemsCanFocus(false);
        alertList.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        alertList.setAdapter(mAdaptor);
        alertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {

            }
        });

        // Load the alerts from the Mobile Service
        loadAlerts();
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

    // add alerts from refresh
    private void refreshItemsFromTable() {
        mService.loadComments(new TableQueryCallback<Comment>() {

            @Override
            public void onCompleted(List<Comment> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    list2=new ArrayList<>();
                    for (Comment item : results) {
                        list2.add(item);
                    }
                    mAdaptor.setCommentList(list2);
                    mService.getAlerts(new TableQueryCallback<Alert>() {

                        @Override
                        public void onCompleted(List<Alert> results, int count,
                                                Exception exception, ServiceFilterResponse response) {
                            if (exception == null){
                                mAdaptor.clear();
                                for (Alert item : results) {
                                    mAdaptor.add(item);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Sorry there was an error loading alerts",
                                        Toast.LENGTH_LONG).show();
                                Log.i("alerts load exception: ",exception.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("comments load",exception.getMessage());
                }
            }
        });
    }

    // get alerts at loadup
    public void loadAlerts(){
        mService.loadAlerts(new TableQueryCallback<BaseUser>() {

            @Override
            public void onCompleted(List<BaseUser> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if(exception==null){
                    list=new ArrayList<>();
                    for(int i=0;i<results.size();i++){
                        list.add(new Names(results.get(i).getId(),results.get(i).getFirstName(),results.get(i).getLastName()));
                    }
                    mAdaptor.setList(list);
                    mService.loadComments(new TableQueryCallback<Comment>() {

                        @Override
                        public void onCompleted(List<Comment> results, int count,
                                                Exception exception, ServiceFilterResponse response) {
                            if (exception == null) {
                                list2=new ArrayList<>();
                                for (Comment item : results) {
                                    list2.add(item);
                                }
                                mAdaptor.setCommentList(list2);
                                mService.getAlerts(new TableQueryCallback<Alert>() {

                                    @Override
                                    public void onCompleted(List<Alert> results, int count,
                                                            Exception exception, ServiceFilterResponse response) {
                                        if (exception == null){
                                            mAdaptor.clear();
                                            for (Alert item : results) {
                                                mAdaptor.add(item);
                                            }
                                        } else {
                                            Toast.makeText(MainActivity.this, "Sorry there was an error loading alerts",
                                                    Toast.LENGTH_LONG).show();
                                            Log.i("alerts load exception: ",exception.getMessage());
                                        }
                                    }
                                });
                            } else {
                                Log.i("comments load",exception.getMessage());
                            }
                        }
                    });
                }
                else{
                    Log.i("loading names: ",exception.getMessage());
                }
            }
        });
    }

    // object to hold types for name list
    public class Names{
        public String id;
        public String first;
        public String last;
        Names(String i,String f,String l){
            this.id=i;
            this.first=f;
            this.last=l;
        }
    }


    public void addComment(Comment com){
        mService.addComment(com);
    }

}
