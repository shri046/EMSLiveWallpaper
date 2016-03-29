package com.androidrec.wallpaper.live.preferences;

import com.androidrec.wallpaper.live.R;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ARColorPickerPreference extends PreferenceActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        ((ColorPickerPreference) findPreference("ns_wave_color")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
                return true;
            }

        });
        ((ColorPickerPreference) findPreference("ns_wave_color")).setAlphaSliderEnabled(true);
    }
}