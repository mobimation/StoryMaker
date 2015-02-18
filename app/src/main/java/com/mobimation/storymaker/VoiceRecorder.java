package com.mobimation.storymaker;

import android.app.Activity;

/**
 * Created by gunnarforsgren on 15-02-16.
 *
 * A VideoRecorder manages the recording of a Voice (audio) Artifact.
 * This determines the initial content of a Voice Artifact which is later
 * available for editing or Clip operations.
 */
public class VoiceRecorder extends Activity implements Recorder {
    @Override
    public int record(String name) {
        return 0;
    }
}
