package com.mobimation.storymaker;

import android.app.Activity;

/**
 * Created by gunnarforsgren on 15-02-16.
 *
 * A VideoRecorder manages the recording of a Video Artifact.
 * This determines the initial content of a Video Artifact which is later
 * available for editing or Clip operations.
 */
public class VideoRecorder extends Activity implements Recorder {
    private static String TAG = VideoRecorder.class.getName();
    @Override
    public int record(String name) {
        return 0;
    }
}
