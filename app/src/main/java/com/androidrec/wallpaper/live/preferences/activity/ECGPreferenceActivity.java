package com.androidrec.wallpaper.live.preferences.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.androidrec.wallpaper.live.R;

public class ECGPreferenceActivity extends PreferenceActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getPreferenceManager().setSharedPreferencesName("prefs");
    addPreferencesFromResource(R.layout.prefs);
  }
}