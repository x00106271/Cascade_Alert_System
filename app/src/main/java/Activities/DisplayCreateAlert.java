package activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adaptors.PlaceAdaptor;
import adaptors.SpinnerTypeAdaptor;
import models.Alert;
import models.Area;
import models.GPS;
import services.Constants;
import services.FetchCordinatesIntentService;
import services.MobileService;
import services.MobileServiceApp;

public class DisplayCreateAlert extends ActionBarActivity implements AdapterView.OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String type, location, descriptionText, titleText;
    private Spinner spinner1, spinner2;
    private EditText description, title;
    private Button post;
    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "CreateAlertActivity";
    private ArrayList<String> arealist;
    private CheckBox priority;
    private int priorityOn = 0;
    private Date date;
    private int day, month, year;
    static final int DATE_DIALOG_ID = 100;
    private TextView textDate, changedate, image, video;
    private AutoCompleteTextView autoCompleteTextView;
    private AddressResultReceiver mResultReceiverGPS;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private GoogleMap googleMap;
    private ImageButton search;
    private boolean addressFound;
    private String gpsId;


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
        spinner1 = (Spinner) findViewById(R.id.alert_type_spinner); // for the alert type
        spinner2 = (Spinner) findViewById(R.id.alert_location_spinner); // for the alert location

        // fill spinner 2 dynamically from DB
        arealist = new ArrayList<String>();
        addToSpinner();

        //for google play services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        addressFound=false;

        //fill spinner 1
        String[] list = {"Crime", "Fire", "Missing", "Stolen", "Road", "Service", "Weather", "Event"};
        SpinnerTypeAdaptor adp1 = new SpinnerTypeAdaptor(this, R.layout.spinner_type, R.id.text_image_spinner, list);
        spinner1.setAdapter(adp1);
        spinner1.setOnItemSelectedListener(this);

        // text field for alert
        description = (EditText) findViewById(R.id.alertEditText);
        title = (EditText) findViewById(R.id.alert_title);

        // for reciever
        mResultReceiverGPS = new AddressResultReceiver(new Handler());

        // buttons
        image = (TextView) findViewById(R.id.imageBtn);
        video = (TextView) findViewById(R.id.videoBtn);
        changedate = (TextView) findViewById(R.id.setDateBtn);
        post = (Button) findViewById(R.id.postBtn);
        search = (ImageButton) findViewById(R.id.alert_search);
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
        changedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        textDate = (TextView) findViewById(R.id.alertDate);
        setCurrentDate();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = title.getText().toString().trim();
                descriptionText = description.getText().toString().trim();
                if(validate()){
                    post.setEnabled(false);
                    insertGPS();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGPS(autoCompleteTextView.getText().toString());
            }
        });

        // checkbox for priority
        priority = (CheckBox) findViewById(R.id.checkPriority);
        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    priorityOn = 1;
                }
            }
        });

        // autocomplete address
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.alert_auto_address);
        PlaceAdaptor pad = new PlaceAdaptor(DisplayCreateAlert.this, R.layout.address_autocomplete);
        autoCompleteTextView.setAdapter(pad);
        autoCompleteTextView.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view , int position ,long arg3)
            {
                hideKeyboard();
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
        if (parent==spinner1) {
            type = parent.getItemAtPosition(position).toString();
        } else if (parent==spinner2) {
            location = parent.getItemAtPosition(position).toString();
        } else {

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
                            android.R.layout.simple_spinner_dropdown_item, arealist);
                    spinner2.setAdapter(adp);
                    spinner2.setOnItemSelectedListener(DisplayCreateAlert.this);
                }
            }
        });
    }

    //This does the alert insert to Mobile Services
    public void addAlert(String gps) {
        Alert newalert = new Alert(getAlertType(), mService.getUserId(), titleText, descriptionText, date, date, isBroadcast(), priorityOn, gps, true, true);
        mService.insertAlert(newalert, new TableOperationCallback<Alert>() {
            @Override
            public void onCompleted(Alert entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    Toast.makeText(DisplayCreateAlert.this, "error...try again!!",
                            Toast.LENGTH_LONG).show();
                    post.setEnabled(true);
                } else {
                    Toast.makeText(DisplayCreateAlert.this, "alert created",
                            Toast.LENGTH_LONG).show();
                    mGoogleApiClient.disconnect();
                    finish();
                }

            }
        });
    }

    // convert alert type to an int
    public int getAlertType() {
        if (type.equals("Crime")) {
            return 0;
        } else if (type.equals("Fire")) {
            return 1;
        } else if (type.equals("Missing")) {
            return 2;
        } else if (type.equals("Stolen")) {
            return 3;
        } else if (type.equals("Road")) {
            return 4;
        }
        else if (type.equals("Service")) {
            return 5;}
        else if (type.equals("Weather")) {
            return 6;}
        else { //event
            return 7;
        }
    }

    // find out is alert broadcast
    public boolean isBroadcast() {
        if (location.equals("All User Area")) {
            return true;
        } else {
            return false;
        }
    }

    //set current date and display
    public void setCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        // set current date into textview
        textDate.setText(new StringBuilder()
                .append(day).append("-")
                .append(month + 1).append("-")
                .append(year).append(" "));
        // set date object
        date = new Date((year - 1900), month, day);
    }

    // date dialog box
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set new date into textview
            textDate.setText(new StringBuilder()
                    .append(day).append("-")
                    .append(month + 1).append("-")
                    .append(year).append(" "));
            // set date object
            date = new Date((year - 1900), month, day);
        }
    };

    // address receiver from service
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT_GPS) {
                double latitude = Constants.ADDRESS_GPS.getLatitude();
                double longitude = Constants.ADDRESS_GPS.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMap.animateCamera(yourLocation);
                googleMap.addMarker(new MarkerOptions().position(latLng).title("here"));
                addressFound=true;
            } else {
                Toast.makeText(DisplayCreateAlert.this, "sorry the address/area was not found", Toast.LENGTH_LONG).show();
                addressFound=false;
            }

        }
    }

    // get gps from address
    public void getGPS(String address) {
        if (mGoogleApiClient.isConnected()) {
            Intent intent = new Intent(DisplayCreateAlert.this, FetchCordinatesIntentService.class);
            intent.putExtra("receiver", mResultReceiverGPS);
            intent.putExtra("address", address);
            startService(intent);
        } else {
            Toast.makeText(DisplayCreateAlert.this, "sorry your GPS services are unavailable and required to use this application", Toast.LENGTH_SHORT).show();
        }
    }

    // for google play services
    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.alert_map)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(yourLocation);
        }
        else{
            LatLng latLng = new LatLng(53.2734, -7.778320310000026);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 30);
            googleMap.animateCamera(yourLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        Toast.makeText(DisplayCreateAlert.this, "error google connection failed", Toast.LENGTH_LONG).show();
    }

    // hide the keyboard of screen
    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // validate all user input is correct
    public boolean validate() {
        if(titleText.equals("")) {
            Toast.makeText(this, "you must have a title", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(descriptionText.equals("")) {
            Toast.makeText(this, "you must have a description", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!addressFound){
            Toast.makeText(this, "please search for the address you entered to confirm it can be found on GPS", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    // insert gps in DB
    public void insertGPS(){
        GPS gps=new GPS(location,Constants.ADDRESS_GPS.getLatitude(),Constants.ADDRESS_GPS.getLongitude(),
                0,false,autoCompleteTextView.getText().toString(),0);
        mService.insertGPS(gps, new TableOperationCallback<GPS>() {
            @Override
            public void onCompleted(GPS entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    post.setEnabled(true);
                    Toast.makeText(DisplayCreateAlert.this, "an error occurred...please try again!", Toast.LENGTH_LONG).show();
                }
                else{
                    gpsId=entity.getId();
                    addAlert(gpsId);
                }

            }
        });
    }
}
