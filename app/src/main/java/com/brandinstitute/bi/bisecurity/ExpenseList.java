package com.brandinstitute.bi.bisecurity;

/**
 * Created by cvega on 2/2/2018.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;

import static android.content.Context.LOCATION_SERVICE;


public class ExpenseList {
    String description;
    String amount;
    String expensePhoto;


    public ExpenseList(){
        super();
    }

    public ExpenseList(String description, String amount, String picture){
        this.description = description;
        this.amount = amount;
        this.expensePhoto = picture;
    }
}