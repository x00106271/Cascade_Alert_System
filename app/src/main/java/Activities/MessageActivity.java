package activities;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import com.cascadealertsystem.R;

import java.util.ArrayList;

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

    }
}
