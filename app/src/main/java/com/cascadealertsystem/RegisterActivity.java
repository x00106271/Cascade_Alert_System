package com.cascadealertsystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import java.net.MalformedURLException;
import java.util.Date;
import Model.BaseUser;

public class RegisterActivity extends Activity {

    private MobileServiceTable<BaseUser> mTable;
    private String firstNameText, lastNameText,emailText,passwordText,passwordConfirm,referText,phoneText,dobTextbox;
    private EditText firstname, lastname,email,password,passwordC,refer,phone;
    private TextView loginScreen,dob;
    private Button submitButton,dobButton;
    private Date dobText;
    private int day,month,year;
    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // for mobile services
        try {
            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            MobileServiceClient mClient = new MobileServiceClient(
                    "https://cascade.azure-mobile.net/",
                    "TVkGQDWdaeNNbNKirTHAknOUmHqWgu76",
                    this);
            mTable = mClient.getTable(BaseUser.class);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "error with cascade alert system", Toast.LENGTH_LONG).show();
        }

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

        year=1950;
        month=0;
        day=1;

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
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
    }
    // what happens when submit button pressed
    public void submit(){
        dobText=new Date((year-1900),month,day);
        createUser();
    }

    // create a new user
    public void createUser(){
        final BaseUser user=new BaseUser(emailText,passwordText,false,firstNameText,lastNameText,dobText,0,phoneText,referText);
        // Insert the new item
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... par) {
                String done="";
                try {
                    mTable.insert(user).get();
                    done="true";
                } catch (Exception e) {
                    done="false";
                }
                return done;
            }
            protected void onPostExecute(String done){
                if(done.equals("true")){
                    Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_LONG).show();
                    Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainScreen);
                    finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "an error occured...try again!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
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

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
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
}