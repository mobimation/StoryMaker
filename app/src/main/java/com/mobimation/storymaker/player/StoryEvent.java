package com.mobimation.storymaker.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobimation.storymaker.story.EventType;
import com.mobimation.storymaker.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by gunnarforsgren on 15-03-19.
 * <p/>
 * The StoryEvent holds information about a piece of media to be
 * played back at a certain point in time.
 * This object is used by an event scheduler that schedules both
 * a launch and termination handler.
 */
public class StoryEvent
        /*
        implements  MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener   */ {
    int volume = 50;
    Player player = null;
    StoryEvent event = null;
    Uri uri = null;
    Runnable insertion = null;
    Runnable termination = null;
    TextureView vv = null;
    CountDownLatch sync = null;
    TextView progress = null;
    MediaPlayer mp = null;
    long start;
    long duration;
    // LABEL, TEXT, AUDIO, IMAGE, VIDEO, PROMPT
    SceneHandler audioInsertionHandler = new SceneHandler();
    SceneHandler audioTerminateHandler = new SceneHandler();
    SceneHandler videoInsertionHandler = new SceneHandler();
    SceneHandler videoTerminateHandler = new SceneHandler();
    SceneHandler labelInsertionHandler = new SceneHandler();
    SceneHandler labelTerminateHandler = new SceneHandler();
    SceneHandler textInsertionHandler = new SceneHandler();
    SceneHandler textTerminateHandler = new SceneHandler();
    SceneHandler promptInsertionHandler = new SceneHandler();
    SceneHandler promptTerminateHandler = new SceneHandler();
    SceneHandler imageInsertionHandler = new SceneHandler();
    SceneHandler imageTerminateHandler = new SceneHandler();
    SceneHandler overlayInsertionHandler = new SceneHandler();
    SceneHandler overlayTerminateHandler = new SceneHandler();
    final EventType type;
    int resource = 0;
    boolean noinit=true;
    private static final String TAG = StoryEvent.class.getSimpleName();

    public StoryEvent(Player player, CountDownLatch sync, EventType type, Uri uri, long start, long duration) {
        this.player = player;
        this.type = type;
        this.start = start;
        this.duration = duration;
        this.event = this;
        this.sync=sync;
        this.uri = uri;
        vv = (TextureView) player.findViewById(R.id.video);
        progress = (TextView) player.findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
    }

    // Constructor for overlay
    public StoryEvent(Player player, CountDownLatch sync, EventType type, int resource, long start, long duration) {
        this.player = player;
        this.type = type;
        this.resource = resource;
        this.start = start;
        this.sync=sync;
        this.duration = duration;
        vv = (TextureView) player.findViewById(R.id.video);
    }

    public void pause() {
        switch (type) {
            case VIDEO:
                videoInsertionHandler.pause();
                videoTerminateHandler.pause();
                Log.d(TAG, "Video handlers paused");
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
                Log.d(TAG, "Video handlers resumed");
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
     * schedule() will schedule a thread to run in the future,
     * with a purpose to launch and optionally take down a piece of media playback
     * part of a multithreaded scene.
     */
    public void schedule() {
        switch (type) {
            case VIDEO:
                Runnable videoInsertion = new Runnable() {
                    @Override
                    public void run() {  // Thread that launches video playback
                    Log.d(TAG, "Video insertion begins at " + start);
                     //                       final VideoView vv2=vv;
                    Log.d(TAG, "Using URL " + uri.toString());
                     // TODO: Modify for TextureView + MediaPlayer

                    vv.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                        @Override
                        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                            Log.d(TAG, "onSurfaceTextureAvailable, width=" + width + " height=" + height);
                             // availableInit();
                        }

                        @Override
                        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                            Log.d(TAG, "onSurfaceTextureSizeChanged, width="+width+" height="+height);
                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                            Log.d(TAG, "onSurfaceTextureDestroyed");
                            mp.stop();
                            Log.d(TAG, "Media Player stopped.");
                            Handler playerReleaseHandler = new Handler();
                            // Release player with one second delay, avoids app crash TODO: Find out why :)
                            final Runnable playerRelease = new Runnable() {
                                @Override
                                    public void run() {
                                        mp.release();
                                        Log.d(TAG, "Media Player released.");
                                    }
                            };
                            playerReleaseHandler.postDelayed(playerRelease, 1000L); // Release with delay
                            return false;
                        }

                        @Override
                        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                           // Log.d(TAG, "onSurfaceTextureUpdated");
                        }
                    });

                    if (vv.isAvailable())
                        if (noinit)
                            availableInit();
                        else
                            Log.d(TAG,"Not available");

                    // vv.requestFocus();
                    // vv2.start();  // Started by onPrepareHandler

                    if (duration > 0) {
                        // Schedule termination
                        Runnable videoTermination = new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Video stops at " + start + duration + " milliseconds.");
                                mp.stop();
                            }
                        };
                        videoTerminateHandler.postDelayed(videoTermination, duration); // Launch with delay >=0
                        Log.d(TAG, "Video termination to occur after " + duration + " millisecond delay.");
                        termination = videoTermination;
                    }
                    else
                        Log.d(TAG, "No video termination");
                    }
                };
                videoInsertionHandler.postDelayed(videoInsertion, start); // Launch with delay >=0
                insertion = videoInsertion;
                Log.d(TAG, "Video scheduled.");
                break;

            case AUDIO:
                final Runnable audioInsertion = new Runnable() {
                    @Override
                    public void run() {
                    videoWait(type);
                    Log.d(TAG, "Audio insertion begins");
                        // TODO: Add playback launch
                        if (duration > 0) {
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
                        } else
                            Log.d(TAG, "No audio termination");
                    }
                };
                audioInsertionHandler.postDelayed(audioInsertion, start); // Launch with delay
                Log.d(TAG, "Audio scheduled.");
                insertion = audioInsertion;
                break;
            case LABEL:
                final Runnable labelInsertion = new Runnable() {
                    @Override
                    public void run() {
                        videoWait(type);
                        Log.d(TAG, "Label insertion begins");
                        // TODO: Add playback launch
                        if (duration > 0) {
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
                        } else
                            Log.d(TAG, "No label termination");
                    }
                };
                labelInsertionHandler.postDelayed(labelInsertion, start); // Launch with delay
                Log.d(TAG, "Label scheduled.");
                insertion = labelInsertion;
                break;
            case TEXT:
                final Runnable textInsertion = new Runnable() {
                    @Override
                    public void run() {
                        videoWait(type);
                        Log.d(TAG, "Text insertion begins");
                        // TODO: Add playback launch
                        if (duration > 0) {
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
                        } else
                            Log.d(TAG, "No text termination");
                    }
                };
                textInsertionHandler.postDelayed(textInsertion, start); // Launch with delay
                Log.d(TAG, "Text scheduled.");
                insertion = textInsertion;
                break;
            case IMAGE:
                final Runnable imageInsertion = new Runnable() {
                    @Override
                    public void run() {
                        videoWait(type);
                        Log.d(TAG, "Image insertion begins");
                        // TODO: Add playback launch
                        if (duration > 0) {
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
                        } else
                            Log.d(TAG, "No image termination");
                    }
                };
                imageInsertionHandler.postDelayed(imageInsertion, start); // Launch with delay
                Log.d(TAG, "Image scheduled.");
                insertion = imageInsertion;
                break;
            case PROMPT:
                overlay(R.id.player_overlay, 0, 0, start, duration, true);
                // TODO: Handle prompt response
                break;
            default:
                Log.e(TAG, "Unsupported event type");
                break;
        }
    }

    /**
     * Initiate video playback using a SurfaceView and MediaPlayer
     */
    private void availableInit() {
        SurfaceTexture st = vv.getSurfaceTexture();
        Surface s = new Surface(st);
        try {
            mp = new MediaPlayer();
            mp.setDataSource(uri.toString());
            mp.setSurface(s);
            mp.prepareAsync();
            mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    Log.d(TAG, "onBufferingUpdate(), " + percent + " %");
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG, "onCompletion()");
                }
            });
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    Log.d(TAG, "onPrepared()");
                    Float vo = vol(volume);
                    mp.setVolume(vo, vo);
                    // progressDialog.dismiss();
                    if (sync != null)
                        if (sync.getCount() > 0L)
                            sync.countDown();  // Let loose overlay thread scheduling
                    mp.start();
                    // Set up a video progress indicator
                    Animation anim;
                    anim = AnimationUtils.loadAnimation(player.getApplicationContext(), R.anim.fadein_2s);
                    // progress.setVisibility(View.VISIBLE);
                    progress.startAnimation(anim);

                    final Handler m_handler;
                    m_handler = new Handler();
                    Runnable onEverySecond = null;
                    onEverySecond = new Runnable() {
                        @Override
                        public void run() {
                            int tv = mp.getCurrentPosition();
                            if (mp.isPlaying()) {
                                progress.setText(getTimeString(new Long(tv).longValue()));
                                m_handler.postDelayed(this, 1000);
                            }
                        }
                    };
                    m_handler.postDelayed(onEverySecond, 1000);
                }
            });
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.d(TAG, "onVideoSizeChanged(), width=" + width + ", height=" + height);
                }
            });
            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d(TAG, "onError(), what=" + what + ", extra=" + extra);
                    return false;
                }
            });
            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Log.d(TAG, "onInfo(), what=" + what + ", extra=" + extra);
                    return false;
                }
            });
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (IllegalStateException ise) {
            ise.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        noinit=false;
    }
    /**
     * Place a ViewStub layouted View on top of the parent View
     * Optionally animate a fade in/out transition
     *
     * @param id         ViewStub resource id
     * @param x          Horizontal start in percentage of parent width
     * @param y          Vertical start in percentage of parent height
     * @param startMs    Delay in ms until start from calling this method 0=immediately
     * @param durationMs Duration in ms until taken down 0=permanent
     * @param animate    true=fade in/out false=no animation
     * @return 0=ok
     */
    private void overlay(final int id, final int x, final int y, final long startMs, final long durationMs, final boolean animate) {

        final Runnable overlayInsertion = new Runnable() {
            @Override
            public void run() {
                videoWait(EventType.PROMPT);
                Log.d(TAG, "Overlay insertion begins");
                // On demand (lazy) inflating of a layout
                ViewStub stub = (ViewStub) player.findViewById(id);
                final View inflated = stub.inflate();
                if (animate) {
                    fadeIn(inflated);
                } else
                    inflated.setVisibility(View.VISIBLE);

                if (durationMs > 0) {
                    // Schedule overlay termination
                    final Runnable overlayTermination = new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Overlay termination begins");
                            if (animate) {
                                fadeOut(inflated);
    /*                          // Run animation to fade out overlay
                                Animation anim;
                                anim = AnimationUtils.loadAnimation(player.getApplicationContext(), R.anim.fadeout);
                                // progress.setVisibility(View.VISIBLE);
                                anim.setAnimationListener(animationOutListener);
                                inflated.startAnimation(anim); */
                            } else
                                inflated.setVisibility(View.INVISIBLE);
                        }
                    };
                    overlayTerminateHandler.postDelayed(overlayTermination, durationMs); // Launch with delay
                    Log.d(TAG, "Overlay termination scheduled.");
                } else
                    Log.d(TAG, "Overlay is permanent");
            }
        };
        overlayInsertionHandler.postDelayed(overlayInsertion, startMs); // Launch with delay
        Log.d(TAG, "Overlay scheduled.");
    }

    /**
     * Code exmple that is supposed to capture a View into a Bitmap
     * @param context
     * @param v Root view of content to capture
     */
    private static void loadBitmapFromView(Context context, android.view.View v) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(returnedBitmap);
