package com.mobimation.storymaker.player;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.mobimation.storymaker.story.EventType;
import com.mobimation.storymaker.R;
import com.mobimation.storymaker.story.Script;

import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

/**
 * Created by gunnar on 15-02-27.
 *
 * Progressor is the engine that interprets (runs) a StoryMaker script
 * and updates the UI thread of the Activity that launched it.
 * Progressor accomplishes the aggregated progression of scenes that make up a story.
 * Here a Scene means the playback of one media type with a start time and duration
 * and optionally some transition and interactivity effects.
 *
 */
public class Progressor extends AsyncTask<Object, Object, Integer>  {

    private static final String TAG = Progressor.class.getSimpleName();
    CountDownLatch sync=null;  // Video ready synchronization
    int volume=0;
    Player player;     // The player
    TextView progress; // Video playback progress indicator
    Script script;     // The script we are processing

    protected Integer doInBackground(Object... params) {
        // Process StoryMaker script
        this.player=(Player)params[0];
        this.script=(Script)params[1];
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

                switch(opcode) {
                    case "video":
                        String url=tokens.nextToken();
                        String duration=tokens.nextToken();
                        publishProgress(opcode,url,startTime,duration);
                        break;
                    case "meme":
                        String top=tokens.nextToken();
                        String bottom=tokens.nextToken();
                        long start=new Long(tokens.nextToken()).longValue();
                        long duration2=new Long(tokens.nextToken()).longValue();
                        String image=tokens.nextToken();
                        publishProgress(opcode,top,bottom,start,duration2,image);
                        break;
                    case "prompt":
                        String promptId=tokens.nextToken();
                        long start2=new Long(tokens.nextToken()).longValue();
                        long duration3=new Long(tokens.nextToken()).longValue();
                        publishProgress(opcode,promptId,start2,duration3);
                        break;
                    default:
                        Log.e(TAG,"Unexpected OPCODE:"+opcode);
                }
            }
            else
              Log.v(TAG,"Skipping script comment: "+line);
        }
        Log.d(TAG,"-------Script processing done-------");

        return new Integer(result);  // Becomes input to onPostExecute()
    }

    @Override
    protected void onPreExecute() {
        sync=new CountDownLatch(1);  // Overlay threads to wait for video prepared
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
        Log.d(TAG,"onProgressUpdate(): param length="+values.length);
        String tag = ((String)values[0]).toLowerCase();
        switch(tag) {
            case "video":
                Uri u=Uri.parse((String) values[1]);
                StoryEvent se = new StoryEvent(player,sync, EventType.VIDEO,u,0L,0L);
                se.schedule();
            break;
            case "prompt":
                StoryEvent se2= new StoryEvent(player,sync,EventType.PROMPT, R.id.player_overlay,8000,0);
                se2.schedule();
            break;
            default:
                Log.e(TAG,"Unexpected OPCODE:"+(String)values[0]);
        }

        super.onProgressUpdate(values);
    }

    /**
     *  Do cleanup and what else
     */
    protected void onPostExecute(Integer result) {
        Log.d(TAG,"onPostExecute() result="+result);
    }
}