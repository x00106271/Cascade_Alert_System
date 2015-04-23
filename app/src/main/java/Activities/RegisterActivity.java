package activities;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cascadealertsystem.Pre_verify;
import com.cascadealertsystem.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.Date;
import java.util.List;

import adaptors.PlaceAdaptor;
import models.BaseUser;
import models.GPS;
import services.Constants;
import services.FetchAddressIntentService;
import services.FetchCordinatesIntentService;
import services.MobileService;
import services.MobileServiceApp;


public class RegisterActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String firstNameText, lastNameText,emailText,passwordText,passwordConfirm,referText,phoneText,dobTextbox;
    private EditText firstname, lastname,email,password,passwordC,refer,phone;
    private TextView loginScreen,dob,dobButton,locateButton;
    private Button submitButton;
    private Date dobText;
    private int day,month,year;
    static final int DATE_PICKER_ID = 1111;
    private MobileService mService;
    private MobileServiceApp mApplication;
    private final String TAG = "RegisterActivity";
    private AutoCompleteTextView autoCompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
    private AddressResultReceiver mResultReceiverGPS;
    private String gps_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        //for google play services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        mGoogleApiClient.connect();

        // for reciever
        mResultReceiver=new AddressResultReceiver(new Handler());
        mResultReceiverGPS=new AddressResultReceiver(new Handler());

        loginScreen = (TextView) findViewById(R.id.link_to_login);
        dob=(TextView) findViewById(R.id.regDOB);
        firstname=(EditText) findViewById(R.id.reg_firstname);
        lastname=(EditText) findViewById(R.id.reg_lastname);
        email=(EditText) findViewById(R.id.reg_email);
        password=(EditText) findViewById(R.id.reg_password);
        passwordC=(EditText) findViewById(R.id.reg_password_confirm);
        refer=(EditText) findViewById(R.id.reg_refer);
        phone=(EditText) findViewById(R.id.reg_phone);
        submitButton=(Button) findViewById(R.id.regBtnSubmit);
        dobButton=(TextView) findViewById(R.id.regBtnDOB);
        locateButton=(TextView) findViewById(R.id.regFind);
        gps_id=null;
        year=1950;
        month=0;
        day=1;

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Switching to Login Screen/closing register screen
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                mGoogleApiClient.disconnect();
                startActivity(loginScreen);
            }
        });

        // Listening to submit button pressed
        submitButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                submitButton.setEnabled(false);
                firstNameText = firstname.getText().toString().trim();
                lastNameText = lastname.getText().toString().trim();
                emailText = email.getText().toString().trim();
                passwordText = password.getText().toString().trim();
                passwordConfirm = passwordC.getText().toString().trim();
                phoneText = phone.getText().toString().trim();
                referText = refer.getText().toString().trim();
                dobTextbox = dob.getText().toString().trim();
                boolean proceed = validate(); // validate all user input
                if (proceed) {
                    checkEmailUnique();
                    } else {
                        submitButton.setEnabled(true);
                    }
                }
        });

        // Listening to date picker button pressed
        dobButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
            }
        });

        // listen to locate button pressed
        locateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected() && mLastLocation != null){
                    Intent intent = new Intent(RegisterActivity.this, FetchAddressIntentService.class);
                    intent.putExtra("receiver", mResultReceiver);
                    startService(intent);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "sorry GPS is unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // autocomplete address
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.reg_address);
        PlaceAdaptor pad = new PlaceAdaptor(RegisterActivity.this,R.layout.address_autocomplete);
        autoCompleteTextView.setAdapter(pad);
        autoCompleteTextView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent screen = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(screen);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // click listener for the auto complete
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    // create user in DB
    public void submit(){
        dobText=new Date((year-1900),month,day);
        //create user and put in DB
        BaseUser newuser=new BaseUser(emailText, passwordText, false, firstNameText, lastNameText, dobText,0, phoneText, referText,gps_id);
        mService.createUser(newuser, new TableOperationCallback<BaseUser>() {
            @Override
            public void onCompleted(BaseUser entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    Toast.makeText(RegisterActivity.this, "an error occurred...please try again!", Toast.LENGTH_LONG).show();
                    submitButton.setEnabled(true);
                }
                else{
                    Intent mainScreen = new Intent(RegisterActivity.this, Pre_verify.class);
                    mGoogleApiClient.disconnect();
                    startActivity(mainScreen);
                }

            }
        });
    }

    // validate all user input is correct
    public boolean validate(){
        if(firstNameText.equals("")){
            Toast.makeText(this, "you must enter a first name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(lastNameText.equals("")){
            Toast.makeText(this, "you must enter a last name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(emailText.equals("")){
            Toast.makeText(this, "you must enter an email address", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(passwordText.equals("")){
            Toast.makeText(this, "you must enter a password", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!passwordText.equals(passwordConfirm)){
            Toast.makeText(this, "your passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(referText.equals("")){
            Toast.makeText(this, "please supply the name of the person who referred you", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(dobTextbox.equals("DD/MM/YY")){
            Toast.makeText(this, "please enter your DOB", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(phoneText.equals("")){
            Toast.makeText(this, "please supply a phone number", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(!emailText.equals("")){
            if(validateEmail(emailText)){
                return true;
            }
            else{
                Toast.makeText(this, "you entered an invalid email address", Toast.LENGTH_LONG).show();
                return false;
            }

        }
        else{
            return true;
        }
    }

    // validate email address
    public boolean validateEmail(String email){
        if(email.matches("[a-z0-9!#$%&'*+/=?^_{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_{|}~-]+)*@(?:[a-z0-9](?:‌​[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")){
            return true;
        }
        else{
            return false;
        }
    }

    // validate email address is unique (check it has not already been used)
    public void checkEmailUnique(){
        mService.checkEmail(emailText,new TableQueryCallback<BaseUser>() {

            @Override
            public void onCompleted(List<BaseUser> results, int count,
                                    Exception exception, ServiceFilterResponse response) {
                if(count==0){
                    getGPS(autoCompleteTextView.getText().toString()); // checks if address is correct and returns a gps co-ordinate
                }
                else{
                    Toast.makeText(RegisterActivity.this, "the email address you used is already in use by another user, pick a different email address", Toast.LENGTH_LONG).show();
                    submitButton.setEnabled(true);
                }
            }
            });

    }

    // date of birth dialog box
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            dob.setText(new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append(" "));
        }
    };

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Constants.LOCATION=mLastLocation;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        Toast.makeText(RegisterActivity.this, "error google connection failed", Toast.LENGTH_LONG).show();
    }

    // address receiver from service
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                Address add;
                String add2;
                String[] add3=new String[3];
                for(int i=0;i<3;i++){
                    add2="";
                    add=Constants.ADDRESSLIST.get(i);
                    for (int j = 0; j < add.getMaxAddressLineIndex(); j++) {
                        add2=add2+add.getAddressLine(j)+", ";
                    }
                    add2=add2.substring(0, add2.length() - 2);
                    add3[i]=add2;
                }
                AlertDialog.Builder addDialog = new AlertDialog.Builder(RegisterActivity.this);
                addDialog.setTitle("pick an address");
                final String[] add4=add3;
                addDialog.setItems(add4, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        autoCompleteTextView.setText(add4[item]);
                    }
                });
                addDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog alert = addDialog.create();
                alert.show();
            }
            else if(resultCode == Constants.FAILURE_RESULT){
                Toast.makeText(RegisterActivity.this, "GPS is unavailable", Toast.LENGTH_LONG).show();
            }
            else if(resultCode == Constants.SUCCESS_RESULT_GPS){
                Toast.makeText(RegisterActivity.this, "creating user...", Toast.LENGTH_SHORT).show();
                insertGPS();
            }
            else{
                Toast.makeText(RegisterActivity.this, "sorry the address was not found", Toast.LENGTH_LONG).show();
                submitButton.setEnabled(true);
            }

        }
    }

    // get gps from address
    public void getGPS(String address){
        if (mGoogleApiClient.isConnected()){
            Intent intent = new Intent(RegisterActivity.this, FetchCordinatesIntentService.class);
            intent.putExtra("receiver", mResultReceiverGPS);
            intent.putExtra("address",address);
            startService(intent);
        }
        else{
            Toast.makeText(RegisterActivity.this, "sorry your GPS services are unavailable and required to use this application", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(true);
        }
    }

    // insert gps in DB
    public void insertGPS(){
        GPS gps=new GPS(autoCompleteTextView.getText().toString(),Constants.ADDRESS_GPS.getLatitude(),Constants.ADDRESS_GPS.getLongitude(),
                0,false,autoCompleteTextView.getText().toString(),0);
        mService.insertGPS(gps, new TableOperationCallback<GPS>() {
            @Override
            public void onCompleted(GPS entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    submitButton.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "an error occurred...please try again!", Toast.LENGTH_LONG).show();
                }
                else{
                    gps_id=entity.getId();
                    submit();
                }

            }
        });
    }
}
