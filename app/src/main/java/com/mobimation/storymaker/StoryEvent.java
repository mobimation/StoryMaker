package com.mobimation.storymaker;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by gunnarforsgren on 15-03-19.
 *
 * The StoryEvent holds information about a piece of media to be
 * played back at a certain point in time.
 * This object is used by an event scheduler that schedules both
 * a launch and termination handler.
 */
public class StoryEvent
        /*
        implements  MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener   */
{
    int volume=50;
    Player player=null;
    StoryEvent event=null;
    Uri uri=null;
    Runnable insertion=null;
    Runnable termination=null;
    VideoView vv=null;
    TextView progress=null;
    MediaPlayer mp=null;
    long start;
    long duration;
    // LABEL, TEXT, AUDIO, IMAGE, VIDEO, PROMPT
    SceneHandler audioInsertionHandler   = new SceneHandler();
    SceneHandler audioTerminateHandler   = new SceneHandler();
    SceneHandler videoInsertionHandler   = new SceneHandler();
    SceneHandler videoTerminateHandler   = new SceneHandler();
    SceneHandler labelInsertionHandler   = new SceneHandler();
    SceneHandler labelTerminateHandler   = new SceneHandler();
    SceneHandler textInsertionHandler    = new SceneHandler();
    SceneHandler textTerminateHandler    = new SceneHandler();
    SceneHandler promptInsertionHandler  = new SceneHandler();
    SceneHandler promptTerminateHandler  = new SceneHandler();
    SceneHandler imageInsertionHandler   = new SceneHandler();
    SceneHandler imageTerminateHandler   = new SceneHandler();
    SceneHandler overlayInsertionHandler = new SceneHandler();
    SceneHandler overlayTerminateHandler = new SceneHandler();
    final EventType type;
    int resource=0;
    String TAG=this.getClass().getName();

    public StoryEvent(Player player, EventType type, Uri uri, long start, long duration) {
        this.player=player;
        this.type=type;
        this.start=start;
        this.duration=duration;
        this.event=this;
        this.uri=uri;
        vv=(VideoView)player.findViewById(R.id.video);
        progress=(TextView)player.findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
    }

    // Constructor for overlay
    public StoryEvent(Player player, EventType type, int resource, long start, long duration) {
        this.player=player;
        this.type=type;
        this.resource=resource;
        this.start=start;
        this.duration=duration;
        vv=(VideoView)player.findViewById(R.id.video);
    }

    public void pause() {
        switch (type) {
            case VIDEO:
                videoInsertionHandler.pause();
                videoTerminateHandler.pause();
                Log.d(TAG,"Video handlers paused");
                break;
            case AUDIO:
                audioInsertionHandler.pause();
                audioTerminateHandler.pause();
                Log.d(TAG, "Audio handlers paused");
                break;
            case LABEL:
                labelInsertionHandler.pause();
                labelTerminateHandler.pause();
                Log.d(TAG, "Label handlers paused");
                break;
            case TEXT:
                textInsertionHandler.pause();
                textTerminateHandler.pause();
                Log.d(TAG, "Text handlers paused");
                break;
            case IMAGE:
                imageInsertionHandler.pause();
                imageTerminateHandler.pause();
                Log.d(TAG, "Image handlers paused");
                break;
            case PROMPT:
                promptInsertionHandler.pause();
                promptTerminateHandler.pause();
                Log.d(TAG, "Prompt handlers paused");
                break;
            default:
                break;
        }
    }

    public void resume() {
        switch (type) {
            case VIDEO:
                videoInsertionHandler.resume();
                videoTerminateHandler.resume();
                Log.d(TAG,"Video handlers resumed");
                break;
            case AUDIO:
                audioInsertionHandler.resume();
                audioTerminateHandler.resume();
                Log.d(TAG, "Audio handlers resumed");
                break;
            case LABEL:
                labelInsertionHandler.resume();
                labelTerminateHandler.resume();
                Log.d(TAG, "Label handlers resumed");
                break;
            case TEXT:
                textInsertionHandler.resume();
                textTerminateHandler.resume();
                Log.d(TAG, "Text handlers resumed");
                break;
            case IMAGE:
                imageInsertionHandler.resume();
                imageTerminateHandler.resume();
                Log.d(TAG, "Image handlers resumed");
                break;
            case PROMPT:
                promptInsertionHandler.resume();
                promptTerminateHandler.resume();
                Log.d(TAG, "Prompt handlers resumed");
                break;
            default:
                break;
        }
    }
    /**
     * schedule() will launch and optionally terminate a media insertion
     */
    public void schedule() {
        switch (type) {
            case VIDEO:
                Runnable videoInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Video insertion begins at "+start);
 //                       final VideoView vv2=vv;
                        Log.d(TAG, "Using URL " + uri.toString());

                        vv.setVideoURI(uri);
     //                   vv2.setOnPreparedListener(event);
                        vv.requestFocus();
                        // vv2.start();  // Started by onPrepareHandler

                        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                        {

                            public void onPrepared(MediaPlayer mp)
                            {
                                Float vo=vol(volume);
                                mp.setVolume(vo, vo);
                                // progressDialog.dismiss();
                                vv.start();
                            }
                        });

                        if (duration>0) {
                            // Schedule termination
                            Runnable videoTermination = new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Video fake stopped at " + start + duration + " milliseconds.");
                                    // vv2.stopPlayback();
                                }
                            };
                            videoTerminateHandler.postDelayed(videoTermination, duration); // Launch with delay
                            Log.d(TAG, "Video termination with " + duration + " millisecond delay.");
                            termination = videoTermination;
                        }
                        else
                            Log.d(TAG,"No video termination");
                    }
                };
                videoInsertionHandler.postDelayed(videoInsertion, start); // Launch with delay
                insertion=videoInsertion;
                Log.d(TAG, "Video scheduled.");
                break;

            case AUDIO:
                final Runnable audioInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Audio insertion begins");
                        // TODO: Add playback launch
                        if (duration>0) {
                            // Schedule termination
                            final Runnable audioTermination = new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Audio termination begins");
                                    // TODO: Add playback termination
                                }
                            };
                            audioTerminateHandler.postDelayed(audioTermination, duration); // Launch with delay
                            Log.d(TAG, "Audio termination scheduled.");
                            termination = audioTermination;
                        }
                        else
                            Log.d(TAG,"No audio termination");
                      }
                };
                audioInsertionHandler.postDelayed(audioInsertion, start); // Launch with delay
                Log.d(TAG, "Audio scheduled.");
                insertion=audioInsertion;
                break;
            case LABEL:
                final Runnable labelInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Label insertion begins");
                        // TODO: Add playback launch
                        if (duration>0) {
                            // Schedule termination
                            final Runnable labelTermination = new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Label termination begins");
                                    // TODO: Add playback termination
                                }
                            };
                            labelTerminateHandler.postDelayed(labelTermination, duration); // Launch with delay
                            Log.d(TAG, "Label termination scheduled.");
                            termination = labelTermination;
                        }
                        else
                            Log.d(TAG,"No label termination");
                    }
                };
                labelInsertionHandler.postDelayed(labelInsertion, start); // Launch with delay
                Log.d(TAG, "Label scheduled.");
                insertion=labelInsertion;
                break;
            case TEXT:
                final Runnable textInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Text insertion begins");
                        // TODO: Add playback launch
                        if (duration>0) {
                            // Schedule termination
                            final Runnable textTermination = new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Text termination begins");
                                    // TODO: Add playback termination
                                }
                            };
                            textTerminateHandler.postDelayed(textTermination, duration); // Launch with delay
                            Log.d(TAG, "Text termination scheduled.");
                            termination = textTermination;
                        }
                        else
                            Log.d(TAG,"No text termination");
                    }
                };
                textInsertionHandler.postDelayed(textInsertion, start); // Launch with delay
                Log.d(TAG, "Text scheduled.");
                insertion=textInsertion;
                break;
            case IMAGE:
                final Runnable imageInsertion = new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Image insertion begins");
                        // TODO: Add playback launch
                        if (duration>0) {
                            // Schedule termination
                            final Runnable imageTermination = new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Image termination begins");
                                    // TODO: Add playback termination
                                }
                            };
                            imageTerminateHandler.postDelayed(imageTermination, duration); // Launch with delay
                            Log.d(TAG, "Image termination scheduled.");
                            termination = imageTermination;
                        }
                        else
                            Log.d(TAG,"No image termination");
                    }
                };
                imageInsertionHandler.postDelayed(imageInsertion, start); // Launch with delay
                Log.d(TAG, "Image scheduled.");
                insertion=imageInsertion;
                break;
            case PROMPT:
                overlay(R.id.player_overlay,0,0,2000,3000,true);
                // TODO: Handle prompt response
                break;
            default:
                Log.e(TAG, "Unsupported event type");
                break;
        }
    }
