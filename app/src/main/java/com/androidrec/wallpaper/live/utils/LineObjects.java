package com.androidrec.wallpaper.live.utils;

import android.graphics.Path;

import com.androidrec.wallpaper.live.constants.WaveType;
import com.androidrec.wallpaper.live.waveforms.ECGAsystole;
import com.androidrec.wallpaper.live.waveforms.ECGNormalSinus;
import com.androidrec.wallpaper.live.waveforms.ECGSuperVentricularTachycardia;
import com.androidrec.wallpaper.live.waveforms.ECGVentricularFibrillation;
import com.androidrec.wallpaper.live.waveforms.ECGWave;

import java.util.ArrayList;
import java.util.List;

public class LineObjects {
    private int lastPoint;
    private final int mScreenWidth;
    private WaveType mWaveType;
    private final int mXScale;
    private final int mYScale;
    private int numSamples;
    private final Path path = new Path();
    private List<Integer> waveformX = new ArrayList<Integer>();
    private List<Integer> waveformY = new ArrayList<Integer>();
    private int xAxisLocation = 0;
    private final int xOffset = 0;
    private int yAxisLocation = 300;
    private ECGWave wave;

    public LineObjects(int paramInt1, int paramInt2, int paramInt3) {
        this.mXScale = paramInt1;
        this.mYScale = paramInt2;
        this.mScreenWidth = paramInt3;
    }

    private void getASYECG() {
        if (!WaveType.ASYSTOLE.equals(this.mWaveType)) {
            this.wave = new ECGAsystole();
            this.getCoordinates();
            this.mWaveType = WaveType.ASYSTOLE;
        }
    }

    private void getCoordinates() {
        this.waveformX = this.wave.getX(numCycles(this.wave.getMax()), this.mXScale);
        this.waveformY = this.wave.getY(numCycles(this.wave.getMax()), this.mYScale);
        this.numSamples = numCycles(this.waveformX.size()) * this.waveformX.size();
    }

    private void getNormalECG() {
        if (!WaveType.NORMAL_SINUS.equals(this.mWaveType)) {
            this.wave = new ECGNormalSinus();
            this.getCoordinates();
            this.mWaveType = WaveType.NORMAL_SINUS;
        }
    }

    private void getSVTECG() {
        if (!WaveType.SVT.equals(this.mWaveType)) {
            this.wave = new ECGSuperVentricularTachycardia();
            this.getCoordinates();
            this.mWaveType = WaveType.SVT;
        }
    }

    private void getVFIBECG() {
        if (!WaveType.VFIB.equals(this.mWaveType)) {
            this.wave = new ECGVentricularFibrillation();
            this.getCoordinates();
            this.mWaveType = WaveType.VFIB;
        }
    }

    private void getVTECG() {
        if (!WaveType.VTACH.equals(this.mWaveType)) {
            this.wave = new ECGSuperVentricularTachycardia();
            this.getCoordinates();
            this.mWaveType = WaveType.VTACH;
        }
    }

    private Path getWaveformPath(int i, final int n) {
        if (i == 0) {
            this.path.moveTo((float)this.xAxisLocation, (float)this.yAxisLocation);
        }
        else {
            this.path.moveTo((float)(this.waveformX.get(i) + this.xAxisLocation + 0), (float)(this.waveformY.get(i) + this.yAxisLocation));
        }
        while (i < n) {
            try {
                final int lastPoint = this.waveformX.get(i) + this.xAxisLocation + 0;
                this.path.lineTo((float)lastPoint, (float)(this.waveformY.get(i) + this.yAxisLocation));
                this.lastPoint = lastPoint;
                ++i;
            }
            catch (Exception ex) {
                break;
            }
        }
        if (n == 0) {
            this.lastPoint = this.xAxisLocation;
        }
        this.numSamples = this.waveformX.size();
        return this.path;
    }

    private int numCycles(int paramInt) {
        if (this.mXScale > 0)
            return (int) Math.ceil(this.mScreenWidth / (this.mXScale * paramInt));
        return (int) Math.ceil(this.mScreenWidth / paramInt);
    }

    public int getLastPoint() {
        return this.lastPoint;
    }

    public int getNumberOfSamples() {
        return this.numSamples;
    }

    public Path getPath(WaveType paramWaveType, int paramInt1, int paramInt2) {
        this.path.reset();
        if (WaveType.VTACH.equals(paramWaveType))
            return getWaveformPath(paramInt1, paramInt2);
        if (WaveType.SVT.equals(paramWaveType))
            return getWaveformPath(paramInt1, paramInt2);
        if (WaveType.VFIB.equals(paramWaveType))
            return getWaveformPath(paramInt1, paramInt2);
        if (WaveType.ASYSTOLE.equals(paramWaveType))
            return getWaveformPath(paramInt1, paramInt2);
        return getWaveformPath(paramInt1, paramInt2);
    }

    public void reset() {
        this.waveformX = null;
        this.waveformY = null;
    }

    public void setWaveType(WaveType paramWaveType) {
        if (WaveType.ASYSTOLE.equals(paramWaveType)) {
            getASYECG();
            return;
        }
        if (WaveType.VFIB.equals(paramWaveType)) {
            getVFIBECG();
            return;
        }
        if (WaveType.VTACH.equals(paramWaveType)) {
            getVTECG();
            return;
        }
        if (WaveType.SVT.equals(paramWaveType)) {
            getSVTECG();
            return;
        }
        getNormalECG();
    }

    public void setXAxisLocation(int paramInt) {
        this.xAxisLocation = paramInt;
    }

    public void setYAxisLocation(int paramInt) {
        this.yAxisLocation = paramInt;
    }
}