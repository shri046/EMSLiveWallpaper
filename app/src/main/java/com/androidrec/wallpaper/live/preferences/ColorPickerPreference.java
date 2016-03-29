package com.androidrec.wallpaper.live.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerPreference extends DialogPreference {
    private int color = 0;
    private OnColorChangeListener colorChangeListener = null;

    public ColorPickerPreference(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    protected void onDialogClosed(boolean paramBoolean) {
        if ((paramBoolean) && (this.color != 0)) {
            SharedPreferences.Editor localEditor = getEditor();
            localEditor.putInt(getKey(), this.color);
            localEditor.commit();
        }
        super.onDialogClosed(paramBoolean);
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder) {
        this.colorChangeListener = new OnColorChangeListener() {
            public void colorChanged(int paramAnonymousInt) {
                ColorPickerPreference.this.color = paramAnonymousInt;
            }
        };
        paramBuilder.setView(new ColorPickerView(getContext(), this.colorChangeListener, getSharedPreferences().getInt(getKey(), -16711936)));
        super.onPrepareDialogBuilder(paramBuilder);
    }

    private class ColorPickerView extends View {
        private static final int CENTER_RADIUS = 32;
        private static final int CENTER_X = 100;
        private static final int CENTER_Y = 100;
        private Paint centerPaint = null;
        private final int[] colors = {-65536, -65281, -16776961, -16711681, -16711936, -256, -65536};
        private Paint paint = null;
        private final RectF rectF;
        private final boolean trackingCenter = false;

        ColorPickerView(Context context, ColorPickerPreference.OnColorChangeListener paramInt, int color) {
            super(context);
            this.paint.setShader(new SweepGradient(0.0F, 0.0F, this.colors, null));
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(32.0F);
            float f = 100.0F - this.paint.getStrokeWidth() * 0.5F;
            this.rectF = new RectF(-f, -f, f, f);
            this.centerPaint = new Paint(1);
            this.centerPaint.setColor(color);
            this.centerPaint.setStrokeWidth(5.0F);
        }

        private int ave(int paramInt1, int paramInt2, float paramFloat) {
            return Math.round((paramInt2 - paramInt1) * paramFloat) + paramInt1;
        }

        private int interpColor(int[] paramArrayOfInt, float paramFloat) {
            if (paramFloat <= 0.0F)
                return paramArrayOfInt[0];
            if (paramFloat >= 1.0F)
                return paramArrayOfInt[(paramArrayOfInt.length - 1)];
            paramFloat *= (paramArrayOfInt.length - 1);
            int j = (int) paramFloat;
            paramFloat -= j;
            int i = paramArrayOfInt[j];
            j = paramArrayOfInt[(j + 1)];
            return Color.argb(ave(Color.alpha(i), Color.alpha(j), paramFloat), ave(Color.red(i), Color.red(j), paramFloat), ave(Color.green(i), Color.green(j), paramFloat), ave(Color.blue(i), Color.blue(j), paramFloat));
        }

        protected void onDraw(Canvas paramCanvas) {
            paramCanvas.translate(getRootView().getWidth() / 2 - (int) (this.paint.getStrokeWidth() / 2.0F), 100.0F);
            paramCanvas.drawOval(this.rectF, this.paint);
            paramCanvas.drawCircle(0.0F, 0.0F, 32.0F, this.centerPaint);
        }

        protected void onMeasure(int paramInt1, int paramInt2) {
            paramInt2 = getRootView().getWidth();
            paramInt1 = paramInt2;
            if (paramInt2 == 0)
                paramInt1 = 250;
            setMeasuredDimension(paramInt1, 200);
        }

        public boolean onTouchEvent(MotionEvent paramMotionEvent) {
            float f1 = paramMotionEvent.getX();
            float f2 = getRootView().getWidth() / 2;
            float f3 = paramMotionEvent.getY();
            switch (paramMotionEvent.getAction()) {
                default:
                case 0:
                case 2:
                case 1:
                f2 = (float) ((float) Math.atan2(f3 - 100.0F, f1 - f2) / 6.283185307179586D);
                f1 = f2;
                if (f2 < 0.0F)
                    f1 = f2 + 1.0F;
                this.centerPaint.setColor(interpColor(this.colors, f1));
                invalidate();
                ColorPickerPreference.this.color = this.centerPaint.getColor();
            }
            return true;
        }
    }

    public interface OnColorChangeListener {
        void colorChanged(int paramInt);
    }
}