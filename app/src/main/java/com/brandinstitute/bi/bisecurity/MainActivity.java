package com.brandinstitute.bi.bisecurity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView rv;
    public String selectedDate;
    public String deviceId;
    public String mPhoneNumber;
    public String softwareVersion = "v0.14";
    public String android_key;
    private ArrayList appointments = new ArrayList<>();

//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }else{
//            Toast.makeText(context, "Please enable your location services in order for this app to work " , Toast.LENGTH_LONG).show();
        }
        createVerifierStrings();
        setContentView(R.layout.recycler_view);
        this.context = this;
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = (tMgr.getLine1Number() == null)? "D-" + tMgr.getDeviceId(): tMgr.getLine1Number();
        deviceId = tMgr.getDeviceId();
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        android_key = prefs.getString("id", not_set);
        if (android_key != not_set && tMgr.getLine1Number() == null) {
            mPhoneNumber = android_key;
        }

        this.setTitle("Brand Institute " + softwareVersion);

        Intent alarm = new Intent(this.context, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 * 3, pendingIntent); // 30 sec
        }

        AppointmentList items = new AppointmentList();

        final Calendar c = Calendar.getInstance();
        final int currentYear = c.get(Calendar.YEAR);
        final int currentMonth = c.get(Calendar.MONTH);
        final int currentDay = c.get(Calendar.DAY_OF_MONTH);

        getAppointmentsHelper(currentMonth, currentDay, currentYear);

        rv=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.date){
            final Calendar c = Calendar.getInstance();
            final int mYear = c.get(Calendar.YEAR);
            final int mMonth = c.get(Calendar.MONTH);
            final int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,  R.style.datePickerTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            getAppointmentsHelper(monthOfYear, dayOfMonth, year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeData(String response){
        appointments.clear();
        String[] appointmentArray = response.replaceAll("\"","").split("\\|");
        for(int i=0; i<appointmentArray.length; i++){
            String[] appointmentDetail = appointmentArray[i].split("~");
                appointments.add(new AppointmentList(appointmentDetail[0], appointmentDetail[1],
                        appointmentDetail[2], appointmentDetail[3], appointmentDetail[4],
                        appointmentDetail[5], appointmentDetail[6], appointmentDetail[7],
                        appointmentDetail[8], appointmentDetail[9], appointmentDetail[10],
                        appointmentDetail[11], appointmentDetail[12], appointmentDetail[13],
                        appointmentDetail[14]));
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(appointments);
        rv.setAdapter(adapter);
    }

    private void getAppointmentsHelper(int month, int day, int year){
        selectedDate = month+1 + "/" + day + "/" + year;
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneNumber = (tMgr.getLine1Number() == null)?  tMgr.getDeviceId():  tMgr.getLine1Number();
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        final String android_key;
        android_key = prefs.getString("id", not_set);
        if (android_key != not_set && mPhoneNumber == null) {
            mPhoneNumber = android_key;
        }
        this.android_key = android_key;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://tools.brandinstitute.com/wsbi/bimobile.asmx/getAppointmentsPipedString", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                if(!response.isEmpty()){
                    initializeData(response);
                    initializeAdapter();
                }else{
                    Toast.makeText(context, "No appointments available for selected date: " + selectedDate, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        }){
            TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phoneId",mPhoneNumber);
                params.put("phoneIdType","1" + softwareVersion + "_P" + mPhoneNumber +  "_D" + deviceId + "_K" + android_key);
                params.put("selDate", selectedDate);
                return params;
            }
        };
        queue.add(sr);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private void createVerifierStrings() {
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        String android_key;
        android_key = prefs.getString("id", not_set);
         if (android_key.equals(not_set)) {
            android_key = getSaltString();
            prefs.edit().putString("id", android_key).commit();
            this.android_key =android_key;
        }else{
             this.android_key =android_key;
         }
    }
}

