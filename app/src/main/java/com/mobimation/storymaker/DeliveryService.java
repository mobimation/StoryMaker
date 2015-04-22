package com.mobimation.storymaker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * DeliveryService manages the transport of user created media artifacts
 * to a central repository. It works on a backlog of not yet transferred
 * material where transfers that become interrupted by power issues or
 * connectivity loss are retried until successful whenever the DeliveryService
 * is up and running. It is intended that a backlog editor be implemented that
 * allow user clearing of artifacts, remote or local that the user decide not to transfer.
 */
public class DeliveryService extends Service {
    private static String TAG = DeliveryService.class.getName();

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.service_started;

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    private long elapsedTime;
    public static long START_TIME = 0;
    public static int TIMER_INTERVAL = 1000; // Once per second

    private Handler timeHandler = new Handler();

    public DeliveryService() {
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public DeliveryService getService() {
            return DeliveryService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        START_TIME = System.currentTimeMillis();
        timeHandler.postDelayed(timerRunnable, TIMER_INTERVAL);

      Log.d(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start id " + startId + ": Intent=" + intent);

        /* ride = intent.getExtras().getInt("ride", 0);

        getLocation();
        trip = createTrip(); */

        showNotification();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        transferEnds();
        timeHandler.removeCallbacks(timerRunnable);
        Log.d(TAG,"onDestroy()");
    }

    /**
     * Run a timer that periodically updates the UI
     */
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            setElapsedTime(getElapsedTime() + TIMER_INTERVAL);
            timeHandler.postDelayed(this, TIMER_INTERVAL);
            updateNotification();
        }
    };
    public long getElapsedTime() {
        return elapsedTime;
    }

    private void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        /*
        // TODO Implement service communication interface
        Intent contentIntent = new Intent(this, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, contentIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        mBuilder = new NotificationCompat.Builder(this).setContentIntent(contentPendingIntent).setSmallIcon(R.drawable.elcykel_and_bike).setOngoing(true)
                .setTicker(getString(R.string.local_service_started)).setContentInfo("Eck").setContentTitle("Loggar resa")
                .setContentText(String.format("Tid: %.0f min Längd: %.2f km", (float) (getElapsedTime() / 1000 / 60), 0f));

        Notification note = mBuilder.build();
        startForeground(NOTIFICATION, note);
*/

    }

    private void updateNotification() {
        Log.d(TAG,"Transfer, updating UI..");
        setElapsedTime(getElapsedTime()+TIMER_INTERVAL);  // Increment time

        // Experimental, simple test of stopping
        if (getElapsedTime()>4000)
            transferEnds();

        // TODO Insert code for updating UI
        /* mBuilder = mBuilder.setContentText(String.format("Tid: %.0f min Längd: %.2f km", (float) (getElapsedTime() / 1000 / 60), trip.getDistance() / 1000));
        Notification note = mBuilder.build();
        startForeground(NOTIFICATION, note);  */
    }

    private void transferEnds() {
            long END_TIME = System.currentTimeMillis();
            elapsedTime = (END_TIME - START_TIME);
            Log.d(TAG,"Transfer done, "+elapsedTime+" milliseconds");
            timeHandler.removeCallbacks(timerRunnable);
            setElapsedTime(0);

        // broadcastDataChanged();

    }
}
