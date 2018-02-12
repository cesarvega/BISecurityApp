package com.brandinstitute.bi.bisecurity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by cvega on 2/5/2018.
 */

public class DetailActivityView extends FragmentActivity implements OnMapReadyCallback {


    private RecyclerView lvExpenses;

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
    TextView txtExpenseAmount;
    ImageView imageView;


    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_detail_view);

        this.imageView = (ImageView)this.findViewById(R.id.expensePhoto);

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
        txtExpenseAmount = (TextView)findViewById(R.id.expenseAmount);

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



        lvExpenses= (RecyclerView) findViewById(R.id.list_view_expenses);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        lvExpenses.setLayoutManager(llm);
        lvExpenses.setHasFixedSize(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LocationManager locationManager = (LocationManager) this
//                .getSystemService(LOCATION_SERVICE);
//        Location location = locationManager
//                .getLastKnownLocation(LocationManager.GPS_PROVIDER);


        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            String searchForAddress = cliAddress1.getText().toString() + ", " + cliCity.getText().toString() + ", " + cliState.getText().toString();

            address = coder.getFromLocationName(searchForAddress, 5);
            Address locationAddress = address.get(0);

//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();

            LatLng position = new LatLng(locationAddress.getLatitude(), locationAddress.getLongitude());
            mMap.addMarker(new MarkerOptions().position(position).title("Appointment Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        } catch (IOException ex) {

            ex.printStackTrace();
        }

    }

    static final int REQUEST_TAKE_PHOTO = 1;
    Uri file;

    public void expensePicture(View view){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
            }
        }
    }

    public void addExpenses(View view){
        System.out.println("Expense Description: " + txtExpenseDescription.getText());
        System.out.println("Expense Amount: " + txtExpenseAmount.getText());
        System.out.println("Picture Taken: " + ((BitmapDrawable)imageView.getDrawable()).getBitmap());

        initializeData("send");
        initializeAdapter();
    }

    private ArrayList expenses = new ArrayList<>();
    private void initializeData(String response){
//        expenses.clear();

        expenses.add(new ExpenseList(txtExpenseDescription.getText().toString(), txtExpenseAmount.getText().toString(),file.getPath().toString()));

        txtExpenseDescription.setText("");
        txtExpenseAmount.setText("");
        imageView.setImageResource(R.drawable.ic_photo_camera_white_24dp);
    }

    private void initializeAdapter(){
        ExpenseListAdapter adapter = new ExpenseListAdapter(expenses);
        lvExpenses.setAdapter(adapter);
    }
}