package com.mobimation.storymaker;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Camera related operations. The capture of camera images for the manufacturing of
 * various photo artifacts used in Story production.
 * TODO: An attempt here to use only the Camera2 API for Android 5 upwards
 * TODO: while earlier Android build levels use the old API.
 *
 * Created by gunnarforsgren on 15-04-14.
 */
public class Camera {
    Context context=null;
    private static final String TAG = Camera.class.getSimpleName();

    public Camera(Context c) {
      this.context=c;
    }

    private void test() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //========= N E W   C A M E R A   A P I ======================
            // Use new Camera API and offer a more advanced capability
            CameraManager cmgr = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] cameras = cmgr.getCameraIdList();
                Log.d(TAG,cameras.length+" devices exists.");
                for (int i=0; i<cameras.length; i++)
                    Log.d(TAG,"Camera="+cameras[i]);
            }
            catch (CameraAccessException cae) {
                Log.e(TAG,"Camera access exception "+cae.toString());
            }
        } else {
            //========= O L D   C A M E R A   A P I ======================
            // Use old Camera API and offer functionality it can support
            android.hardware.Camera camera=isCameraAvailiable();
            if (camera!=null) {
                // Try to do something
                android.hardware.Camera.Parameters parameters=camera.getParameters();
                List l=parameters.getSupportedVideoSizes();
                for (int n=0; n<l.size(); n++) {
                    Log.d(TAG,"Size="+l.toString());
                }
            }
        }
    }

    public static android.hardware.Camera isCameraAvailiable() {
        return android.hardware.Camera.open();
    }
}
