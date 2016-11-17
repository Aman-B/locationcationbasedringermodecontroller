package com.bewtechnologies.gtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aman  on 7/23/2015.
 */
public class TrackerService extends Service implements LocationListener {
    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 11; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000*60*5; // 30 minutes

    // Declaring a Location Manager
    protected LocationManager locationManager;
    private String provider;

    public TrackerService(Context context) {
        this.mContext = context;
        getLocation();
    }

    private Location getLocation() {
        //criteria
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);



        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            provider = locationManager.getBestProvider(criteria, true);


            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled

                createdialog(mContext);

            }
            else
            {
                    this.canGetLocation = true;
                    Log.i("criteria works? ", "see "+provider);
                    if(provider!=null)
                    {
                        if (locationManager != null)
                        {
                            location = locationManager
                                    .getLastKnownLocation(provider);
                            locationManager.requestLocationUpdates(
                                    provider,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("location:"," "+location);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("lat:"," "+latitude);
                                Log.d("long:"," "+longitude);
                            }

                            // First get location from Network Provider
                            else if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                }
                            }
                            // if GPS Enabled get lat/long using GPS Services
                           else if (isGPSEnabled) {

                                    locationManager.requestLocationUpdates(
                                            LocationManager.GPS_PROVIDER,
                                            MIN_TIME_BW_UPDATES,
                                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                    Log.d("GPS Enabled", "GPS Enabled");
                                    if (locationManager != null) {
                                        location = locationManager
                                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        if (location != null) {
                                            latitude = location.getLatitude();
                                            longitude = location.getLongitude();
                                        }
                                    }

                            }
                        }
                    }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub
        Log.i("location changed : ", "check");

    }

    @Override
    public void onProviderDisabled(String arg0) {
            Log.d("Hello, darkness ","my old friend!");
            Toast.makeText(MainActivity.mcon, "Hello darkness my friend", Toast.LENGTH_SHORT).show();
            createdialog(MainActivity.mcon);


    }

    @Override
    public void onProviderEnabled(String arg0) {

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void createdialog(Context context) {

        try {
            android.app.AlertDialog.Builder adialog = new android.app.AlertDialog.Builder(MainActivity.mcon);
            adialog.setTitle("Enable GPS settings");
            adialog.setMessage("Location service of your phone needs to be enabled for this app and it's mode must be set to high accuracy. Do you want to enable it?");


            adialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.mcon.startActivity(i);
                }
            });

            adialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            adialog.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            MainActivity.mcon.startActivity(i);
            PendingIntent pendInt = PendingIntent.getService(MainActivity.mcon,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            // if alertdialog box fails, try notification.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.mcon)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentText("GPS needs to be enabled and set to high accuracy for Gtone to work.")
                            .setContentTitle("alarmreceive")
                            .setContentIntent(pendInt)
                            .setAutoCancel(true);
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            Notification note = mBuilder.build();

            note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


            // Builds the notification and issues it.
            mNotifyMgr.notify(0, note);

        }


    }


}
