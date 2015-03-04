package com.mobimation.storymaker;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.VideoView;

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
public class Progressor extends AsyncTask<VideoView, Void, Integer> implements MediaPlayer.OnPreparedListener {
    private static String TAG = Progressor.class.getName();

    protected Integer doInBackground(VideoView... params) {

        int result=0;
        VideoView vv=params[0];
        vv.requestFocus();
        // vv.setVideoURI(Uri.parse("http://www.lilldata.se/suzuki/GT750M-1.flv"));
        vv.setVideoURI(Uri.parse("http://laidback.tv/video/goldie.mp4"));
        vv.setOnPreparedListener(this);
        vv.start();
        Log.d(TAG, "Video playback starting..");

        return new Integer(result);  // Becomes input to onPostExecute()
    }

    /**
     * Prepare
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Update UI thread playback scenery
     * @param values
     * TODO: Determine parameters
     */
    @Override
    protected void onProgressUpdate(Void... values) {
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
}
