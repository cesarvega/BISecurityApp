package com.brandinstitute.bi.bisecurity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    TextView txtExpenseDescription;
    TextView textExpenseAmount;
    AppCompatImageView imageView;


    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail_view);

        this.imageView = (AppCompatImageView)this.findViewById(R.id.expensePhoto);

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
        txtExpenseDescription = (TextView)findViewById(R.id.expenseDescription);
        textExpenseAmount = (TextView)findViewById(R.id.expenseAmount);

        companyName.setText(getIntent().getStringExtra("companyName"));
        clientContact.setText(getIntent().getStringExtra("clientContact"));
        txtMonth.setText(getIntent().getStringExtra("txtMonth"));
        txtDay.setText(getIntent().getStringExtra("txtDay"));
        txtTime.setText(getIntent().getStringExtra("txtTime"));
        cliAddress1.setText(getIntent().getStringExtra("cliAddress1"));
        if(getIntent().getStringExtra("cliAddress2").toString().equals("")){
            cliAddress2.setVisibility(View.GONE);
        }else{
            cliAddress2.setText(getIntent().getStringExtra("cliAddress2"));
        }

        if(getIntent().getStringExtra("cliCity").toString().equals("")){
            cliCity.setVisibility(View.GONE);
        }else{
            cliCity.setText(getIntent().getStringExtra("cliCity"));
        }

        if(getIntent().getStringExtra("cliState").toString().equals("")){
            cliState.setVisibility(View.GONE);
        }else{
            cliState.setText(getIntent().getStringExtra("cliState"));
        }

        if(getIntent().getStringExtra("cliZip").toString().equals("")){
            cliZip.setVisibility(View.GONE);
        }else{
            cliZip.setText(getIntent().getStringExtra("cliZip"));
        }

        if(getIntent().getStringExtra("cliCountry").toString().equals("")){
            cliCountry.setVisibility(View.GONE);
        }else{
            cliCountry.setText(getIntent().getStringExtra("cliCountry"));
        }

        cliPhone.setText(getIntent().getStringExtra("cliPhone"));
        appointmentId.setText("Appointment #: " + getIntent().getStringExtra("appointmentId"));
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

    static final int REQUEST_TAKE_PHOTO = 1;

    public void expensePicture(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    public void addExpenses(View view){
        System.out.println("Expense Description: " + txtExpenseDescription.getText());
        System.out.println("Expense Amount: " + textExpenseAmount.getText());
        System.out.println("Picture Taken: " + ((BitmapDrawable)imageView.getDrawable()).getBitmap());
    }
}