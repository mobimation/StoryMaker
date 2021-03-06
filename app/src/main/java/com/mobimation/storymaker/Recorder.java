package com.mobimation.storymaker;

/**
 * Created by gunnarforsgren on 15-02-16.
 *
 * A Recorder is a base class for Recording something.
 */
public interface Recorder {
    int start();
    int pause();
    int stop();
    int delete();
    int save(String destination);
}
