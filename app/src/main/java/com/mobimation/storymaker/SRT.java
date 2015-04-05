package com.mobimation.storymaker;

import android.net.Uri;

/**
 * Created by gunnar on 15-03-03.
 */

public class SRT {
 /**
  *
  *   1
  *           00:02:17,440 --> 00:02:20,375
  *   Senator, we're making
  *   our final approach into Coruscant.
  *
  *    2
  *           00:02:20,476 --> 00:02:22,501
  *   Very good, Lieutenant.
  *
  */
 Entry[]data;
 private static final String TAG = SRT.class.getSimpleName();


    // Return SRT duration syntax
 public String duration(String hour, String minute, String second, String milli) {
  return hour+":"+minute+":"+second+","+milli;
 }

    // Construct SRT data
    // TODO: The embryo of a state machine that parse a SRT file into a structure for use during video playback.
    public SRT(String[]lines) {
        int p=0;
        String duration="";
        String lin1="";
        String lin2="";
        int state=0;
        for (int x=0; x<lines.length; x++) {
            if ((state==0)&& (lines[x].isEmpty()))
                state=0;  // Skip any leading empty lines
            else {
                if (state==1) {  // Expect pos
                    p = new Integer(lines[x].trim()).intValue();
                    state++;
                }
                else if (state==2) {
                    // Read start/end pos
                    duration=lines[x].trim();
                    state++;
                }
                else if (state==3) {
                    // Read first subtitle line
                    lin1=lines[x].trim();
                    state++;
                }
                else if (state==4) {
                    lin2=lines[x].trim();
                    data[p++]=new Entry(p,duration,lin1,lin2);
                    if (lin2.isEmpty())
                        state=1;
                    else
                        state=0;
                }
            }
        }
    }

    class Entry {
        int pos;
        String duration;
        String l1;
        String l2;

        public Entry(int pos,String duration, String l1, String l2) {
            this.pos=pos;
            this.duration=duration;
            this.l1=l1;
            this.l2=l2;
        }
    }

}
