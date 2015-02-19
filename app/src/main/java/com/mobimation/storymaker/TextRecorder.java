package com.mobimation.storymaker;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textrecorder_layout);
        /**
         * Load font for Text Recorder label
         */
        TextView labelTextRecorder = (TextView) findViewById(R.id.labelTextRecorder);
        labelTextRecorder.setTypeface(getFont("fonts/BlackOpsOne-Regular.ttf"));
    }
    @Override
    public int record(String name) {
        return 0;
    }

    private Typeface getFont(String fontName) {
        return Typeface.createFromAsset(getAssets(), fontName);
    }



}
