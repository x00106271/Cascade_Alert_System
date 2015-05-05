package activities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adaptors.NamesListAdaptor;
import models.Alert;
import models.BaseUser;
import models.RecipientAlert;
import services.MobileService;
import services.MobileServiceApp;

public class MessageActivity extends ActionBarActivity {

    private Button btn,list;
    private EditText title;
    private EditText body;
    private String titleText,bodyText;
    public static ArrayList<Names> names;
    private MobileService mService;
    private MobileServiceApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn=(Button)findViewById(R.id.message_button);
        btn.setEnabled(false);
        title=(EditText)findViewById(R.id.message_title);
        body=(EditText)findViewById(R.id.message_body);
        list=(Button)findViewById(R.id.spinner5);
        names=new ArrayList<>();


        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        //get list of names to populate list
        getNamesList();

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                post();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                createDropDown();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //create a drop down dialog
    public void createDropDown(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select People");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ListView modeList = new ListView(this);
        NamesListAdaptor nla=new NamesListAdaptor(this);
        modeList.setAdapter(nla);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        for(Names n:names){
            nla.add(n);
        }
        dialog.show();
    }

    //post message
    public void post(){
        btn.setEnabled(false);
        titleText=title.getText().toString().trim();
        bodyText=body.getText().toString().trim();
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date date = new Date((year - 1900), month, day);
        final Alert alert=new Alert(8,mService.getUserId(),titleText,bodyText,date,date,false,5,null,true,true);
        //create alert
        mService.insertAlert(alert, new TableOperationCallback<Alert>() {
            @Override
            public void onCompleted(Alert entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e("add message error: ", exception.getMessage());
                    Toast.makeText(MessageActivity.this, "error...try again!!",
                            Toast.LENGTH_LONG).show();
                    btn.setEnabled(true);
                } else {
                    // input alert into recipient table for every user picked on names list
                    final String alertid=entity.getId();
                    for(Names n:names){
                        if(n.picked==true){
                            final RecipientAlert re=new RecipientAlert(alertid,0,n.usersid);
                            mService.addToRecipients(re,new TableOperationCallback<RecipientAlert>() {
                                @Override
                                public void onCompleted(RecipientAlert rentity, Exception exception,
                                                        ServiceFilterResponse response) {
                                    if(exception!=null){
                                        Log.i("message to recipents: ",exception.getMessage());
                                        Toast.makeText(MessageActivity.this, "error...try again!!",
                                                Toast.LENGTH_LONG).show();
                                        btn.setEnabled(true);
                                    }
                                }
                            });
                        }
                    }
                    Toast.makeText(MessageActivity.this, "message sent",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    //get names list
    public void getNamesList(){
        mService.loadAlerts(new TableQueryCallback<BaseUser>() {

            @Override
            public void onCompleted(List<BaseUser> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    for(BaseUser user:results){
                        Names n=new Names(user.getFirstName(),user.getLastName(),user.getId(),false);
                        names.add(n);

                    }
                    btn.setEnabled(true);
                }
            }
        });
    }

    //class for adaptor
    public class Names{
        public String first;
        public String last;
        public String usersid;
        public boolean picked;
        public Names(String f,String l,String u,boolean p){
            this.first=f;
            this.last=l;
            this.usersid=u;
            this.picked=p;
        }
    }
}
