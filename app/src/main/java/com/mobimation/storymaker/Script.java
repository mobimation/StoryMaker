package com.mobimation.storymaker;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gunnar on 15-02-27.
 *
 * A class that holds information and logic related to
 * accessing the content of a Storymaker script.
 */
public class Script {
    Uri script;
    String content;
    private static String TAG = Script.class.getName();
    // Default local script
    public Script(Context c) {
        Resources res = c.getResources();
        String line;
        InputStream is = res.openRawResource(R.raw.promo);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            try {
                while ((line = reader.readLine()) != null) {
                    content=content+line;
                }
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage());
            }
        } catch(UnsupportedEncodingException uee) {
            Log.e(TAG,uee.getMessage());
        }

    }
}
