package com.bewtechnologies.gtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Aman  on 7/23/2015.
 */


public class AlarmReceiver extends BroadcastReceiver {

    public static double  elat; // returns latitude
    public static double elong;
    ShowLocationActivity locationListener;
    private PendingIntent restoreAudioState;
    public int ringstate;

    public int p_ringstate;

    public double alarmlat;
    public double alarmlong;

    public boolean marked =false;

    //to remind user after twelve hours
    long time;


    //Sharedpref
    private int notif;





    @Override
    public void onReceive(Context context, Intent intent) {

        TrackerService gps = new TrackerService(context);
        if(gps.canGetLocation())
        {
            elat = gps.getLatitude(); // returns latitude
            elong = gps.getLongitude();
           /* elat=  15.3536397;
            elong= 75.7936571;*/
            Log.i("Mycoordinates : ","here :"+elat + " " + elong);

            usersetting cm = new usersetting();

            Calendar calender=Calendar.getInstance();



            locationListener = new ShowLocationActivity(context);
            AudioManager adm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            ringstate = adm.getRingerMode();

            Intent i = new Intent(context.getApplicationContext(), restoreAudio.class);
            i.putExtra("com.bewtechnologies.gtone.restore", ringstate);
            restoreAudioState = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

//            Log.i("Notifiedo? "," " + MainActivity.Notified);



           // Log.i("Isnear", "see : "+cm.checkMatch(elat,elong,context));


           // Log.i("Isnear", "see : "+cm.checkMatch(elat,elong,usersetting.tlat,usersetting.tlong)+" one lat: "+usersetting.tlat+" and long : "+usersetting.tlong+" dekhe to "+ cm.givecoord(context));

            //For reminding user after 12hours
            //below method returns hour in 24 hour format
             long checkTime=calender.get(Calendar.HOUR_OF_DAY);
            // System.out.println("Time hour: " +MainActivity.timeline);

             if(MainActivity.timeline == checkTime)
             {
                 cm.putdata(false,context);
             }

          //  Log.i("temp coord ", "here: "+ usersetting.tlat+" "+usersetting.tlong);


            notif=usersetting.notys;

            System.out.println("Notif : " +notif);


                if (cm.checkMatch(elat, elong, context))
                {
                       Log.i("temp coord ", "here: "+"give co"+cm.givecoord(context)+" " + usersetting.tlat + " " + usersetting.tlong);

                        if ((cm.givecoord(context)) != 0)
                        {
                            Log.i("temp coord ", "here: " + usersetting.tlat + " " + usersetting.tlong);
                          //Log.i("inside correct givecoord? ", "yes! cm0 : "+(cm.checkMatch(elat,elong,usersetting.tlat,usersetting.tlong)));

                            if (!(cm.checkMatch(elat, elong, usersetting.tlat, usersetting.tlong)))
                            {
                              // Log.i("inside correct location? ", "yes! cm"+(cm.checkMatch(elat, elong, usersetting.tlat, usersetting.tlong)));
                                notifyuser(context);
                                MainActivity.timeline=calender.get(Calendar.HOUR_OF_DAY);
                                notif=1;
                              //  notif=true;
                               /* usersetting.editor.putBoolean("notif",true);
                                usersetting.editor.apply();*/
                                //cm.putdata(true,context);
                               // Log.i("Notifiedi? ", " Let me do: " + MainActivity.Notified);
                            }
                            /*else{
                                *//*cm.removetemp(context,usersetting.tlat,usersetting.tlong);
                                //notification for restored state


                                cm.putdata(false,context);*//*
                            }*/
                        }
                        else
                        {
                            //Log.i("inside correct location? ", "yes!");
                            notifyuser(context);
                            notif =1;
                            MainActivity.timeline=calender.get(Calendar.HOUR_OF_DAY);
                            //cm.putdata(true,context);
                           // Log.i("Notifiede? ", " Let me do: " + MainActivity.Notified);
                        }
                }
                else
                {
                    if((elat!=0)&&(elong!=0))
                    {
                        cm.givecoord(context);
                        notif = usersetting.notys;
                        Log.i("Notified? ", " In else; " + notif);
                        if (notif == 1) {
                            Log.i("Inside out mode? :", "yes.");
                            //  usersetting.editor.putBoolean("notif",false);

                            //usersetting.notys=true;
                            cm.removetemp(context, usersetting.tlat, usersetting.tlong);
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle("gtone")
                                            .setAutoCancel(true);


                            adm.setRingerMode(ringstate);
                            if (ringstate == 0) {
                                mBuilder.setContentTitle("Your phone is out of silent mode.");
                                mBuilder.setTicker("Silent places- Phone out of silent mode.");
                            } else if (ringstate == 1) {
                                // Log.i("Inside main vibrate mode 1:", "yes");
                                mBuilder.setContentTitle("Your phone is out of vibrate mode.");
                                mBuilder.setTicker("Silent places- Phone out of  vibrate mode.");
                            }


                            NotificationManager mNotifyMgr =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                            Notification note = mBuilder.build();

                            note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


                            // Builds the notification and issues it.
                            mNotifyMgr.notify(0, note);

                        }
                    }
                }


           /* if (notif==true)
            {
                    if ((cm.givecoord(context)) != 0)
                    {


                        if (!(cm.checkMatch(elat, elong, usersetting.tlat, usersetting.tlong)))
                        {




                                //notification for restored state

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(R.drawable.batdroid)
                                                .setContentTitle("alarmreceive")
                                                .setAutoCancel(true);


                                adm.setRingerMode(ringstate);
                                if (ringstate == 0) {
                                    mBuilder.setContentTitle("Your phone is out of silent mode.");
                                    mBuilder.setTicker("Gtone- Phone out of silent mode.");
                                } else if (ringstate == 1) {
                                    // Log.i("Inside main vibrate mode 1:", "yes");
                                    mBuilder.setContentTitle("Your phone is out of vibrate mode.");
                                    mBuilder.setTicker("Gtone- Phone out of  vibrate mode.");
                                } else {
                                    mBuilder.setContentTitle("Your phone is out of normal mode.");
                                    mBuilder.setTicker("Gtone- Phone out of normal mode.");
                                }


                                NotificationManager mNotifyMgr =
                                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


                                Notification note = mBuilder.build();

                                note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


                                // Builds the notification and issues it.
                                mNotifyMgr.notify(0, note);
                                cm.putdata(false,context);
                                cm.removetemp(context,usersetting.tlat,usersetting.tlong);
                              //  MainActivity.Notified = false;
                              //  Log.i("Notified needed? ", " Let me do: " + MainActivity.Notified);

                        }
                    }

            }*/


        }
        Log.i("Notified? ", " Out; " + notif);


    }

