package com.mobimation.storymaker.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Storage manages all aspects of storing media artifacts produced by the
 * app user (camera photos,video,audio, and text) on either app private,
 * public local storage, or remote location by ftp.
 *
 * Created by gunnar on 15-04-10.
 */
public class Storage {
    private static final String TAG = Storage.class.getSimpleName();
    private final Type type;
    private Context context;

    public enum Type {
        PRIVATE,    // App private storage
        INTERNAL,   // Device built-in storage
        CARD,       // On SD card of device
        REMOTE      // On remote server (ftp,http)
    }
    
    public Storage(Context context, Type type) {
        this.type=type;
        this.context=context;
    }

    public File getLocalRoot() {
        switch (type) {
            case PRIVATE:
                return context.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE);
                // return context.getDir("Home Folder", Context.MODE_PRIVATE).getAbsolutePath();
            case INTERNAL:
                return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                // return Environment.getExternalStorageDirectory();
            case CARD:
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            case REMOTE:
        }
        return null;
    }
}
