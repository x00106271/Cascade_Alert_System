package activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;

import com.cascadealertsystem.R;

public class DisplayCreateAlert extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private String type,location,descriptionText,titleText;
    private Spinner spinner1,spinner2;
    private EditText description,title;
    private Button image,video,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_create_alert);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // create spinners
        spinner1=(Spinner) findViewById(R.id.alert_type_spinner); // for the alert type
        spinner2=(Spinner) findViewById(R.id.alert_location_spinner); // for the alert location

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
    // method to post alert
    public void postAlert(){

    }
}
