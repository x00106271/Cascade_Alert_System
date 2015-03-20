package activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import com.cascadealertsystem.R;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
import models.Area;
import services.MobileService;
import services.MobileServiceApp;

public class DisplayCreateAlert extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private String type,location,descriptionText,titleText;
    private Spinner spinner1,spinner2;
    private EditText description,title;
    private Button image,video,date;
    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "CreateAlertActivity";
    private ArrayList<String> arealist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_create_alert);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        // create spinners
        spinner1=(Spinner) findViewById(R.id.alert_type_spinner); // for the alert type
        spinner2=(Spinner) findViewById(R.id.alert_location_spinner); // for the alert location

        // fill spinner dynamically from DB
        arealist=new ArrayList<String>();
        addToSpinner();

        // spinner listener
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

        // text field for alert
        description=(EditText) findViewById(R.id.alertEditText);
        title=(EditText) findViewById(R.id.alert_title);

        // buttons
        image=(Button) findViewById(R.id.imageBtn);
        video=(Button) findViewById(R.id.videoBtn);
        date=(Button) findViewById(R.id.dateBtn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_create_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    // take info from spinners selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(id==R.id.alert_type_spinner){
            type=parent.getItemAtPosition(position).toString();
        }
        else{
            location=parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // spinner fill of areas
    public void addToSpinner() {
        mService.getSpinnerList(new TableQueryCallback<Area>() {

            @Override
            public void onCompleted(List<Area> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if (exception == null) {

                    for (Area item : results) {
                        arealist.add(item.getLabel());
                    }
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(DisplayCreateAlert.this,
                            android.R.layout.simple_dropdown_item_1line, arealist);
                    spinner2.setAdapter(adp);

                } else {

                }
            }
        });
    }

    //This does the alert insert to Mobile Services
    public void addAlert(){
        Alert newalert=new Alert();
        mService.insertAlert(newalert, new TableOperationCallback<Alert>() {
            @Override
            public void onCompleted(Alert entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    return;
                }

            }
        });
    }
}
