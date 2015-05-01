package activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.Toast;
import com.cascadealertsystem.R;
import com.google.android.gms.games.Notifications;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.google.android.gms.gcm.*;
import com.microsoft.windowsazure.messaging.*;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adaptors.AlertAdaptor;
import adaptors.HelpAdaptor;
import models.Alert;
import models.BaseUser;
import models.Comment;
import models.MediaAsset;
import models.OutputFile;
import models.UserArea;
import services.Constants;
import services.MobileService;
import services.MobileServiceApp;
import services.MyHandler;

public class MainActivity extends ActionBarActivity {

    private AlertAdaptor mAdaptor;
    private final String TAG = "MainActivity";
    private MobileService mService;
    private MobileServiceApp mApplication;
    private ListView alertList;
    public static ArrayList<Names> list;
    private ArrayList<Comment> list2;
    private ProgressBar spinner;
    private ArrayList<Alert> alerts;
    private ArrayList<MediaAsset> media;
    private Context mContext;
    private static ArrayList<OutputFile> files;

    //notification hub
    private String SENDER_ID = "Constants.SENDER_PID";
    private Notifications notifications;
    Set<String> categories = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        //lists
        alerts=new ArrayList<>();
        media=new ArrayList<>();
        files=new ArrayList<>();

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

