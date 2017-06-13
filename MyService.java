package com.jordan.lucie.utrackapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Service class
 * @author Group 1
 * @version 1.0
 *
 * Inspired by android developer website
 */

public class MyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    try {
                        Thread.sleep(600000);
                        sendNotification();
                        Log.d("", " RUNS");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();
        return START_STICKY;
    }

    public void sendNotification(){
        Context context = getApplicationContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //put the icon
        mBuilder.setSmallIcon(R.drawable.img_location);
        //put the title
        mBuilder.setContentTitle("UTrack App");
        //put some text
        mBuilder.setContentText("Check your new locations!");

        Notification notification = mBuilder.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(8, notification);
    }

}
