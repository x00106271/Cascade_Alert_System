package activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;
import adaptors.AlertAdaptor;
import adaptors.HelpAdaptor;
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
    private ProgressBar spinner;
    public static Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load progress spinner
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

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
            case R.id.action_help: // dialog with help for icons
                HelpAdaptor ad=new HelpAdaptor(this,R.layout.help_row,R.id.help_text);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Icon Descriptions");
                builder.setAdapter(ad, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                String s;
                for(int i=0;i<12;i++){
                    s=getTextHelp(i);
                    ad.add(new HelpAdaptor.Item(s));
                }

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
                        list.add(new Names(results.get(i).getId(),results.get(i).getFirstName(),results.get(i).getLastName(),results.get(i).getUserLevel()));
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
                                            spinner.setVisibility(View.GONE);
                                            for (Alert item : results) {
                                                mAdaptor.add(item);
                                            }
                                        } else {
                                            spinner.setVisibility(View.GONE);
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
        public int userLevel;
        Names(String i,String f,String l,int u){
            this.id=i;
            this.first=f;
            this.last=l;
            this.userLevel=u;
        }
    }

    // add a comment to the DB
    public void addComment(Comment com){
        mService.addComment(com);
    }

    // get the user id for the adapter
    public String getUserId(){
        return mService.getUserId();
    }

    // add text to image for help dropdown
    public String getTextHelp(int pos) {
        String[] imageIds = new String[12];
        imageIds[0] = "Open alert creation screen";
        imageIds[1] = "Open private message screen";
        imageIds[2] = "Open Google maps screen";
        imageIds[3] = "Refresh the alerts";
        imageIds[4] = "Represents a crime";
        imageIds[5]="Represents a fire";
        imageIds[6]="Represents something/someone missing";
        imageIds[7]="Represents something stolen";
        imageIds[8]="Represents a road related issue";
        imageIds[9]="Represents a service outage";
        imageIds[10]="Represents a weather warning";
        imageIds[11]="Represents an event";
        return (imageIds[pos]);
    }

    //delete an alert
    public void deleteAlert(Alert a){
        mService.deleteAlert(a);
        mAdaptor.remove(a);
    }

    //update an alert
    public void updateAlert(Alert a){
        mService.updateAlert(a);
    }
}
