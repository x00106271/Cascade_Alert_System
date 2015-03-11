package com.cascadealertsystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.EditText;

public class DisplayCreateAlert extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    String type,location,alert;
    Spinner spinner1,spinner2;
    EditText alertText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_create_alert);
        // create spinners
        spinner1=(Spinner) findViewById(R.id.alert_type_spinner); // for the alert type
        spinner2=(Spinner) findViewById(R.id.alert_location_spinner); // for the alert location
        // spinner listener
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        // text field for alert
        alertText=(EditText) findViewById(R.id.alertEditText);
        // make the keyboard automatically pop up with activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            return true;
        }



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
    // method to post alert
    public void postAlert(){
        alert=alertText.getText().toString().trim();
    }
}