        //notifiactions
        NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);
        notifications = new Notifications(this,SENDER_ID);
        retrievePushIds();

        // Load the alerts from the Mobile Service
        loadAlerts();
    }

    // action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // action bar menu onclick handler
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

    // add alerts from refresh button
    private void refreshItemsFromTable() {
        mService.loadComments(new TableQueryCallback<Comment>() {

            @Override
            public void onCompleted(List<Comment> results, int count,
                                    Exception exception, ServiceFilterResponse response) { // load comments
                if (exception == null) {
                    list2=new ArrayList<>();
                    for (Comment item : results) {
                        list2.add(item);
                    }
                    mAdaptor.setCommentList(list2);
                    mService.getAlerts(new TableQueryCallback<Alert>() {

                        @Override
                        public void onCompleted(List<Alert> results, int count,
                                                Exception exception, ServiceFilterResponse response) { // load alerts
                            if (exception == null){
                                for (Alert item : results) {
                                    alerts.add(item);
                                }
                                mService.getMediaList(new TableQueryCallback<MediaAsset>() {

                                    @Override
                                    public void onCompleted(List<MediaAsset> results, int count,
                                                            Exception exception, ServiceFilterResponse response) { // load media table list
                                        if (exception == null) {
                                            for (MediaAsset item : results) {
                                                media.add(item);
                                            }
                                            loadAdaptor(); // load alerts into adapter to show on main screen
                                        } else {
                                            spinner.setVisibility(View.GONE);
                                            Toast.makeText(MainActivity.this, "Sorry there was an error loading alerts",
                                                    Toast.LENGTH_LONG).show();
                                            Log.i("media load exception: ", exception.getMessage());
                                        }
                                    }
                                });
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

    // get alerts at load up of activity
    public void loadAlerts(){
        mService.loadAlerts(new TableQueryCallback<BaseUser>() {

            @Override
            public void onCompleted(List<BaseUser> results, int count,
                                    Exception exception, ServiceFilterResponse response) { //names of users and there ids
                if(exception==null){
                    list=new ArrayList<>();
                    for(int i=0;i<results.size();i++){
                        list.add(new Names(results.get(i).getId(),results.get(i).getFirstName(),results.get(i).getLastName(),results.get(i).getUserLevel()));
                    }
                    mAdaptor.setList(list);
                    mService.loadComments(new TableQueryCallback<Comment>() {

                        @Override
                        public void onCompleted(List<Comment> results, int count,
                                                Exception exception, ServiceFilterResponse response) { //comments
                            if (exception == null) {
                                list2=new ArrayList<>();
                                for (Comment item : results) {
                                    list2.add(item);
                                }
                                mAdaptor.setCommentList(list2);
                                mService.getAlerts(new TableQueryCallback<Alert>() {

                                    @Override
                                    public void onCompleted(List<Alert> results, int count,
                                                            Exception exception, ServiceFilterResponse response) { //alerts
                                        if (exception == null){
                                            for (Alert item : results) {
                                                alerts.add(item);
                                            }
                                            mService.getMediaList(new TableQueryCallback<MediaAsset>() {

                                                @Override
                                                public void onCompleted(List<MediaAsset> results, int count,
                                                                        Exception exception, ServiceFilterResponse response) { //media
                                                    if (exception == null) {
                                                        for (MediaAsset item : results) {
                                                            media.add(item);
                                                        }
                                                        loadAdaptor(); // adapter load
                                                    } else {
                                                        spinner.setVisibility(View.GONE);
                                                        Toast.makeText(MainActivity.this, "Sorry there was an error loading alerts",
                                                                Toast.LENGTH_LONG).show();
                                                        Log.i("media load exception: ", exception.getMessage());
                                                    }
                                                }
                                            });
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

    // load adaptor
    public void loadAdaptor(){
        for (Alert item : alerts) { //alerts
            for(MediaAsset ma:media){ //matching media (if any) to alerts
                if(ma.getAlertId().equals(item.getId())){ //if found download file from blob
                    final String dataid=ma.getData();
                    final String alertid=item.getId();
                    final String ext=ma.getExt();
                    new Thread(new Runnable() {
                        public void run() {
                            try
                            {
                                CloudStorageAccount storageAccount = CloudStorageAccount.parse(Constants.storageConnectionString);
                                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                                CloudBlobContainer container = blobClient.getContainerReference("assets");
                                container.createIfNotExists();
                                // Loop through each blob item in the container.
                                for (ListBlobItem blobItem : container.listBlobs()) {
                                    // If the item is a blob, not a virtual directory.
                                    if (blobItem instanceof CloudBlob) {
                                        // Download the item and save it to a file in cache
                                        CloudBlob blob = (CloudBlob) blobItem;
                                        if(blob.getName().equals(dataid)){
                                            File output=File.createTempFile("download", null, mContext.getCacheDir()); //temp file held in cache directory
                                            blob.download(new FileOutputStream(output));
                                            String filename=output.getAbsolutePath(); //path to temp file
                                            Log.i("FILE: ",filename);
                                            output.deleteOnExit(); // delete file when program ends
                                            MainActivity.files.add(new OutputFile(alertid,filename,ext));
                                        }
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                Log.i("download file error: ",e.getMessage());
                            }
                        }
                    }).start();
                }
            }
        }
        mAdaptor.clear();
        spinner.setVisibility(View.GONE);
        mAdaptor.setFileList(files);
        Log.i("NOW","NOW");       // this (NOW NOW) is showing up in the logs, then all the alerts are being loaded in adapter,
        for(OutputFile f:files){  // and then the files come back from download. its the same with both async task and thread
            Log.i("file id: ",f.getId());
            Log.i("file path: ",f.getPath());
        }
        for(Alert item:alerts){
            mAdaptor.add(item);
        }
    }


    //notification hub
    public void retrievePushIds() {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MobileServiceTable<UserArea> mUserAreaTable;
                    List<UserArea> userAreas;

                    mUserAreaTable=MobileService.mClient.getTable(UserArea.class);
                    userAreas = mUserAreaTable.where().field("UserId").
                            eq(MobileService.mUserId).execute().get();
                    categories.add(MobileService.mUserId);
                    for (int i = 0; i < userAreas.size(); i++)
                    {
                        categories.add(userAreas.get(i).areaId);
                        System.out.println(i + " " + userAreas.get(i).areaId);
                    }
                    //notifications.storeCategoriesAndSubscribe(categories);
                } catch (Exception e){
                    //createAndShowDialog(e, "Error");
                }
                return null;
            }
        }.execute();
    }
}
