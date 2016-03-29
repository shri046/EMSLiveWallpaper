package com.androidrec.wallpaper.live.preferences.category;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidrec.wallpaper.live.R;

public class ECGPreferenceCategory extends PreferenceCategory {
    public ECGPreferenceCategory(Context paramContext) {
        super(paramContext);
    }

    public ECGPreferenceCategory(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public ECGPreferenceCategory(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public View onCreateView(ViewGroup viewGroup) {
        TextView paramViewGroup = (TextView) super.onCreateView(viewGroup);
        paramViewGroup.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        paramViewGroup.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        return paramViewGroup;
    }
}