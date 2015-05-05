package activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
import models.GPS;
import services.MobileService;
import services.MobileServiceApp;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap map;
    private Spinner spin1;
    private MobileService mService;
    private MobileServiceApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // for mobile services
        mApplication = (MobileServiceApp) getApplication();
        mApplication.setCurrentActivity(this);
        mService = mApplication.getMobileService();

        //default opening map
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        LatLng irl = new LatLng(53.344103999999990000,  -6.267493699999932000);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(irl, 5));
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(true);
        map.clear();

        //spinner
        spin1 = (Spinner) findViewById(R.id.spinner1);
        List<String> list = new ArrayList<>();
        list.add("choose an alert to view on map");
        list.add("Crime");
        list.add("Fire");
        list.add("Missing");
        list.add("Stolen");
        list.add("Road");
        list.add("Service");
        list.add("Weather");
        list.add("Event");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(dataAdapter);
        spin1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            if(pos==1){
                load(0);
            }
            else if(pos==2){
                load(1);
            }
            else if(pos==3){
                load(2);
            }
            else if(pos==4){
                load(3);
            }
            else if(pos==5){
                load(4);
            }
            else if(pos==6){
                load(5);
            }
            else if(pos==7){
                load(6);
            }
            else{
                load(7);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }

    }

    //load alert type on map
    public void load(int type){
        map.clear();
        mService.getAlertByType(type, new TableQueryCallback<Alert>(){
            @Override
            public void onCompleted(List<Alert> results, int count,
            Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    String gps;
                    Location loc = map.getMyLocation();
                    LatLng irl2 = new LatLng(loc.getLatitude(),loc.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(irl2, 9));
                    for(Alert a:results){
                        gps=a.getGpsId();
                        mService.getCordinates(gps, new TableQueryCallback<GPS>() {
                            @Override
                            public void onCompleted(List<GPS> result, int count,
                                                    Exception exception, ServiceFilterResponse response) {
                                if (exception == null) {
                                    LatLng irl = new LatLng(result.get(0).getLat(),result.get(0).getLng());
                                    map.addMarker(new MarkerOptions()
                                            .position(irl));
                                } else {
                                    Toast.makeText(MapsActivity.this, "Sorry an error has occurred",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
                else{
                    Toast.makeText(MapsActivity.this, "Sorry an error has occurred",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
