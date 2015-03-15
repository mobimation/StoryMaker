package com.mobimation.storymaker;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import java.net.URL;
import java.util.StringTokenizer;
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
    VideoView vv;      // The video player view
    Player player;     // The player
    TextView progress; // Video playback progress indicator
    Script script;     // The script we are processing
/*
    // Optional constructor that declare two parameters
    public Progressor(Player p, Script script) {
        this.player=p;
        vv=(VideoView)p.findViewById(R.id.video);
        this.script=script;
    }
*/
    protected Integer doInBackground(Object... params) {
        // Process StoryMaker script
        this.player=(Player)params[0];
        vv=(VideoView)player.findViewById(R.id.video);
        this.script=(Script)params[1];
        progress=(TextView)player.findViewById(R.id.progress);
        int result=0;
        String line;
        Log.d(TAG,"Script has "+script.lines()+" lines.");
        Log.d(TAG,"-------Script processing begins------");
        // Script processing loop
        for (int x=0; x<script.lines(); x++) {
            line=script.getLine(x);
            // Interpret line
            if (line==null) {
               Log.e(TAG, "null string at line " + x + " reading script !");
               break;
            }
            else
            if (line.length()==0) {
               Log.e(TAG,"empty string at line "+x+" reading script !");
               break;
            }
            else
            if (!line.startsWith("#")) {
              Log.d(TAG,"Processing script line: "+line);
                StringTokenizer tokens = new StringTokenizer(line, " ");
                String startTime=tokens.nextToken();
                // For now skip waiting for start time and go on with operation
                String opcode=tokens.nextToken();
                if (opcode.toLowerCase().equals("video")) {
                   String url=tokens.nextToken();
                   String duration=tokens.nextToken();
                   publishProgress(opcode,url,startTime,duration);
                }
            }
            else
              Log.v(TAG,"Skipping script comment: "+line);
        }
        Log.d(TAG,"-------Script processing done-------");
        // For testing, hardcode a video playback command, TODO:can be given further arguments
        // publishProgress("Video","http://www.lilldata.se/suzuki/GT750M-1.flv");

        return new Integer(result);  // Becomes input to onPostExecute()
    }

    /**
     * Prepare

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }
     */

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
        Log.d(TAG,"onProgressUpdate(): param length="+values.length);
        if (((String)values[0]).toLowerCase().equals("video")) {
            //-----------VIDEO COMMAND------------------------
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
        } //------------END OF VIDEO COMMAND------------------
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