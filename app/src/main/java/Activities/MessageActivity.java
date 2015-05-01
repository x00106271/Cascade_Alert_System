package activities;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

import models.BaseUser;
import services.MobileService;
import services.MobileServiceApp;

public class MessageActivity extends ActionBarActivity {

    private Button btn;
    private EditText title;
    private EditText body;
    private Spinner spin;
    private String titleText,bodyText;
    private ArrayList<String> names;
    private ArrayList<String> picked;
    private MobileService mService;
    private MobileServiceApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn=(Button)findViewById(R.id.message_button);
        title=(EditText)findViewById(R.id.message_title);
        body=(EditText)findViewById(R.id.message_body);
        spin=(Spinner)findViewById(R.id.spinner5);


        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                post();
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
        mService.loadAlerts(new TableQueryCallback<BaseUser>() {

            @Override
            public void onCompleted(List<BaseUser> results, int count,
            Exception exception, ServiceFilterResponse response) {
                if (exception == null) {

                }
            }
        });
    }

    //post message
    public void post(){

    }
}
