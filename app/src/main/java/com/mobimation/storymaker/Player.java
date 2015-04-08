package com.mobimation.storymaker;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 *
 * Player plays back a Story using a StoryMaker script as input.
 * This orchestrates a multi media presentation assembled in runtime from
 * various media types (video, photos, audio, text).
 * The script contains scene instructions that introduce material
 * at a point in time, with optional effects such as fade in/out and
 * bounded box constraints, each with a duration. Many such scenes can
 * play simultaneously in a visually layered fashion,
 * together making up the complete presentation, thus effectively
 * accomplishing runtime editing from media artifacts.
 * A Scene concerns one playback duration of a single media type.
 * One or several parallel Scene progressions with their individual
 * start time, duration and overlapping make up a complete Story.
 *
 * TODO: Player is currently the test entry point for trying out the script processing.
 *
 */
public class Player extends Activity
{
    VideoView vv;
    private static final String TAG = Player.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Play in fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);
        RelativeLayout rl=(RelativeLayout)this.findViewById(R.id.FrameLayout1);
        rl.setDrawingCacheEnabled(true);
        // Test run local "promo.sm" test script.
        new Progressor().execute(this,new Script(this,R.raw.promo));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
