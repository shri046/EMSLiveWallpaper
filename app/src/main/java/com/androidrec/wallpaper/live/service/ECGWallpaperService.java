package com.androidrec.wallpaper.live.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.androidrec.wallpaper.live.R;
import com.androidrec.wallpaper.live.constants.WaveType;
import com.androidrec.wallpaper.live.utils.LineObjects;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ECGWallpaperService extends WallpaperService {
    public static final String PREFS_FILE = "prefs";
    public static final String TAG = "ECGWallpaperService";

    public void onCreate() {
        super.onCreate();
    }

    public WallpaperService.Engine onCreateEngine() {
        try {
            return new ECGEngine();
        } catch (IOException localIOException) {
            Log.w("ECGWallpaperService", "Error creating ECGEngine", localIOException);
            stopSelf();
        }
        return null;
    }

    class ECGEngine extends WallpaperService.Engine
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        private static final int batteryXOffset = 40;
        private final int heartXOffset = 140;
        private static final int heartYOffset = 4;
        private BroadcastReceiver batteryLevelReceiver;
        private WaveType currentWaveType = WaveType.NORMAL_SINUS;
        private final Runnable ecgProcess;
        private Path fPath;
        private LinearGradient gradient;
        private final Paint gridPaint;
        private LineObjects lineObject;
        private Path mBattery;
        private int mBatteryLevel;
        private Paint mBatteryPaint;
        private Paint mBoxPaint;
        private Canvas mCanvas;
        private int mCharging = 0;
        private int mCurrentStep;
        private final Matrix mGradientMatrix;
        private Path mHeart;
        private String mHeartRate = "";
        public SharedPreferences mPrefs;
        private ScheduledExecutorService mScheduledExecutorService;
        private boolean mSurfaceAvailable = false;
        private Paint mTextPaint;
        private long mTime;
        private int mWaveColor = R.color.colorGreen;
        private WaveType mWaveType;
        private final Paint pathPaint;
        private int screenHeight;
        private int screenWidth;
        private SurfaceHolder surfaceHolder;

        public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
            if (paramString != null) {
                this.mPrefs = paramSharedPreferences;
                setupBatteryOptions();
                initLineObject();
            }
        }

        ECGEngine()
                throws IOException {
            super();
            PreferenceManager.setDefaultValues(ECGWallpaperService.this.getApplicationContext(), R.xml.prefs, false);
            this.mPrefs = ECGWallpaperService.this.getSharedPreferences("prefs", 0);
            this.mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(this.mPrefs, null);
            this.mGradientMatrix = new Matrix();
            this.fPath = new Path();
            this.pathPaint = new Paint();
            setupPathPaint();
            this.gridPaint = new Paint();
            setupGridPaint();
            setScreenDimensions();
            registerBatteryMonitor();
            initLineObject();
            this.ecgProcess = new Runnable() {
                public void run() {
                    if (ECGWallpaperService.ECGEngine.this.mSurfaceAvailable)
                        ECGWallpaperService.ECGEngine.this.ecg();
                }
            };
        }

        private int asyBattery() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.asy_battery_level), Integer.parseInt(ECGWallpaperService.this.getString(R.string.asy_battery_default)));
        }

        private int blurPath() {
            return Integer.parseInt(this.mPrefs.getString(ECGWallpaperService.this.getString(R.string.ecg_line_blur), ECGWallpaperService.this.getString(R.string.ecg_line_blur_default)));
        }

        private void createBatteryPath() {
            this.mBattery = new Path();
            this.mBattery.moveTo(40.0F, 100.0F);
            this.mBattery.lineTo(40.0F, 74.0F);
            this.mBattery.lineTo(46.0F, 74.0F);
            this.mBattery.lineTo(46.0F, 72.0F);
            this.mBattery.lineTo(52.0F, 72.0F);
            this.mBattery.lineTo(52.0F, 74.0F);
            this.mBattery.lineTo(58.0F, 74.0F);
            this.mBattery.lineTo(58.0F, 100.0F);
            this.mBattery.lineTo(40.0F, 100.0F);
            this.mBattery.close();
            this.mBatteryPaint = this.mTextPaint;
        }

        private void createBoxPaint() {
            this.mBoxPaint = new Paint();
            this.mBoxPaint.setStyle(Paint.Style.FILL);
            this.mBoxPaint.setAntiAlias(true);
            this.mBoxPaint.setTextSize(40.0F);
            this.mBoxPaint.setPathEffect(new CornerPathEffect(30.0F));
        }

        private void createGradient() {
            this.mBoxPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            this.mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            this.gradient = new LinearGradient(0.0F, this.screenHeight / 2, this.screenWidth / 3, this.screenHeight / 2, 0, this.mWaveColor, Shader.TileMode.CLAMP);
        }

        private void createHeartPath() {
            this.mHeart = new Path();
            this.mHeart.moveTo(this.screenWidth - this.heartXOffset + 24, 78.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 20, 76.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 16, 74.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 14, 76.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 10, 78.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 10, 84.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 24, 102.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 38, 84.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 38, 78.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 34, 76.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 32, 74.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 28, 76.0F);
            this.mHeart.lineTo(this.screenWidth - this.heartXOffset + 24, 78.0F);
            this.mHeart.close();
        }

        private void createTextPaint() {
            this.mTextPaint = new Paint(this.mBoxPaint);
            this.mTextPaint.setPathEffect(null);
        }

        private void draw() {
            this.mCanvas.drawColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            this.lineObject.setWaveType(this.mWaveType);
            if ((this.mCurrentStep < this.lineObject.getNumberOfSamples()) || (this.mCurrentStep == 0)) {
                drawGraph();
                this.mCurrentStep += 1;
                return;
            }
            this.mCurrentStep = 0;
            drawGraph();
        }

        private void drawBattery() {
            this.mBatteryPaint.setShader(new LinearGradient(90.0F, 100.0F, 90.0F, 92 - this.mBatteryLevel * 26 / 100, this.mWaveColor, -16777216, Shader.TileMode.CLAMP));
            Rect localRect = new Rect(41, 99, 57, 75);
            this.mCanvas.drawRect(localRect, this.mBatteryPaint);
            this.mBatteryPaint.setShader(null);
            this.mBatteryPaint.setStyle(Paint.Style.STROKE);
            this.mBatteryPaint.setStrokeWidth(2.0F);
            this.mCanvas.drawPath(this.mBattery, this.mBatteryPaint);
            this.mBatteryPaint.setStyle(Paint.Style.FILL);
            this.mCanvas.drawText(this.mBatteryLevel + "%", 64.0F, 100.0F, this.mTextPaint);
        }

        private void drawGraph() {
            if (enableGrid())
                drawGrid();
            if (enableHeartbeat())
                drawHeart();
            if (enableBattery())
                drawBattery();
            translateGradient();
            this.fPath = this.lineObject.getPath(this.mWaveType, 0, this.mCurrentStep + 1);
            this.pathPaint.setShader(this.gradient);
            this.pathPaint.setStrokeWidth(strokeWidth() * 3);
            this.mCanvas.drawPath(this.fPath, this.pathPaint);
        }

        private void drawGrid() {
            int m = this.screenHeight / 10;
            int n = this.screenHeight / 10;
            int j = 0;
            int i = 0;
            while (true) {
                if (i >= this.screenHeight / 10)
                    return;
                this.mCanvas.drawLine(m * i + m, 0.0F, m * i + m, this.screenHeight, this.gridPaint);
                int k = j;
                if (i > 0) {
                    this.mCanvas.drawLine(0.0F, n * j, this.screenWidth, n * j, this.gridPaint);
                    k = j + 1;
                }
                i += 1;
                j = k;
            }
        }

        private void drawHeart() {
            if ((System.currentTimeMillis() - this.mTime > 1000L) && (System.currentTimeMillis() - this.mTime < 1500L)) {
                this.mCanvas.drawPath(this.mHeart, this.mBoxPaint);
                this.mCanvas.drawText(this.mHeartRate, this.screenWidth - 100, 100.0F, this.mTextPaint);
                if (System.currentTimeMillis() - this.mTime > 1500L)
                    this.mTime = System.currentTimeMillis();
            }
        }

        private void ecg() {
            this.surfaceHolder = getSurfaceHolder();
            this.mCanvas = null;
            try {
                this.mCanvas = this.surfaceHolder.lockCanvas();
                if (this.mCanvas != null)
                    draw();
                if (isVisible())
                    this.mScheduledExecutorService.schedule(this.ecgProcess, 1000L / (15L * wSpeed()), TimeUnit.MILLISECONDS);
            } finally {
                if (this.mCanvas != null)
                    this.surfaceHolder.unlockCanvasAndPost(this.mCanvas);
            }
        }

        private boolean enableBattery() {
            return this.mPrefs.getBoolean(ECGWallpaperService.this.getString(R.string.ecg_enable_battery_level), false);
        }

        private boolean enableGrid() {
            return this.mPrefs.getBoolean(ECGWallpaperService.this.getString(R.string.ecg_enable_grid), true);
        }

        private boolean enableHeartbeat() {
            return this.mPrefs.getBoolean(ECGWallpaperService.this.getString(R.string.ecg_display_heartbeat), true);
        }

        private int getASYColor() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.asy_wave_color), R.color.colorPrimary);
        }

        private int getNSColor() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.ns_wave_color), R.color.colorPrimary);
        }

        private int getSVTColor() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.svt_wave_color), R.color.colorPrimary);
        }

        private int getVFIBColor() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.vfib_wave_color), R.color.colorPrimary);
        }

        private int getVTColor() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.vt_wave_color), R.color.colorPrimary);
        }

        private void initLineObject() {
            setScreenDimensions();
            this.lineObject = new LineObjects(xScale(), yScale(), this.screenWidth);
            this.lineObject.setXAxisLocation(0);
            if (yScale() == 6) {
                this.lineObject.setYAxisLocation(this.screenHeight / 2 + this.screenHeight / 10);
                if (blurPath() <= 0)
                    this.pathPaint.setMaskFilter(new BlurMaskFilter(blurPath(), BlurMaskFilter.Blur.NORMAL));
            }
            createBoxPaint();
            createTextPaint();
            createGradient();
            if (enableHeartbeat())
                createHeartPath();
            createBatteryPath();
            this.lineObject.setYAxisLocation(this.screenHeight / 2);
            this.pathPaint.setMaskFilter(null);
        }

        private void registerBatteryMonitor() {
            this.batteryLevelReceiver = new BroadcastReceiver() {
                public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
                    int i = paramAnonymousIntent.getIntExtra("level", -1);
                    int j = paramAnonymousIntent.getIntExtra("scale", -1);
                    ECGWallpaperService.ECGEngine.this.mBatteryLevel = -1;
                    if ((i >= 0) && (j > 0))
                        ECGWallpaperService.ECGEngine.this.mBatteryLevel = (i * 100 / j);
                    ECGWallpaperService.ECGEngine.this.mCharging = paramAnonymousIntent.getIntExtra("plugged", -1);
                    ECGWallpaperService.ECGEngine.this.setupBatteryOptions();
                }
            };
            registerBatteryReceiver();
        }

        private void registerBatteryReceiver() {
            IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
            ECGWallpaperService.this.registerReceiver(this.batteryLevelReceiver, localIntentFilter);
        }

        private void reset() {
            try {
                shutdownThread();
                ECGWallpaperService.this.unregisterReceiver(this.batteryLevelReceiver);
                if (this.lineObject != null)
                    this.lineObject.reset();
                this.lineObject = null;
                this.mCanvas = null;
            } catch (Exception localException) {
            }
        }

        private void setScreenDimensions() {
            Display localDisplay = ((WindowManager) ECGWallpaperService.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            this.screenWidth = localDisplay.getWidth();
            this.screenHeight = localDisplay.getHeight();
        }

        private void setupBatteryOptions() {
            if (this.mCharging != 0) {
                this.mWaveType = WaveType.SVT;
                this.mWaveColor = getSVTColor();
                this.mHeartRate = String.valueOf(220 - this.mBatteryLevel * 40 / 100);
            }
            if (!this.currentWaveType.equals(this.mWaveType)) {
                createGradient();
                this.currentWaveType = this.mWaveType;
            }
            if (this.mBatteryLevel <= this.asyBattery()) {
                this.mWaveType = WaveType.ASYSTOLE;
                this.mWaveColor = getASYColor();
                this.mHeartRate = "0";
            } else if ((this.mBatteryLevel > 5) && (this.mBatteryLevel <= vfibBattery())) {
                this.mWaveType = WaveType.VFIB;
                this.mWaveColor = getVFIBColor();
                this.mHeartRate = String.valueOf(300 - this.mBatteryLevel * 30 / 100);
            } else if ((this.mBatteryLevel > vfibBattery()) && (this.mBatteryLevel <= vtBattery())) {
                this.mWaveType = WaveType.VTACH;
                this.mWaveColor = getVTColor();
                this.mHeartRate = String.valueOf(200 - this.mBatteryLevel * 20 / 100);
            } else {
                this.mWaveType = WaveType.NORMAL_SINUS;
                this.mWaveColor = getNSColor();
                this.mHeartRate = String.valueOf(100 - this.mBatteryLevel * 40 / 100);
            }
        }

        private void setupGridPaint() {
            this.gridPaint.setColor(Color.rgb(0, 90, 0));
            this.gridPaint.setStyle(Paint.Style.STROKE);
        }

        private void setupPathPaint() {
            this.pathPaint.setStyle(Paint.Style.STROKE);
            this.pathPaint.setAntiAlias(true);
            this.pathPaint.setPathEffect(new CornerPathEffect(20.0F));
        }

        private void shutdownThread() {
            this.mScheduledExecutorService.shutdown();
            if (!this.mScheduledExecutorService.isTerminated()) {
                try {
                    this.mScheduledExecutorService.awaitTermination(1000L, TimeUnit.MILLISECONDS);
                    this.mScheduledExecutorService = null;
                } catch (InterruptedException localInterruptedException) {
                }
            }
        }

        private int strokeWidth() {
            return Integer.parseInt(this.mPrefs.getString(ECGWallpaperService.this.getString(R.string.ecg_line_width), ECGWallpaperService.this.getString(R.string.ecg_line_width_default)));
        }

        private void translateGradient() {
            double d2 = xScale() / wSpeed();
            double d1;
            if (d2 == 0.0D) {
                d1 = xScale() * 1.25D / wSpeed();
                this.mGradientMatrix.setTranslate((float) (this.lineObject.getLastPoint() - Math.round(d1) * this.screenWidth / 2L), this.screenHeight / 2);
                this.gradient.setLocalMatrix(this.mGradientMatrix);
                d1 = d2;
                if (d2 == 2.0D)
                    d1 = xScale() / (wSpeed() * 1.5D);
            }
        }

        private int vfibBattery() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.vfib_battery_level), Integer.parseInt(ECGWallpaperService.this.getString(R.string.vfib_battery_default)));
        }

        private int vtBattery() {
            return this.mPrefs.getInt(ECGWallpaperService.this.getString(R.string.vt_battery_level), Integer.parseInt(ECGWallpaperService.this.getString(R.string.vt_battery_default)));
        }

        private int wSpeed() {
            return Integer.parseInt(this.mPrefs.getString(ECGWallpaperService.this.getString(R.string.ecg_speed), ECGWallpaperService.this.getString(R.string.ecg_speed_default)));
        }

        private int xScale() {
            return Integer.parseInt(this.mPrefs.getString(ECGWallpaperService.this.getString(R.string.ecg_xscale), ECGWallpaperService.this.getString(R.string.ecg_xscale_default)));
        }

        private int yScale() {
            return Integer.parseInt(this.mPrefs.getString(ECGWallpaperService.this.getString(R.string.ecg_yscale), ECGWallpaperService.this.getString(R.string.ecg_yscale_default)));
        }


        public void onSurfaceCreated(SurfaceHolder paramSurfaceHolder) {
            super.onSurfaceCreated(paramSurfaceHolder);
            this.mSurfaceAvailable = true;
        }

        public void onSurfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
            this.mSurfaceAvailable = false;
            shutdownThread();
            super.onSurfaceDestroyed(paramSurfaceHolder);
        }

        public void onVisibilityChanged(boolean paramBoolean) {
            super.onVisibilityChanged(paramBoolean);
            if (paramBoolean) {
                registerBatteryReceiver();
                setScreenDimensions();
                if (this.lineObject == null)
                    initLineObject();
                this.mScheduledExecutorService = Executors.newScheduledThreadPool(1);
                ecg();
                return;
            }
            reset();
        }
    }
}