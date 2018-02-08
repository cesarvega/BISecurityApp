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
        LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng position = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(position).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

    }
}