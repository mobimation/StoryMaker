package com.mobimation.storymaker;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by gunnarforsgren on 15-03-19.
 *
 * The StoryEvent holds information about a piece of media to be
 * played back at a certain point in time.
 * This object is used by an event scheduler that schedules both
 * a launch and termination handler.
 */
public class StoryEvent {
    Runnable audioInsertion;
    long start;
    long duration;
    Handler insertionHandler = new Handler();
    Handler terminateHandler = new Handler();
    EventType type;
    String TAG=this.getClass().getName();

    public StoryEvent(EventType type, Uri uri, long start, long duration) {
        this.type=type;
        this.start=start;
        this.duration=duration;
    }
    public void schedule() {
        switch (type) {
            case AUDIO:
                final Runnable audioInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Audio insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable audioTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Audio termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        terminateHandler.postDelayed(audioTermination,duration); // Launch with delay
                        Log.d(TAG, "Audio termination scheduled.");
                      }
                };
                insertionHandler.postDelayed(audioInsertion,start); // Launch with delay
                Log.d(TAG, "Audio insertion scheduled.");
                break;
            default:
                Log.e(TAG, "Unsupported event type");
                break;
        }
    }
}