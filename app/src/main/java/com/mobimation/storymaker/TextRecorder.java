package com.mobimation.storymaker;

import android.app.Activity;

/**
 * Created by gunnarforsgren on 15-02-16.
 *
 * A TextRecorder manages the recording of text into an Artifact.
 *
 * This determines the initial content of a Text Artifact which is later
 * available for editing or Clip operations.
 */
public class TextRecorder extends Activity implements Recorder {

    @Override
    public int record(String name) {
        return 0;
    }
}
