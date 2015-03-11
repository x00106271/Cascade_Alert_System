package com.cascadealertsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.User;


public class RegisterActivity extends Activity {

    String firstNameText, lastNameText,emailText,passwordText,passwordConfirm,addressStreet,addressEstate,addressArea,referText,
            dobText,phoneText;
    EditText firstname, lastname,email,password,passwordC,street,estate,area,refer,dob,phone;
    TextView loginScreen;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginScreen = (TextView) findViewById(R.id.link_to_login);
        firstname=(EditText) findViewById(R.id.reg_firstname);
        lastname=(EditText) findViewById(R.id.reg_lastname);
        email=(EditText) findViewById(R.id.reg_email);
        password=(EditText) findViewById(R.id.reg_password);
        passwordC=(EditText) findViewById(R.id.reg_password_confirm);
        refer=(EditText) findViewById(R.id.reg_refer);
        phone=(EditText) findViewById(R.id.reg_phone);
        submitButton=(Button) findViewById(R.id.regBtnSubmit);

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
                addressStreet=street.getText().toString().trim();
                addressEstate=estate.getText().toString().trim();
                addressArea=area.getText().toString().trim();
                passwordConfirm=passwordC.getText().toString().trim();
                dobText=dob.getText().toString().trim();
                phoneText=phone.getText().toString().trim();
                boolean proceed=validate();
                if(proceed){
                    submit();
                }
            }
        });
    }
    // what happens when submit button pressed
    public void submit(){
        boolean passed=createUser();
        if(passed){
            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_LONG).show();
            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainScreen);
            finish();
        }
        else{
            Toast.makeText(this, "Process failed! Try again", Toast.LENGTH_LONG).show();
        }
    }
    // create a new user
    public boolean createUser(){
        // final User user=new User(emailText,passwordText,null,false,firstNameText,lastNameText,dobText,phoneText,true,0,"",referText,DATE);

        return true;
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
        else if(addressStreet.equals("")||addressStreet.equals("(street)")||addressEstate.equals("")||addressEstate.equals("(estate)")
                ||addressArea.equals("")||addressArea.equals("(area)")){
            Toast.makeText(this, "you must enter a full address (street, estate and area)", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(referText.equals("")){
            Toast.makeText(this, "please supply the name of the person who referred you", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(dobText.equals("")){ //must validate date is correct
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
}
