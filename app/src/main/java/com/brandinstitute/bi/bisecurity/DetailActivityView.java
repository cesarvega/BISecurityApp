package com.brandinstitute.bi.bisecurity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.lang.System.in;


/**
 * Created by cvega on 2/5/2018.
 */

public class DetailActivityView extends FragmentActivity implements OnMapReadyCallback {
    private Context context;
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

//    TextView txtExpenseDescription;
    TextView txtExpenseAmount;
    ImageView imageView;
    Spinner selectedSpinnerOption;
    private final String sendLocationURL = "https://tools.brandinstitute.com/wsbi/bimobile.asmx/addGeoLocation";

    private ArrayList expenses = new ArrayList<>();

    private GoogleMap mMap;

    private ProgressDialog progressDialog;
    private String appid;
    private String phoneIdType;
    private String phoneId;
    private String searchForAddress;
    private static String mPhoneNumber;
    private static String latitude;
    private static String longitude;
//        private String phoneId = "15555218135";
//    private String phoneId = "3057427989";
    private ArrayList<String> expenseTypeArray ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expenseTypeArray = new ArrayList<String>();
        expenseTypeArray.add("Billable Travel Meals");
        expenseTypeArray.add("Billable Travel Non-Meals");
        expenseTypeArray.add("Non-Billable Travel Meals");
        expenseTypeArray.add("Non-Billable Travel Non-Meals");
        setContentView(R.layout.appointment_detail_view);
        this.context = this;
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        this.mPhoneNumber = null;
        phoneId = (tMgr.getLine1Number() == null)? "D-" + tMgr.getDeviceId(): tMgr.getLine1Number();
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        String android_key;
        android_key = prefs.getString("id", not_set);
        if (android_key != not_set && phoneId == null) {
            mPhoneNumber = android_key;
            phoneId = android_key;
        }
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

        if(getIntent().getStringExtra("cliAddress1").toString().equals("") ||getIntent().getStringExtra("cliAddress1").toString().equals(null) ){
            cliAddress1.setVisibility(View.GONE);
        }else{
            cliAddress1.setText(getIntent().getStringExtra("cliAddress1"));
        }


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
//        lvExpenses= (RecyclerView) findViewById(R.id.list_view_expenses);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        lvExpenses.setLayoutManager(llm);
//        lvExpenses.setHasFixedSize(true);
//        getExpenses();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            String adress = (cliAddress1.getText().toString() + ", ").replace(",,",",");
            String city = (cliCity.getText().toString() + ", ").replace(",,", ",");
            searchForAddress =  adress + city + cliState.getText().toString();
            address = coder.getFromLocationName(searchForAddress, 5);
            if (address.size() != 0){

                Address locationAddress = address.get(0);
                LatLng position = new LatLng(locationAddress.getLatitude(), locationAddress.getLongitude());
                mMap.addMarker(new MarkerOptions().position(position).title(searchForAddress));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
            }else {Toast.makeText(context, "Sorry, we could not find the appointment address ", Toast.LENGTH_LONG).show();}
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static final int REQUEST_TAKE_PHOTO = 1;
    Uri file;

//    private static File getOutputMediaFile(){
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "CameraDemo");
//
//        if (!mediaStorageDir.exists()){
//            if (!mediaStorageDir.mkdirs()){
//                return null;
//            }
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        return new File(mediaStorageDir.getPath() + File.separator +
//                "IMG_"+ timeStamp + ".jpg");
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View promptView = layoutInflater.inflate(R.layout.add_expense_view, null);
                promptView.setMinimumHeight(200);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.spinnerTheme);
                alertDialogBuilder.setView(promptView);
                alertDialogBuilder.setPositiveButton("Save", null);
                alertDialogBuilder.setNegativeButton("Cancel", null);
                this.imageView = (ImageView)promptView.findViewById(R.id.expensePhoto);
                this.imageView.setRotation(90);
