package com.mobimation.storymaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    TextView pageIndicator;
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

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (TextView) findViewById(R.id.pageIndicator);
        pageIndicator.setTypeface(getFont("fonts/Digits.ttf"));

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
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
                pageIndicator.setText("Abcd");
                break;
            case 1:
                pageIndicator.setText("aBcd");
                break;
            case 2:
                pageIndicator.setText("abCd");
                break;
            case 3:
                pageIndicator.setText("abcD");
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
            }
            return PlayLaunchFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            // Total pages.
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("SectionsPagerAdapter", "getPageTitle() - position=" + position);
            switch (position) {
                case 0:
                    pageIndicator.setText("Abcd");
                    return "Abcd";
                case 1:
                    pageIndicator.setText("aBcd");
                    return "aBcd";
                case 2:
                    pageIndicator.setText("abCd");
                    return "abCd";
                case 3:
                    pageIndicator.setText("abcD");
                    return "abcD";
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
     * TODO: This page needs to be a list of text artifacts. On the page needs to
     * TODO: exist a button to launch input of a new text artifact.
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
            rootView.setBackground((BitmapDrawable)getResources().getDrawable(R.drawable.recording_phone));
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
}

