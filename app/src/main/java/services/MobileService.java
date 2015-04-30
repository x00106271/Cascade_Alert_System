package services;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import activities.LoginActivity;
import models.Alert;
import models.Area;
import models.BaseUser;
import models.Comment;
import models.GPS;
import models.MediaAsset;
import models.RecipientAlert;
import models.SMSForgots;
import models.UserArea;

public class MobileService {

    public static MobileServiceClient mClient;
    private MobileServiceTable<BaseUser> mBaseUserTable;
    private MobileServiceTable<Area> mAreaTable;
    private MobileServiceTable<Alert> mAlertTable;
    private MobileServiceTable<UserArea> mUserAreaTable;
    private MobileServiceTable<GPS> mGPSTable;
    private MobileServiceTable<Comment> mCommentTable;
    private MobileServiceTable<RecipientAlert> mRATable;
    private MobileServiceTable<SMSForgots> mPForgotTable;
    private MobileServiceTable<MediaAsset> mMediaTable;
    private Context mContext;
    private final String TAG = "CAS mobile services. ";
    private String mUserId;
    private String mAreaId=null;
    private List<UserArea> mAreaIds;
    private String mEmail;
    private CloudStorageAccount storageAccount;
    private CloudBlobClient blobClient;
    private CloudBlobContainer container;
    private CloudBlockBlob blob;
    public static final String SENDER_ID = "395945727516";