//                txtExpenseDescription = (TextView)promptView.findViewById(R.id.expenseDescription);
                txtExpenseAmount = (TextView)promptView.findViewById(R.id.expenseAmount);
                selectedSpinnerOption = (Spinner) promptView.findViewById(R.id.selectionExpenseType);
                imageView.setImageURI(file);

                // create an alert dialog
                final AlertDialog alert = alertDialogBuilder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button btn = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(txtExpenseAmount.getText().toString().isEmpty() || txtExpenseAmount.getText().toString().equals("") || txtExpenseAmount.getText().toString() == null){
                                    Toast.makeText(context, "Please provide an amount to save the expense", Toast.LENGTH_LONG).show();
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivityView.this, R.style.alertDialogConfirmation);
                                    builder.setMessage("You will not be able to delete this expense. Are you sure you want to submit?")
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    initializeData("", txtExpenseAmount.getText().toString(),
                                                            selectedSpinnerOption.getSelectedItem().toString().subSequence(0, 1).toString(), file.getPath().toString(), "saveToDataBase");
                                                    initializeAdapter();
                                                    dialog.dismiss();
                                                    alert.dismiss();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alertConfirmation = builder.create();
                                    alertConfirmation.show();
                                }
                            }
                        });
                    }
                });
                alert.show();
            }
        }
    }

//    public void addExpenses(View view){
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
//    }

    private void initializeData(String expenseDescription, String expenseAmount, String expenseType, String imageString, String whatToDo){


        expenses.add(new ExpenseList(expenseDescription, expenseAmount, expenseType, imageString, whatToDo));
//        if(txtExpenseDescription != null){
//            if(txtExpenseDescription.getText().toString() != ""){
//                txtExpenseDescription.setText("");
//            }
//        }
        if(txtExpenseAmount != null){
            if(txtExpenseAmount.getText().toString() != ""){
                txtExpenseAmount.setText("");
            }
        }
//        imageView.setImageResource(R.drawable.ic_photo_camera_white_24dp);
    }

    private void initializeAdapter(){
        ExpenseListAdapter adapter = new ExpenseListAdapter(expenses, this.context);
        lvExpenses.setAdapter(adapter);
    }

    public void checkIn(View view) {
            // Try to obtain the map from the SupportMapFragment.
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null){
                    longitude = Double.toString(location.getLongitude());
                    latitude = Double.toString(location.getLatitude());
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(position).title(searchForAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    setLatandlong();
                }else{
                    Toast.makeText(context, "Location error" , Toast.LENGTH_LONG).show();
                }

            }
        }


    public void goGPS(View view){
        String map = "http://maps.google.co.in/maps?q=" + searchForAddress;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }

    private void getExpenses(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://tools.brandinstitute.com/wsbi/bimobile.asmx/getExpensesByAppointmentId", new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
//                Toast.makeText(context, "Expenses arrived", Toast.LENGTH_LONG).show();
                try {
                    JSONArray arr = new JSONArray(s);
                    expenses.clear();
                    for (int i=0; i<arr.length(); i++){
                        byte[] bitArrayResult = Base64.decode( arr.getJSONObject(i).get("img").toString(), Base64.DEFAULT);
                        String pathString;
                        try {
                            File tempFile = File.createTempFile("tempFile", ".jpg", null);
                            FileOutputStream fos = new FileOutputStream(tempFile);
                            fos.write(bitArrayResult);
                            pathString = tempFile.getAbsolutePath().toString();
                            initializeData("", arr.getJSONObject(i).get("recTotal").toString(), expenseTypeArray.get((int)arr.getJSONObject(i).get("recType")-1).toString(), pathString, "");
                            initializeAdapter();
//                            expenses.clear();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
//                Toast.makeText(context, "Expense data error -> "+volleyError, Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                phoneIdType = "1";
                appid =(String) ((DetailActivityView) context).appointmentId.getText().subSequence(15, ((DetailActivityView) context).appointmentId.getText().length());
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("phoneId",phoneId);
                parameters.put("phoneIdType",phoneIdType);
                parameters.put("appid",appid);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }


    private void setLatandlong(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, sendLocationURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Toast.makeText(context, "You have checked in", Toast.LENGTH_LONG).show();

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(context, "An error occurred while you try to check in"+volleyError, Toast.LENGTH_LONG).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String mDate = dateFormat.format(new Date());
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("phoneId", phoneId);// number
                parameters.put("phoneIdType", "1" );
                parameters.put("geoDate", mDate);
                parameters.put("appId", "0");
                parameters.put("geoLatitude", latitude );
                parameters.put("geoLongitude",longitude);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
    }
}