package com.minhnhan.sever;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by MinhNhan on 17/05/2016.
 */
public class CheckConnect {
    Context context;

    // Check all connectivities whether available or not
    public boolean isNetworkAvailable(Context context) {
        this.context = context;
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(context, "Internet accessed", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(context, "no Internet", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean isGPSEnabled(Context context){

        LocationManager lm;
        boolean GPS_Status =false;
        boolean gps_enabled = false;
        String title,message="";
        try{
            lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(gps_enabled){
            GPS_Status = true;
            Toast.makeText(context, "GPS on", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "no GPS", Toast.LENGTH_SHORT).show();
        }
        return GPS_Status;
    }
}