/*
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError() what=" + what + " extra=" + extra);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onInfo()  what=" + what + " extra=" + extra);
        return false;
    }
*/
    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    /*
    @Override
    public void onPrepared(MediaPlayer mp) {
        this.mp=mp;
        Float vo=vol(volume);
        mp.setVolume(vo, vo);
        vv.start();
        // mp.start();
        Animation anim;
        anim= AnimationUtils.loadAnimation(player.getApplicationContext(),R.anim.fadein_2s);
        // progress.setVisibility(View.VISIBLE);
        progress.startAnimation(anim);

        // Set up a video progress indicator
        final Handler m_handler;
        m_handler = new Handler();
        Runnable onEverySecond=null;
        onEverySecond = new Runnable() {
            @Override
            public void run() {
                Long tv=new Long(vv.getCurrentPosition());
                if (vv.isPlaying()) {
                    progress.setText(getTimeString(tv));
                    m_handler.postDelayed(this, 1000);
                }
            }
        };
        m_handler.postDelayed(onEverySecond, 1000);
    }
*/
    /**
     *
     * Place a ViewStub layouted View on top of the parent View
     * Optionally animate a fade in/out transition
     *
     * @param id            ViewStub resource id
     * @param x             Horizontal start in percentage of parent width
     * @param y             Vertical start in percentage of parent height
     * @param startMs       Delay in ms until start from calling this method 0=immediately
     * @param durationMs    Duration in ms until taken down 0=permanent
     * @param animate       true=fade in/out false=no animation
     * @return              0=ok
     */
    private void overlay(final int id, final int x, final int y,final int startMs, final long durationMs, final boolean animate) {
        final Animation.AnimationListener animationInListener
                = new Animation.AnimationListener(){

            /**
             * <p>Notifies the start of the animation.</p>
             *
             * @param animation The started animation.
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                Log.d(TAG,"AnimIn");
                vv.pause();
            }

            /**
             * <p>Notifies the repetition of the animation.</p>
             *
             * @param animation The animation which was repeated.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        final Animation.AnimationListener animationOutListener
                = new Animation.AnimationListener(){

            /**
             * <p>Notifies the start of the animation.</p>
             *
             * @param animation The started animation.
             */
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
              Log.d(TAG,"AnimOut");
                vv.start();
            }

            /**
             * <p>Notifies the repetition of the animation.</p>
             *
             * @param animation The animation which was repeated.
             */
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        final Runnable overlayInsertion = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"Overlay insertion begins");
                // On demand (lazy) inflating of a layout
                ViewStub stub = (ViewStub) player.findViewById(id);
                final View inflated = stub.inflate();
                if (animate) {
                    // Run animation to fade in overlay
                    Animation anim;
                    anim= AnimationUtils.loadAnimation(player.getApplicationContext(),R.anim.fadein_2s);
                    // progress.setVisibility(View.VISIBLE);
                    anim.setAnimationListener(animationInListener);
                    inflated.startAnimation(anim);
                    inflated.requestFocus();
                }
                else
                    inflated.setVisibility(View.VISIBLE);

                if (durationMs>0) {
                    // Schedule overlay termination
                    final Runnable overlayTermination = new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Overlay termination begins");
                            if (animate) {
                                // Run animation to fade in overlay
                                Animation anim;
                                anim = AnimationUtils.loadAnimation(player.getApplicationContext(), R.anim.fadeout);
                                // progress.setVisibility(View.VISIBLE);
                                anim.setAnimationListener(animationOutListener);
                                inflated.startAnimation(anim);
                            } else
                                inflated.setVisibility(View.INVISIBLE);
                        }
                    };
                    overlayTerminateHandler.postDelayed(overlayTermination, durationMs); // Launch with delay
                    Log.d(TAG, "Overlay termination scheduled.");
                }
                else
                  Log.d(TAG,"Overlay is permanent");
            }
        };
        overlayInsertionHandler.postDelayed(overlayInsertion, startMs); // Launch with delay
        Log.d(TAG, "Overlay scheduled.");
    }

    // Return a logarithmic MediaPlayer audio volume from a linear audio volume 0-100
    private float vol(int value) {
        return 1-(float)(Math.log(100-value)/Math.log(100));
    }

    private static String getTimeString(Long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

}