    public MobileService(Context context) {

        mContext = context;

        // mobile service
        try {
            mClient = new MobileServiceClient(Constants.MOBILE_SERVICE_URL,
                    Constants.MOBILE_SERVICE_APPLICATION_KEY, mContext);

            NotificationsManager.handleNotifications(mContext, SENDER_ID, MyHandler.class);

            mClient.registerDeserializer(Date.class,new DateDeserializer());
            mClient.registerSerializer(Date.class,new DateSerializer());

            mBaseUserTable = mClient.getTable(BaseUser.class);
            mAreaTable=mClient.getTable(Area.class);
            mAlertTable=mClient.getTable(Alert.class);
            mUserAreaTable=mClient.getTable(UserArea.class);
            mGPSTable=mClient.getTable(GPS.class);
            mCommentTable=mClient.getTable(Comment.class);
            mRATable=mClient.getTable(RecipientAlert.class);
            mPForgotTable=mClient.getTable(SMSForgots.class);
            mMediaTable=mClient.getTable(MediaAsset.class);

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

    // set area id from login
    public void setAreaIdLogin(String id,TableQueryCallback<UserArea> callback) {
        mUserAreaTable.where().field("userId").eq(id).execute(callback);
    }

    public String getUserId(){
        return mUserId;
    }
    public String getAreaId(){
        return mAreaId;
    }
    //set area id
    public void setAreaId(String id) {this.mAreaId=id;}

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
        mAlertTable.where().field("active").eq(true).execute(callback);
    }

    // create gps in DB
    public void insertGPS(GPS gps, TableOperationCallback<GPS> callback) {
        mGPSTable.insert(gps, callback);
    }

    // check email unique in register screen/ check email exists for forgot password login screen
    public void checkEmail(String email,TableQueryCallback<BaseUser> callback) {
        mBaseUserTable.where().field("email").eq(email).execute(callback);
    }

    // load alerts on main screen load
    public void loadAlerts(TableQueryCallback<BaseUser> callback){
        mBaseUserTable.where().execute(callback);
    }

    // get comment for alert
    public void loadComments(TableQueryCallback<Comment> callback){
        mCommentTable.where().execute(callback);
    }

    // add comment to DB
    public void addComment(Comment com){
        final Comment com2=com;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mCommentTable.insert(com2);
                } catch (Exception exception) {
                    Log.i("add comment error: ",exception.getMessage());
                }
                return null;
            }
        }.execute();
    }

    //add to recipient alert table for messaging
    public void addRecipient(){
        //mRATable.insert();
    }

    //  add entry to forgot password table
    public void addForgotPassword(SMSForgots send){
        final SMSForgots toSend=send;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mPForgotTable.insert(toSend);
                } catch (Exception exception) {
                    Log.i("forgot error: ",exception.getMessage());
                }
                return null;
            }
        }.execute();
    }

    // delete an alert (alert itself not being deleted just the alert being made inactive)
    public void deleteAlert(Alert a){
        final Alert up=a;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mAlertTable.update(up).get();
                } catch (Exception exception) {
                    Log.i("delete alert error: ",exception.getMessage());
                }
                return null;
            }
        }.execute();
    }

    //update an alert
    public void updateAlert(final Alert a){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mAlertTable.update(a).get();

                } catch (Exception exception) {
                    Log.i("update alert error: ",exception.getMessage());
                }
                return null;
            }
        }.execute();
    }

                             /** Storage Methods **/

    public void uploadFile(final String name,final File media){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    storageAccount = CloudStorageAccount.parse(Constants.storageConnectionString);
                    blobClient = storageAccount.createCloudBlobClient();
                    container = blobClient.getContainerReference("assets");
                    container.createIfNotExists();
                    // name the file and upload it
                    blob = container.getBlockBlobReference(name);
                    blob.upload(new FileInputStream(media), media.length());
                }
                catch (Exception e)
                {
                    Log.i("upload file error: ",e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public void listFiles(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    storageAccount = CloudStorageAccount.parse(Constants.storageConnectionString);
                    blobClient = storageAccount.createCloudBlobClient();
                    container = blobClient.getContainerReference("assets");
                    container.createIfNotExists();
                    // Loop over blobs within the container and output the URI to each of them.
                    for (ListBlobItem blobItem : container.listBlobs()) {
                        Log.i("file: ", blobItem.getUri().toString());
                    }
                }
                catch (Exception e)
                {
                    Log.i("list files error: ",e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    // this file is being called in main activity file instead
    public void downloadFile(final String name){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    storageAccount = CloudStorageAccount.parse(Constants.storageConnectionString);
                    blobClient = storageAccount.createCloudBlobClient();
                    container = blobClient.getContainerReference("assets");
                    container.createIfNotExists();
                    // Loop through each blob item in the container.
                    for (ListBlobItem blobItem : container.listBlobs()) {
                        // If the item is a blob, not a virtual directory.
                        if (blobItem instanceof CloudBlob) {
                            // Download the item and save it to a file with the same name.
                            CloudBlob blob = (CloudBlob) blobItem;
                            if(blob.getName().equals(name)){
                                File output=File.createTempFile("download",null,mContext.getCacheDir());
                                String filename=output.getAbsolutePath();
                                blob.download(new FileOutputStream(output));
                                output.deleteOnExit(); // delete file when program ends
                                Log.i("download NAME: ",filename);
                            }
                        }
                    }


                }
                catch (Exception e)
                {
                    Log.i("download file error: ",e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    public void deleteFile(){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try
                {
                    storageAccount = CloudStorageAccount.parse(Constants.storageConnectionString);
                    blobClient = storageAccount.createCloudBlobClient();
                    container = blobClient.getContainerReference("assets");
                    container.createIfNotExists();
                    // Retrieve reference to a blob named "myimage.jpg".
                    CloudBlockBlob blob = container.getBlockBlobReference("myimage.jpg");

                    // Delete the blob.
                    blob.deleteIfExists();
                }
                catch (Exception e)
                {
                    Log.i("delete file error: ",e.getMessage());
                }
                return null;
            }
        }.execute();
    }

    // insert into media assets table
    public void insertMedia(MediaAsset med,TableOperationCallback<MediaAsset> callback) {
        mMediaTable.insert(med, callback);

    }

    //update the media asset table
    public void updateMedia(final MediaAsset a){
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                mMediaTable.update(a);
                return null;
            }
        }.execute();
    }

    //get media assets list
    public void getMediaList(TableQueryCallback<MediaAsset> callback){
        mMediaTable.where().execute(callback);
    }

}
