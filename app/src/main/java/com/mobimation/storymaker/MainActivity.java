package com.mobimation.storymaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Main activity of the StoryMaker app.
 */
public class MainActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private static String TAG = MainActivity.class.getName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    TextView pageIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    @Override
    public void onPageSelected(int i) {
        Log.d(TAG, "onPageSelected() - page=" + i);
        switch (i) {
            case 0:
                pageIndicator.setText("Abc");
                break;
            case 1:
                pageIndicator.setText("aBc");
                break;
            case 2:
                pageIndicator.setText("abC");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
            switch (position) {
                case 0:
                    return PlayLaunchFragment.newInstance(position+1);
                case 1:
                    // return PlaceholderFragment.newInstance(position + 1);
                    return PlaceholderFragment.newInstance(position+1);
                case 2:
                    return PlaceholderFragment.newInstance(position+1);
            }
            return PlaceholderFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("SectionsPagerAdapter", "getPageTitle() - position=" + position);
            switch (position) {
                case 0:
                    pageIndicator.setText("Abc");
                    return "Abc";
                case 1:
                    pageIndicator.setText("aBc");
                    return "aBc";
                case 2:
                    pageIndicator.setText("abC");
                    return "abC";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
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
            final View rootView = inflater.inflate(R.layout.fragment_play, container, false);
            final Button b= (Button)rootView.findViewById(R.id.buttonVideoPlay);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), Player.class));
                }
            });
            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlayerFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        VideoView vv;
        View rootView;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlayerFragment newInstance(int sectionNumber) {
            Log.d("PlayerFragment","sectionNumber="+sectionNumber);
            PlayerFragment fragment = new PlayerFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlayerFragment() {
            Log.d("PlayerFragment", "constructor");
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser)
        {
            super.setUserVisibleHint(isVisibleToUser);
            if (this.isVisible())
            {
                if (!isVisibleToUser)   // If we are becoming invisible, then...
                {
                   Log.d("PlayerFragment", "pause");
                   vv.pause(); //pause or stop video
                }

                if (isVisibleToUser) // If we are becoming visible, then...
                {
                   Log.d("PlayerFragment", "resume");
                   vv.resume(); //play your video
                }
            }
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("PlayerFragment","onCreateView()");
            rootView = inflater.inflate(R.layout.activity_player, container, false);

            vv= (VideoView) rootView.findViewById(R.id.video);
            vv.requestFocus();
            vv.setVideoURI(Uri.parse("http://www.lilldata.se/suzuki/GT750M-1.flv"));
            vv.start();

            return rootView;
        }
    }


    private Typeface getFont(String fontName) {
        return Typeface.createFromAsset(getAssets(), fontName);
    }

}
