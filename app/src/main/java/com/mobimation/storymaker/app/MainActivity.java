package com.mobimation.storymaker.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mobimation.storymaker.R;
import com.mobimation.storymaker.editor.StoryEditorActivity;
import com.mobimation.storymaker.player.Player;
import com.mobimation.storymaker.recorder.PhotoRecorder;
import com.mobimation.storymaker.recorder.TextRecorder;
import com.mobimation.storymaker.transfer.DeliveryService;
import com.yahoo.mobile.client.android.util.RangeSeekBarDemoActivity;

/**
 * Main navigation activity of the StoryMaker app.
 * A ViewPager is used for accomplishing a sideways swipe action
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    DeliveryService deliveryService;
    private static final String TAG = MainActivity.class.getSimpleName();
    Button buttonStartService;
    private Intent svc;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    TextView pageIndicator;

    private boolean mIsBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service. Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            deliveryService = ((DeliveryService.LocalBinder)service).getService();
            mIsBound = true;
            Log.d(TAG,"onServiceConnected()");
            // TODO: setupGui();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            deliveryService = null;

            mIsBound = false;
            Log.d(TAG,"onServiceDisconnected()");
            // Toast.makeText(MainActivity.this,
            // R.string.local_service_disconnected, Toast.LENGTH_SHORT).show();
            // TODO:  setupGui();
            // wait for a new connection
            doBindService();
        }
    };

    void doBindService() {
        // Establish a connection with the service. We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        Log.d(TAG,"waiting for new service connection..");
        bindService(new Intent(MainActivity.this, DeliveryService.class),
                mConnection, 0);
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            Log.d(TAG,"Detached service connection.");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_layout);
        // b.setOnClickListener();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        svc = new Intent(MainActivity.this, DeliveryService.class);

        buttonStartService= (Button) findViewById(R.id.buttonStartService);
        buttonStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch file transfer service for the transfer of a file
                // TODO putExtra typically used to specify what
                // TODO file to transfer.
                // svc.putExtra("file", file);
                // svc.putExtra("id",id);  // And so on....
                MainActivity.this.startService(svc);
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (TextView) findViewById(R.id.pageIndicator);
        pageIndicator.setTypeface(getFont("fonts/Digits.ttf"));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        Log.d(TAG,"onPageScrolled(),i="+i+" v="+v+" i2="+i2);
    }

    @Override
    public void onPageSelected(int i) {
        Log.d(TAG, "onPageSelected() - page=" + i);
        switch (i) {
            case 0:
                pageIndicator.setText("Abcde");
                break;
            case 1:
                pageIndicator.setText("aBcde");
                break;
            case 2:
                pageIndicator.setText("abCde");
                break;
            case 3:
                pageIndicator.setText("abcDe");
                break;
            case 4:
                pageIndicator.setText("abcdE");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d(TAG,"onPageScrollStateChanged(), i="+i);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("SectionsPagerAdapter", "getItem() - position=" + position+1);

            // The sliding pages and their positions:
            switch (position) {
                case 0:
                    return PlayLaunchFragment.newInstance(position+1);
                case 1:
                    return TextRecorderLaunchFragment.newInstance(position+1);
                case 2:
                    return PhotoRecorderLaunchFragment.newInstance(position+1);
                case 3:
                    return StoriesFragment.newInstance(position+1);
                case 4:
                    return RangeSeekBarDemoLaunchFragment.newInstance(position+1);
            }
            return PlayLaunchFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            // Total pages.
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("SectionsPagerAdapter", "getPageTitle() - position=" + position);
            switch (position) {
                case 0:
                    pageIndicator.setText("Abcde");
                    return "Abcde";
                case 1:
                    pageIndicator.setText("aBcde");
                    return "aBcde";
                case 2:
                    pageIndicator.setText("abCde");
                    return "abCde";
                case 3:
                    pageIndicator.setText("abcDe");
                    return "abcDe";
                case 4:
                    pageIndicator.setText("abcdE");
                    return "abcdE";
            }
            return null;
        }
    }

    /*********************************************************************************************
     * Player launch page
     * TODO: Player is for test right now.
     * TODO: Needs to be part of the Stories list for editing a new or existing story
     */
    public static class PlayLaunchFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlayLaunchFragment newInstance(int sectionNumber) {
            PlayLaunchFragment fragment = new PlayLaunchFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlayLaunchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_player_launcher, container, false);

            final Button b= (Button)rootView.findViewById(R.id.buttonPlayerLaunch);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Player.class));
                }
            });
            return rootView;
        }
    }

    /*********************************************************************************************
     * Text Recorder launch page
     * TODO: This page needs to be a list of text artifacts.
     * TODO: A button is needed on the page for launching the input of a new text artifact.
     */
    public static class TextRecorderLaunchFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TextRecorderLaunchFragment newInstance(int sectionNumber) {
            TextRecorderLaunchFragment fragment = new TextRecorderLaunchFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public TextRecorderLaunchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_textrecorder_launcher, container, false);
            final Button b= (Button)rootView.findViewById(R.id.buttonTextRecorderLaunch);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), TextRecorder.class));
                }
            });
            return rootView;
        }
    }

    private Typeface getFont(String fontName) {
        return Typeface.createFromAsset(getAssets(), fontName);
    }

    /*********************************************************************************************
     * Photo Recorder launch page.
     * TODO: This page should list all photos, own and others. There should be a
     * TODO: button on that page to launch taking a new photo.
     */
    public static class PhotoRecorderLaunchFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PhotoRecorderLaunchFragment newInstance(int sectionNumber) {
            PhotoRecorderLaunchFragment fragment = new PhotoRecorderLaunchFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PhotoRecorderLaunchFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_photos_launcher, container, false);
            rootView.setBackground(getResources().getDrawable(R.drawable.recording_phone));
            final Button b= (Button)rootView.findViewById(R.id.buttonPhotoRecorderLaunch);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), PhotoRecorder.class));
                }
            });
            return rootView;
        }
    }



    /*********************************************************************************************
     * Timeline launch page.
     */
    public static class StoriesFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
        View rootView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
        * Returns a new instance of this fragment for the given section
        * number.
         */
        public static StoriesFragment newInstance(int sectionNumber) {
         Log.d("StoriesFragment","sectionNumber="+sectionNumber);
         StoriesFragment fragment = new StoriesFragment();
         Bundle args = new Bundle();
         args.putInt(ARG_SECTION_NUMBER, sectionNumber);
         fragment.setArguments(args);
         return fragment;
        }

        public StoriesFragment() {
            Log.d("StoriesFragment", "constructor");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Log.d("StoriesFragment","onCreateView()");
            rootView = inflater.inflate(R.layout.fragment_stories_launcher, container, false);
            final Button b= (Button)rootView.findViewById(R.id.buttonStoriesLaunch);
            // TODO: Story page should show list of stories, own and received.
            // TODO: Starting the editor for a new story should be a button on that page.
            b.setOnClickListener(new View.OnClickListener() {
               @Override
              public void onClick(View view) {
                  startActivity(new Intent(getActivity(), StoryEditorActivity.class));
              }
            });
            return rootView;
        }
    }

    /*********************************************************************************************
     * Demo page for RangeSeekBar UI widget
     */
    public static class RangeSeekBarDemoLaunchFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        View rootView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RangeSeekBarDemoLaunchFragment newInstance(int sectionNumber) {
            Log.d("RangeSeekBarFragment","sectionNumber="+sectionNumber);
            RangeSeekBarDemoLaunchFragment fragment = new RangeSeekBarDemoLaunchFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public RangeSeekBarDemoLaunchFragment() {
            Log.d("RangeSeekBarFragment", "constructor");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("RangeSeekBarFragment","onCreateView()");
            rootView = inflater.inflate(R.layout.fragment_range_seek_bar_demo_launcher, container, false);
            final Button b= (Button)rootView.findViewById(R.id.buttonRangeSeekBarDemoLaunch);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), RangeSeekBarDemoActivity.class));
                }
            });
            return rootView;
        }
    }
}

