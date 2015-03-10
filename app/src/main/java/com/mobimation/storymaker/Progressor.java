package com.mobimation.storymaker;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

/**
 * Created by gunnar on 15-02-27.
 *
 * Progressor is the engine that interprets (runs) a StoryMaker script
 * and updates the UI thread of the Activity that launched it.
 * Progressor accomplishes the aggregated progression of scenes that make up a story.
 * Here a Scene means the playback of one media type with a start time and duration
 * and optionally some transition and interactivity effects.
 * TODO: The Progressor is supposed to direct media playback while interpreting a Storymaker
 * TODO: script. This needs to be done by posting commands onto the Activity UI thread.
 * TODO: Let´ see how well this works.
 *
 */
public class Progressor extends AsyncTask<Object, Object, Integer> implements MediaPlayer.OnPreparedListener {
    private static String TAG = Progressor.class.getName();
    VideoView vv;
    Player p;
    TextView progress;
    Script script;

    // Optional constructor that declare two parameters
    public Progressor(Player p, Script script) {
        this.p=p;
        vv=(VideoView)p.findViewById(R.id.video);
        this.script=script;
    }

    protected Integer doInBackground(Object... params) {
        // Process StoryMaker script
        int result=0;
        // Issue a video playback command, TODO:can be given further arguments
        publishProgress("Video","http://www.lilldata.se/suzuki/GT750M-1.flv");

        return new Integer(result);  // Becomes input to onPostExecute()
    }

    /**
     * Prepare
     */
    @Override
    protected void onPreExecute() {
        progress=(TextView)p.findViewById(R.id.progress);
        super.onPreExecute();
    }

    /**
     * Update Player scene
     *
     * onProgressUpdate implements a command set for media playback
     * according to passed arguments and operates on the UI thread of
     * the Player activity that launched the AsyncTask.
     * The first object in the variable amount of arguments represents an op code.
     * Based on the particular operation the arguments that follow
     * are of variable types and numbers depending on operation.
     * The code that implements each operation knows what parameters are expected
     * and will load and cast these into the expected types.
     *
     * @param values
     * TODO: Determine parameters
     */
    @Override
    protected void onProgressUpdate(Object... values) {
        // ====== Command interpreter begins
        if (((String)values[0]).equals("Video")) {
            vv.requestFocus();
            vv.setVideoURI(Uri.parse((String)values[1]));
            // vv.setVideoURI(Uri.parse(scriptName));
            vv.setOnPreparedListener(this);
            vv.start();
            Log.d(TAG, "Video playback starts");
            final Handler m_handler;
            m_handler = new Handler();
            Runnable onEverySecond=null;
            onEverySecond = new Runnable() {
                @Override
                public void run() {
                        Long tv=new Long(vv.getCurrentPosition());
                        if (vv.isPlaying()) {
                            progress.setText(getTimeString(tv));
                            m_handler.postDelayed(this, 1000);
                        }
                }
            };
            progress.postDelayed(onEverySecond, 1000);
        }
        else
            Log.e(TAG,"Unexpected OPCODE:"+(String)values[0]);
        // ====== Command interpreter ends

        super.onProgressUpdate(values);
    }

    /**
     *  Do cleanup and what else
     */
    protected void onPostExecute(Integer result) {
        // mImageView.setImageBitmap(result);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    static String getTimeString(Long millis) {
       return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}