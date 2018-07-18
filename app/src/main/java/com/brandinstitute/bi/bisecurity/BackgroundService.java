package com.brandinstitute.bi.bisecurity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by carlos on 12/14/16.
 */

public class BackgroundService extends Service {

    private static Location location;
    private double latitude;
    private double longitude;

    private boolean isRunning;
    private Context mContext;
    private Thread backgroundThread;

    private static String mPhoneNumber;
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


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.mContext = getApplicationContext();
        this.isRunning = false;

        // Phone Number
        TelephonyManager phoneManager = (TelephonyManager)
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        mPhoneNumber = phoneManager.getLine1Number();
//        mPhoneNumber = null;
        mPhoneNumber = (mPhoneNumber == null)? "D-" + phoneManager.getDeviceId(): phoneManager.getLine1Number();
        SharedPreferences prefs = this.getSharedPreferences("Someprefstringreference", 0);
        String not_set = "NOTSET";
        String android_key;
        android_key = prefs.getString("id", not_set);
        if (android_key != not_set && mPhoneNumber == null) {
            mPhoneNumber = android_key;
        }


        // Time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mDate = dateFormat.format(new Date());

        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {

            Location loc = getLocation();

            if (loc != null){
                addGEOLocation(mPhoneNumber, mDate, Double.toString(loc.getLatitude()), Double.toString(loc.getLongitude()));
            }else{
                addGEOLocation(mPhoneNumber, mDate, "00", "00");// sending  zeros when location service is not enable
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            getLocation();
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }


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

    private final String NAMESPACE = "http://brandpoll.brandinstitute.com/BIGeoLocation/";
    private final String URL = "http://brandpoll.brandinstitute.com/BIGeoLocation/wsBasic.asmx";
    private final String SOAP_ACTION = "http://brandpoll.brandinstitute.com/BIGeoLocation/addGEO";
    private final String METHOD_NAME = "addGEO";

    private String addGEOLocation(String device, String locDate, String latitude, String longitude) {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("device", device) ;
        request.addProperty("locDate", locDate ) ;
        request.addProperty("latitude", latitude) ;
        request.addProperty("longitude", longitude ) ;

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }


}




