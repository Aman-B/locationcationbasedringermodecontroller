package com.bewtechnologies.gtone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Aman  on 8/27/2015.
 */

public class Bootreceiver extends BroadcastReceiver implements LocationListener{

    protected LocationManager locationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        Log.i("Br:", "running");
       // context.startService(new Intent(context,AlarmReceiver.class));


       /* Intent intr = new Intent(context,AlarmReceiver.class);
        context.startService(intr);*/

        if("com.bewtechnologies.gtone.Mate".equals(intent.getAction()))
        {
            Log.i("Br is :", "reading");
        }

        else if(intent.getAction().matches("android.intent.action.PROVIDER_CHANGED"))
        {
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
           boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled) {
                // no network provider is enabled



            }
            else
            {
                if(alarmUp==false)
                {
                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    int interval = 1000 * 60 * 5;
                    Intent alarmIntent = new Intent(context, AlarmReceiver.class);

                    Log.i("Alarmreceiver started: ", "GPS enabled");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                }
                else{
                    Log.i("Else provider:","Service already running. Alarmup? "+alarmUp);
                }
            }

        }

        else if(intent.getAction().matches("android.intent.action.BOOT_COMPLETED")){
            if(alarmUp==false)
            {
                AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                int interval = 1000 * 60 * 5;
                Log.i("Alarmreceiver started: ", "Phone rebooted");
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);


                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            }
            else{
                Log.i("Else boot:","Service already running. Alarmup? "+alarmUp);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