//        v.draw(c);

        takeScreen(returnedBitmap);
    }

    /**
     * Code that saves a Bitmap to a file
     * @param bitmap
     */
    private static void takeScreen(Bitmap bitmap) {
        //Bitmap bitmap = ImageUtils.loadBitmapFromView(this, view); //get Bitmap from the view
        String mPath = Environment.getExternalStorageDirectory() + File.separator + "screen_" + System.currentTimeMillis() + ".jpeg";
        File imageFile = new File(mPath);
        OutputStream fout=null;
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
          try {
              fout.close();
          }
          catch (IOException ioe) {
              ioe.printStackTrace();
          }
        }
    }

    /**
     * Fade in an inflated view part of the FrameLayout1 hierarchy.
     * @param inflated
     */
    private void fadeIn (final View inflated) {
        // Run animation to fade in overlay
        Animation anim;
        anim = AnimationUtils.loadAnimation(player.getApplicationContext(), R.anim.fadein);
        // progress.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mp.pause();  // Pause Movie
                inflated.bringToFront();

                // Capture screen
                final RelativeLayout rl=(RelativeLayout)player.findViewById(R.id.FrameLayout1);
                rl.post(new Runnable() {
                    public void run() {
                        //take screenshot
                        Bitmap myBitmap = captureScreen(rl);
                        try {
                            if (myBitmap != null) {
                                //save image to SD card
                                saveImage(myBitmap);
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });

                // Set up button handling so user can reply to question
                Button b1=(Button) inflated.findViewById(R.id.b1);
                Button bx=(Button) inflated.findViewById(R.id.bx);
                Button b2=(Button) inflated.findViewById(R.id.b2);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"1 pressed");
                        fadeOut(inflated);

                    }
                });
                bx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"X pressed");
                        fadeOut(inflated);
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"2 pressed");
                        fadeOut(inflated);
                    }
                });
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        inflated.startAnimation(anim);
        inflated.requestFocus();
    }

    /**
     * Fade out and free resources of the inflated view represented by a ViewStub
     * part of the FrameLayout1 hierarchy.
     * @param inflated
     */
    private void fadeOut(final View inflated) {
        // Run animation to fade out overlay
        Animation anim;
        anim = AnimationUtils.loadAnimation(player.getApplicationContext(), R.anim.fadeout);
        // progress.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            // Remove no longer used inflated overlay view
            RelativeLayout rl = (RelativeLayout)player.findViewById(R.id.FrameLayout1);
            rl.removeView(inflated);
            mp.start();  // Continue paused video
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        inflated.startAnimation(anim);
    }

    /**
     * Save Bitmap as JPG to file
     * @param bitmap
     * @throws IOException
     */
    private static void saveImage(Bitmap bitmap) throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "snapshot.png");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
    }

    /**
     * Capture Bitmap from a view
     * @param v Root view of content to capture
     * @return  Bitmap containing what was captured
     */
    private static Bitmap captureScreen(View v) {
        Bitmap screenshot = null;
        try {
            if(v!=null) {
                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }
        }catch (Exception e){
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }
        return screenshot;
    }

    /**
     * Return a logarithmic MediaPlayer audio volume from a linear audio volume 0-100
     * @param value
     * @return float value
     */
    private float vol(int value) {
        return 1 - (float) (Math.log(100 - value) / Math.log(100));
    }

    /**
     * Delays scheduling of video overlay effects
     * until video actually begins playing.
     *
     * @param type Show type in tracing
     */
    private void videoWait(EventType type) {
      if (sync!=null) {
              try {
                  Log.d(TAG, "Media type " + type.toString() + " sleep until video ready..");
                  sync.await();
                  Log.d(TAG, "Media type " + type.toString() + " OK to schedule.");
              } catch (InterruptedException ie) {
                  Log.d(TAG,"videoWait() - exception="+ie.getMessage());
              }
      }
      else
          Log.e(TAG,"videoWait() - sync is NULL");
    }

    /**
     * Return a formatted time representation of a millisecond value
     * @param millis
     * @return String
     */
    private static String getTimeString(Long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

}