    public void notifyuser(Context context) {
        //Log.i("inside notify user? ", "yes!");

        AudioManager adm = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        ringstate = adm.getRingerMode();

        Intent i = new Intent(context.getApplicationContext(), restoreAudio.class);
        i.putExtra("com.bewtechnologies.gtone.restore", ringstate);
        restoreAudioState = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
/*
        MainActivity.Notified = true;
        MainActivity.flat=elat;
        MainActivity.flong=elong;*/

        //marking
        usersetting cm = new usersetting();
        cm.savecoord(context,elat,elong,1);




        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //save time
        //  MainActivity.timeline=System.currentTimeMillis();


        //notification

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .addAction(R.drawable.next, "Undo?", restoreAudioState)
                        .setContentTitle("alarmreceive")

                        .setAutoCancel(true);




        MainActivity ma = new MainActivity();
        int savedRingerMode = ma.getSavedRingerMode(usersetting.dblatitude, usersetting.dblongitude, context);

        if (savedRingerMode == 0) {
            //Log.i("Inside main silent mode 1:", "yes");
            //  adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            mBuilder.setContentTitle("Your phone is now silent.");
            mBuilder.setTicker("Gtone- Phone is silent.");
        } else if (savedRingerMode == 1) {
            // Log.i("Inside main vibrate mode 1:", "yes");
            // adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            mBuilder.setContentTitle("Your phone is now on vibrate mode.");
            mBuilder.setTicker("Gtone- Phone is on vibrate mode.");
        } else {
            // Log.i("Inside main normal  mode 1:", "yes");
            // adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mBuilder.setContentTitle("Your phone is now on normal mode.");
            mBuilder.setTicker("Gtone- Phone is on normal mode.");
        }





        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Notification note = mBuilder.build();

        note.flags |= note.DEFAULT_LIGHTS | note.FLAG_AUTO_CANCEL;


        // Builds the notification and issues it.
        mNotifyMgr.notify(0, note);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();

            Thread.sleep(1000);
            r.stop();
            marked=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (savedRingerMode == 0) {
                //Log.i("Inside main silent mode 1:", "yes");
                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);

            } else if (savedRingerMode == 1) {
                // Log.i("Inside main vibrate mode 1:", "yes");
                adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //        mBuilder.setContentTitle("Your phone is now on vibrate mode.");
            } else {
                // Log.i("Inside main normal  mode 1:", "yes");
                adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //  mBuilder.setContentTitle("Your phone is now on normal mode.");
            }

        }


    }


}
