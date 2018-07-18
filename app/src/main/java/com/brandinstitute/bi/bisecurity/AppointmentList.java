package com.brandinstitute.bi.bisecurity;

/**
 * Created by cvega on 2/2/2018.
 */

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import android.app.Service;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.jdeferred.AlwaysCallback;
import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.ProgressCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static java.security.AccessController.getContext;


public class AppointmentList   {
    String AppId;
    String AppDate;
    String cliCompany;
    String cliContact;
    String cliAddress1;
    String cliAddress2;
    String cliCity;
    String cliState;
    String cliZip;
    String cliCountry;
    String cliPhone;
    String cliNotes;
    String appType;
    String project;
    String meetingType;

    public AppointmentList(){
        super();
    }

    public AppointmentList(String appId, String appDate,String cliCompany,String cliContact,String cliAddress1,
                           String cliAddress2,String cliCity,String cliState,String cliZip,String cliCountry,
                           String cliPhone,String cliNotes,String appType,String project,String meetingType){
        this.AppId = appId;
        this.AppDate = appDate;
        this.cliCompany = cliCompany;
        this.cliContact = cliContact;
        this.cliAddress1 = cliAddress1;
        this.cliAddress2 = cliAddress2;
        this.cliCity = cliCity;
        this.cliState = cliState;
        this.cliZip = cliZip;
        this.cliCountry = cliCountry;
        this.cliPhone = cliPhone;
        this.cliNotes = cliNotes;
        this.appType = appType;
        this.project = project;
        this.meetingType = meetingType;
    }

    private static Location location;
    private double latitude;
    private double longitude;

    private boolean isRunning;
    private Context mContext;
    private Thread backgroundThread;

    private static String appointments;
    private static String mDate;
    private static String mSource;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1000 * 10 * 1 ->10 Secs

    public LocationManager locationManager;

    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(this);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    private Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Log.d("GPS Enabled", "GPS Enabled : Updates");

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.d("GPS Enabled", "GPS Enabled -----------");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        mSource = "G";
                        return location;
                    }
                }
            }


            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("Network", "Network");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        mSource = "N";
                        return location;
                    }
                }
            }


        } catch (Exception e) {
            Log.d("Ex", e.getMessage());
        }
        return location;
    }

}