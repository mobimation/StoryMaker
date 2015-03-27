package com.mobimation.storymaker;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gunnarforsgren on 2015-03-25.
 *
 * The SceneHandler extends Handler with managing of paused
 * operation.
 */
public class SceneHandler extends Handler {
    private static String TAG = SceneHandler.class.getName();
    private Timer scheduleTimer;
    private enum State { IDLE, SCHEDULED, RUNNING };
    private State state=State.IDLE;
    private Long paused=0L; // Time when paused
    private Long start=0L;  // Time when job was scheduled
    private Long delay=0L;  // Delay until running
    private Long remain=0L; // Time remaining until run when paused
    private Runnable r;


    public SceneHandler() {
        super();
    }

    public SceneHandler(Handler.Callback c) {
        super(c);
    }

    public SceneHandler(Looper l) {
        super(l);
    }

    public SceneHandler(Looper l, Handler.Callback c) {
        super(l,c);
    }

    public boolean postTimed(Runnable r, long delayMillis) {
        this.r=r;
        start = SystemClock.currentThreadTimeMillis();
        state=State.SCHEDULED;
        delay=delayMillis;  // Remember delay
        return postDelayed(r,delayMillis);
    }

    /**
     * Paused scheduling.
     */
    public void pause() {
        if (r!=null) {
            this.removeCallbacks(r);
            paused = SystemClock.currentThreadTimeMillis();
            remain = paused - delay - start;
        }
    }

    /**
     * Resume paused scheduling where new delay is what remains of
     * the original delay from time of pausing.
     */
    public void resume() {
        if (r!=null) {
            if (remain > 0) {  // If we need to schedule
                this.postDelayed(r, remain);
                Log.d(TAG, r.toString() + " - Rescheduling for "+remain+ "ms.");
            }
            else
                Log.d(TAG,r.toString()+" - Timed out by" + remain + " ms. Resume not needed");
        }
    }

    private TimerTask tt=new TimerTask() {
        @Override
        public void run() {
          Log.d(TAG,"Timer task runs");
        }
    };
}