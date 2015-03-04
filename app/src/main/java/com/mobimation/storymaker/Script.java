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
import java.util.ArrayList;

/**
 * Created by gunnar on 15-02-27.
 *
 * A class that fetches and holds the content of a Storymaker script.
 * This data becomes processed by the Progressor activity that
 * play content according to Script instructions.
 */
public class Script {
    Uri script;
    String content;
    ArrayList<String> list;
    private static String TAG = Script.class.getName();
    // Default local script
    public Script(Context c) {
        Resources res = c.getResources();
        list=new ArrayList<String>();
        String line;
        InputStream is = res.openRawResource(R.raw.promo);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            try {
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException ioe) {
                Log.e(TAG, ioe.getMessage());
            }
        } catch(UnsupportedEncodingException uee) {
            Log.e(TAG,uee.getMessage());
        }

    }

    /**
     * Return       the content of a specific script line.
     * @param       lineNumber A specific string line of the script
     * @return      The tring of that script line
     * @see         com.mobimation.storymaker.Progressor
     *
     */
    public String getLine(int lineNumber) {
        if (list.size()>lineNumber)
          return list.get(lineNumber);
        else
          return null;  // Requested line number outside range
    }

    /**
     * Return amount of lines of script
     * @return
     */
    public int lines() {
        return list.size();
    }
}
