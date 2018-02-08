package com.brandinstitute.bi.bisecurity;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by cvega on 2/5/2018.
 */

public class DetailActivityView extends FragmentActivity implements OnMapReadyCallback {

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    TextView txtMonth;
    TextView txtDay;
    TextView txtTime;
    TextView companyName;
    TextView clientContact;
    TextView cliAddress1;
    TextView cliAddress2;
    TextView cliCity;
    TextView cliState;
    TextView cliZip;
    TextView cliCountry;
    TextView cliPhone;
    TextView cliNotes;
    TextView appType;
    TextView appointmentId;
    TextView meetingType;

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String s = getIntent().getStringExtra("companyName");
        String ss = getIntent().getStringExtra("clientContact");
        setContentView(R.layout.appointment_detail_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        companyName = (TextView)findViewById(R.id.company_name);
        clientContact = (TextView)findViewById(R.id.client_contact);
        txtMonth = (TextView)findViewById(R.id.month);
        txtDay = (TextView)findViewById(R.id.day);
        txtTime = (TextView)findViewById(R.id.time);
        cliAddress1 = (TextView)findViewById(R.id.client_address1);
        cliAddress2 = (TextView)findViewById(R.id.client_address2);
        cliCity = (TextView)findViewById(R.id.client_city);
        cliState = (TextView)findViewById(R.id.client_state);
        cliZip = (TextView)findViewById(R.id.client_zip);
        cliCountry = (TextView)findViewById(R.id.client_country);
        cliPhone = (TextView)findViewById(R.id.client_phone);
        appointmentId = (TextView)findViewById(R.id.appointment_id);

        companyName.setText(getIntent().getStringExtra("companyName"));
        clientContact.setText(getIntent().getStringExtra("clientContact"));
        txtMonth.setText(getIntent().getStringExtra("txtMonth"));
        txtDay.setText(getIntent().getStringExtra("txtDay"));
        txtTime.setText(getIntent().getStringExtra("txtTime"));
        cliAddress1.setText(getIntent().getStringExtra("cliAddress1"));
        cliAddress2.setText(getIntent().getStringExtra("cliAddress2"));
        cliCity.setText(getIntent().getStringExtra("cliCity"));
        cliState.setText(getIntent().getStringExtra("cliState"));
        cliZip.setText(getIntent().getStringExtra("cliZip"));
        cliCountry.setText(getIntent().getStringExtra("cliCountry"));
        cliPhone.setText(getIntent().getStringExtra("cliPhone"));
        appointmentId.setText(getIntent().getStringExtra("appointmentId"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        try {
            LocationManager locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
             isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
             isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng sydney = new LatLng(longitude, latitude);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                LatLng position = new LatLng(-80.1913, 25.7723);

//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(position)
                                        .zoom(3).build();
                                //Zoom in and animate the camera.
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));  mMap.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}