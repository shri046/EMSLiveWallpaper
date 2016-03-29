package com.androidrec.wallpaper.live.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidrec.wallpaper.live.R;

/**
 * Created by Stelian Morariu on 12/9/13.
 */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
    private static final String APPLICATIONNS = "http://stemo.ro";
    private static final int DEFAULT_VALUE = 255;
    private static final String LOG_TAG = SeekBarPreference.class.getSimpleName();
    private int mMaxValue = 255;
    private int mMinValue = 0;
    private int mInterval = 1;
    private int mCurrentValue;
    private SeekBar mSeekBar;
    private TextView mStatusText;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreference(context, attrs);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreference(context, attrs);
    }

    private void initPreference(Context context, AttributeSet attrs) {
        setValuesFromXml(attrs);
        mSeekBar = new SeekBar(context, attrs);
        mSeekBar.setMax(mMaxValue);
        mSeekBar.setOnSeekBarChangeListener(this);

        setWidgetLayoutResource(R.layout.preference_seek_bar);
    }

    private void setValuesFromXml(AttributeSet attrs) {
        mMaxValue = attrs.getAttributeIntValue(ANDROIDNS, "max", DEFAULT_VALUE);
        mMinValue = attrs.getAttributeIntValue(APPLICATIONNS, "min", 0);


        String units = getAttributeStringValue(attrs, APPLICATIONNS, "units", "");


        try {
            String newInterval = attrs.getAttributeValue(APPLICATIONNS, "interval");
            if (newInterval != null)
                mInterval = Integer.parseInt(newInterval);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Invalid interval value", e);
        }

    }

    private String getAttributeStringValue(AttributeSet attrs, String namespace, String name, String defaultValue) {
        String value = attrs.getAttributeValue(namespace, name);
        if (value == null)
            value = defaultValue;

        return value;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);

        // The basic preference layout puts the widget frame to the right of the title and summary,
        // so we need to change it a bit - the seekbar should be under them.
        LinearLayout layout = (LinearLayout) view;
        layout.setOrientation(LinearLayout.VERTICAL);

        return view;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);
        ViewParent oldContainer = mSeekBar.getParent();

        try {
            // move our seekbar to the new view we've been given

            if (oldContainer != newContainer) {
                // remove the seekbar from the old view
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(mSeekBar);
                }
                // remove the existing seekbar (there may not be one) and add ours
                newContainer.removeAllViews();
                newContainer.addView(mSeekBar, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error binding view: " + ex.toString());
        }

        //if dependency is false from the beginning, disable the seek bar
        if (view != null && !view.isEnabled()) {
            mSeekBar.setEnabled(false);
        }

        updateView(view);
    }

    /**
     * Update a SeekBarPreference view with our current state
     *
     * @param view
     */
    protected void updateView(View view) {

        try {
//            mStatusText = (TextView) view.findViewById(R.id.seekBarPrefValue);
//
//            mStatusText.setText(String.valueOf(mCurrentValue));
//            mStatusText.setMinimumWidth(30);

            mSeekBar.setProgress(mCurrentValue);

            TextView seekMin = (TextView) view.findViewById(R.id.seekbarMinLabel);
            seekMin.setText(String.valueOf(mMinValue));

            TextView seekMax = (TextView) view.findViewById(R.id.seekbarMaxLabel);
            seekMax.setText(String.valueOf(mMaxValue));

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error updating seek bar preference", e);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = progress + mMinValue;

        if (newValue > mMaxValue)
            newValue = mMaxValue;
        else if (newValue < mMinValue)
            newValue = mMinValue;
        else if (mInterval != 1 && newValue % mInterval != 0)
            newValue = Math.round(((float) newValue) / mInterval) * mInterval;

        // change rejected, revert to the previous value
        if (!callChangeListener(newValue)) {
            seekBar.setProgress(mCurrentValue - mMinValue);
            return;
        }

        // change accepted, store it
        mCurrentValue = newValue;
//        mStatusText.setText(String.valueOf(newValue));
        persistInt(newValue);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {

        int defaultValue = ta.getInt(index, DEFAULT_VALUE);
        return defaultValue;

    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        if (restoreValue) {
            mCurrentValue = getPersistedInt(mCurrentValue);
        } else {
            int temp = 0;
            try {
                temp = (Integer) defaultValue;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Invalid default value: " + defaultValue.toString());
            }

            persistInt(temp);
            mCurrentValue = temp;
        }

    }

    /**
     * make sure that the seekbar is disabled if the preference is disabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mSeekBar.setEnabled(enabled);
    }

    @Override
    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        super.onDependencyChanged(dependency, disableDependent);

        //Disable movement of seek bar when dependency is false
        if (mSeekBar != null) {
            mSeekBar.setEnabled(!disableDependent);
        }
    }
}