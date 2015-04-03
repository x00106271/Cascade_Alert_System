package activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
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
import java.util.Date;
import adaptors.PlaceAdaptor;
import models.BaseUser;
import services.Constants;
import services.FetchAddressIntentService;
import services.MobileService;
import services.MobileServiceApp;


public class RegisterActivity extends Activity implements AdapterView.OnItemClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private String firstNameText, lastNameText,emailText,passwordText,passwordConfirm,referText,phoneText,dobTextbox;
    private EditText firstname, lastname,email,password,passwordC,refer,phone;
    private TextView loginScreen,dob;
    private Button submitButton,dobButton,locateButton;
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
        dobButton=(Button) findViewById(R.id.regBtnDOB);
        locateButton=(Button) findViewById(R.id.regFind);

        year=1950;
        month=0;
        day=1;

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                Intent loginScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(loginScreen);
            }
        });

        // Listening to submit button pressed
        submitButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
        firstNameText=firstname.getText().toString().trim();
        lastNameText=lastname.getText().toString().trim();
        emailText=email.getText().toString().trim();
        passwordText=password.getText().toString().trim();
        passwordConfirm=passwordC.getText().toString().trim();
        phoneText=phone.getText().toString().trim();
        referText=refer.getText().toString().trim();
        dobTextbox=dob.getText().toString().trim();
        boolean proceed=validate();
        if(proceed){
            submit();
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
                //mGoogleApiClient.disconnect();
            }
        });

        // autocomplete address
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.reg_address);
        PlaceAdaptor pad = new PlaceAdaptor(RegisterActivity.this,R.layout.address_autocomplete);
        autoCompleteTextView.setAdapter(pad);
        autoCompleteTextView.setOnItemClickListener(this);

    }

    // click listener for the auto complete
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    };

    // what happens when submit button pressed
    public void submit(){
        dobText=new Date((year-1900),month,day);
        BaseUser newuser=new BaseUser(emailText, passwordText, false, firstNameText, lastNameText, dobText,0, phoneText, referText);
        mService.createUser(newuser, new TableOperationCallback<BaseUser>() {
            @Override
            public void onCompleted(BaseUser entity, Exception exception,
                                    ServiceFilterResponse response) {

                if (exception != null) {
                    Log.e(TAG, exception.getMessage());
                    Toast.makeText(RegisterActivity.this, "an error occured...try again!", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_LONG).show();
                    Intent mainScreen = new Intent(RegisterActivity.this, Pre_verify.class);
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
        else if(emailText.equals("")){ // NEED TO VALIDATE EMAIL
            Toast.makeText(this, "you must enter a correct email address", Toast.LENGTH_LONG).show();
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
        else{
            return true;
        }
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
        //
        // More about this in the next section.
        Toast.makeText(RegisterActivity.this, "error failed", Toast.LENGTH_LONG).show();
    }

    // address receiver from service
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == Constants.SUCCESS_RESULT) {
                //ArrayList<String> returned= resultData.getStringArrayList("returning");
                Address add;
                String add2;
                for(int i=0;i<5;i++){
                    add2="";
                    add=Constants.ADDRESSLIST.get(i);
                    for (int j = 0; j < add.getMaxAddressLineIndex(); j++) {
                        add2=add2+add.getAddressLine(j)+", ";
                    }
                    Toast.makeText(RegisterActivity.this, add2, Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(RegisterActivity.this, "error code returned", Toast.LENGTH_LONG).show();
            }

        }
    }
}
