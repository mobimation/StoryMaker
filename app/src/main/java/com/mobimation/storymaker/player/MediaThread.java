package com.mobimation.storymaker.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by gunnarforsgren on 2015-03-25.
 */
class MediaThread extends Thread {
    private static final String TAG = MediaThread.class.getSimpleName();
    public Handler mHandler;

    @Override
    public void run() {
        // Initialize the current thread as a Looper
        // (this thread can have a MessageQueue now)
        Looper.prepare();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                try {
                    this.getLooper().wait();
                }
                catch (InterruptedException ie) {

                }
            }
        };

        Message msg = Message.obtain();

// Read the documentation on the properties used below
// http://developer.android.com/reference/android/os/Message.html

        msg.arg1 = 100;
        msg.arg2 = 200;

        msg.obj = "String";

        Bundle bundle = new Bundle();
        bundle.putString("foo", "bar");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        mHandler.notify();

        // Run the message queue in this thread
        Looper.loop();
    }
}