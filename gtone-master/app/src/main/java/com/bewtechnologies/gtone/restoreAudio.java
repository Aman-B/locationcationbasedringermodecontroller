package com.bewtechnologies.gtone;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

/**
 * Created by Aman  on 7/19/2015.
 */
public class restoreAudio extends Service {

    AudioManager adm;

    public static double marklat;
    public static double marklong;


    @Override
    public void onCreate() {
        super.onCreate();
     //   Log.i("Inside rA oncreate?", "yes we are");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        marklat=AlarmReceiver.elat;
        marklong=AlarmReceiver.elong;
       // Log.i("mark latlng :"," "+marklat+" "+marklong);

        handleCommand(intent);


        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleCommand(Intent intent) {


      //  Log.i("Inside restore Audio?", "oh,Yes we are");

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        adm = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(intent!=null)
        {


            int mode = intent.getIntExtra("com.bewtechnologies.gtone.restore", 0);


            if (mode == AudioManager.RINGER_MODE_NORMAL)

            {
                //Log.i("Inside ringer mode normal?", "oh,Yes we are");
                adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                //nm.cancel(0);

            }

            else if (mode == AudioManager.RINGER_MODE_VIBRATE)
            {
                //Log.i("Inside ringer mode vibrate?", "oh,Yes we are");
                adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                //nm.cancel(0);
            }
            else
            {
                //Log.i("Inside silent ringer mode?", "oh,Yes we are");
                adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //nm.cancel(0);
            }

        }

        nm.cancel(0);

    }


}






