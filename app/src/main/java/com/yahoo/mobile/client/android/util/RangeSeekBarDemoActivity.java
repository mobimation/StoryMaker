package com.yahoo.mobile.client.android.util;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mobimation.storymaker.Player;
import com.mobimation.storymaker.R;

/**
 * Activity that demonstrates the appearance and function of the RangeSeekBar.
 */
public class RangeSeekBarDemoActivity extends ActionBarActivity {
    private static final String TAG = RangeSeekBarDemoActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_seek_bar_demo);
        /**
         * Let´s set up some event handlers for the range seek bars
         */
        final RangeSeekBar bar1= (RangeSeekBar)findViewById(R.id.bar1);
        bar1.setOnRangeSeekBarChangeListener(
            new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    Log.d(TAG, "bar1:minValue=" + minValue.toString() + " maxValue=" + maxValue.toString());
                }
            }
        );
        final RangeSeekBar bar2= (RangeSeekBar)findViewById(R.id.bar2);
        bar2.setOnRangeSeekBarChangeListener(
            new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    Log.d(TAG, "bar2:minValue=" + minValue.toString() + " maxValue=" + maxValue.toString());
                }
            }
        );
        final RangeSeekBar bar3= (RangeSeekBar)findViewById(R.id.bar3);
        bar3.setOnRangeSeekBarChangeListener(
            new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    Log.d(TAG, "bar3:minValue=" + minValue.toString() + " maxValue=" + maxValue.toString());
                }
            }
        );
        final RangeSeekBar bar4= (RangeSeekBar)findViewById(R.id.bar4);
        bar4.setOnRangeSeekBarChangeListener(
            new RangeSeekBar.OnRangeSeekBarChangeListener() {
                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                    Log.d(TAG, "bar4:minValue=" + minValue.toString() + " maxValue=" + maxValue.toString());
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_range_seek_bar_demo, menu);
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
}
