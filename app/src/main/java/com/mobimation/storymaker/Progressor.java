package com.mobimation.storymaker;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
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
public class Progressor extends AsyncTask<Object, Object, Integer>
        implements  MediaPlayer.OnPreparedListener,
                    MediaPlayer.OnErrorListener,
                    MediaPlayer.OnInfoListener,
                    PromptResponse {
    private static final String TAG = Progressor.class.getSimpleName();
    VideoView vv;      // The video player view
    CountDownLatch sync=null;  // Video ready synchronization
    int volume=0;
    Player player;     // The player
    TextView progress; // Video playback progress indicator
    Script script;     // The script we are processing
    private PromptResponse promptListener;

    public void setPromptListener(PromptResponse listener) {
        promptListener=listener;
    }
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
        setPromptListener(this);
        this.player=(Player)params[0];
        vv=(VideoView)player.findViewById(R.id.video);
        this.script=(Script)params[1];
        // progress=(TextView)player.findViewById(R.id.progress);
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
                if (opcode.toLowerCase().equals("video")) {  // VIDEO event
                   String url=tokens.nextToken();
                   String duration=tokens.nextToken();
                   publishProgress(opcode,url,startTime,duration); // Run scene command on UI thread
                   // Returns here immediately
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

    @Override
    protected void onPreExecute() {
        sync=new CountDownLatch(1);
        super.onPreExecute();
    }

    /**
     * Update Player scene
     *
     * A call to publishProgress() ends up here.
     * onProgressUpdate() implements a command set for media playback
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
            Uri u=Uri.parse((String) values[1]);
            StoryEvent se = new StoryEvent(player,sync,EventType.VIDEO,u,0L,40000L);
            // TEST: Schedule overlay
            StoryEvent se2= new StoryEvent(player,sync,EventType.PROMPT,R.id.player_overlay,8000,0);
            se.schedule();
            se2.schedule();  // TEST: Schedule overlay
            // setPromptListener(this);
            // se2.schedule();

            // ------Launch video overlay--------------------
            // olabel("test", 0, 0, 0, 0L, true);
/*
            // ------Launch video progress indicator---------
            final Handler m_handler;
            m_handler = new Handler();
            Runnable onEverySecond=null;
            onEverySecond = new Runnable() {
                @Override
                public void run() {
                        Log.d(TAG,"progress..");  // TODO: Timing: this delay makes indication consistent..
                        Long tv=new Long(vv.getCurrentPosition());
                        if (vv.isPlaying()) {
                            progress.setText(getTimeString(tv));
                            m_handler.postDelayed(this, 1000);
                        }
                }
            };
           m_handler.postDelayed(onEverySecond, 1000);
            */
     //       progress.postDelayed(onEverySecond, 1000);
            // -----End of video progress indicator launch----

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
        Log.d(TAG,"onPostExecute() result="+result);
    }

    /**
     * We arrive here when video is ready to begin playing
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
       Float vo=vol(volume);
       mp.setVolume(vo, vo);
       vv.start();
    }

    static String getTimeString(Long millis) {
       return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
    // Return a logarithmic MediaPlayer audio volume from a linear audio volume 0-100
    float vol(int value) {
        return 1-(float)(Math.log(100-value)/Math.log(100));
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG,"onError() what="+what+" extra="+extra);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG,"onInfo()  what="+what+" extra="+extra);
        return false;
    }

    /**
     *
     * Schedule placing a label on top of the parent View
     * Optionally animate a fade in/out transition
     *
     * @param text          Text label to be displayed
     * @param x             Horizontal start in percentage of parent width
     * @param y             Vertical start in percentage of parent height
     * @param startMs       Delay in ms until start from calling this method 0=immediately
     * @param durationMs    Duration in ms until taken down 0=permanent
     * @param animate       true=fade in/out false=no animation
     * @return              0=ok
     */
    private void olabel(String text, int x, int y,int startMs, long durationMs, boolean animate) {
        // On demand (lazy) inflating of a layout
        TextView tv;
        ViewStub stub = (ViewStub) player.findViewById(R.id.player_overlay);
        View inflated = stub.inflate();

        inflated.setVisibility(View.VISIBLE);
    }

    /**
     * Schedule a StoryEvent insertion.
     * During interpretation of a script any StoryEvent that occurs are scheduled
     * to be carried out at a certain point in time. When that time is reached
     * a handler for the type of effect is launched to manage the playback
     * of the particular type of event.
     * @param event
     */
    private void schedule(StoryEvent event) {

    }

    /**
     *
     * @param value Generic type, here we expect an int value.
     */
    @Override
    public void promptResponse(Object... value) {
        if (value.length>0) {
            int i=((Integer)value[0]).intValue();
            Log.d(TAG,"Prompt response="+i);
        }
    }
}