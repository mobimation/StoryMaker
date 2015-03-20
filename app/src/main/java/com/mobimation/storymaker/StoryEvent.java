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
    // LABEL, TEXT, AUDIO, IMAGE, VIDEO, PROMPT
    Handler audioInsertionHandler  = new Handler();
    Handler audioTerminateHandler  = new Handler();
    Handler videoInsertionHandler  = new Handler();
    Handler videoTerminateHandler  = new Handler();
    Handler labelInsertionHandler  = new Handler();
    Handler labelTerminateHandler  = new Handler();
    Handler textInsertionHandler   = new Handler();
    Handler textTerminateHandler   = new Handler();
    Handler promptInsertionHandler = new Handler();
    Handler promptTerminateHandler = new Handler();
    Handler imageInsertionHandler  = new Handler();
    Handler imageTerminateHandler  = new Handler();
    EventType type;
    String TAG=this.getClass().getName();

    public StoryEvent(EventType type, Uri uri, long start, long duration) {
        this.type=type;
        this.start=start;
        this.duration=duration;
    }

    /**
     * schedule() will launch and terminate a media insertion
     */
    public void schedule() {
        switch (type) {
            case VIDEO:
                final Runnable videoInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Video insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable videoTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Video termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        videoTerminateHandler.postDelayed(videoTermination,duration); // Launch with delay
                        Log.d(TAG, "Video termination scheduled.");
                    }
                };
                videoInsertionHandler.postDelayed(videoInsertion,start); // Launch with delay
                Log.d(TAG, "Video insertion scheduled.");
                break;
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
                        audioTerminateHandler.postDelayed(audioTermination,duration); // Launch with delay
                        Log.d(TAG, "Audio termination scheduled.");
                      }
                };
                audioInsertionHandler.postDelayed(audioInsertion,start); // Launch with delay
                Log.d(TAG, "Audio insertion scheduled.");
                break;
            case LABEL:
                final Runnable labelInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Label insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable labelTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Label termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        labelTerminateHandler.postDelayed(labelTermination,duration); // Launch with delay
                        Log.d(TAG, "Label termination scheduled.");
                    }
                };
                labelInsertionHandler.postDelayed(labelInsertion,start); // Launch with delay
                Log.d(TAG, "Label insertion scheduled.");
                break;
            case TEXT:
                final Runnable textInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Text insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable textTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Text termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        textTerminateHandler.postDelayed(textTermination,duration); // Launch with delay
                        Log.d(TAG, "Text termination scheduled.");
                    }
                };
                textInsertionHandler.postDelayed(textInsertion,start); // Launch with delay
                Log.d(TAG, "Text insertion scheduled.");
                break;
            case IMAGE:
                final Runnable imageInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Image insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable imageTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Image termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        imageTerminateHandler.postDelayed(imageTermination,duration); // Launch with delay
                        Log.d(TAG, "Image termination scheduled.");
                    }
                };
                imageInsertionHandler.postDelayed(imageInsertion,start); // Launch with delay
                Log.d(TAG, "Image insertion scheduled.");
                break;
            case PROMPT:
                final Runnable promptInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Prompt insertion begins");
                        // TODO: Add playback launch
                        // Schedule termination
                        final Runnable promptTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Prompt termination begins");
                                // TODO: Add playback termination
                            }
                        };
                        promptTerminateHandler.postDelayed(promptTermination,duration); // Launch with delay
                        Log.d(TAG, "Prompt termination scheduled.");
                    }
                };
                promptInsertionHandler.postDelayed(promptInsertion,start); // Launch with delay
                Log.d(TAG, "Audio insertion scheduled.");
                break;
            default:
                Log.e(TAG, "Unsupported event type");
                break;
        }
    }
}