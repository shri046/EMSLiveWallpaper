// 
// Decompiled by Procyon v0.5.30
// 

package com.androidrec.wallpaper.live.utils;

import com.androidrec.wallpaper.live.waveforms.ECGVentricularTachycardia;
import com.androidrec.wallpaper.live.waveforms.ECGVentricularFibrillation;
import com.androidrec.wallpaper.live.waveforms.ECGSuperVentricularTachycardia;
import com.androidrec.wallpaper.live.waveforms.ECGNormalSinus;
import com.androidrec.wallpaper.live.waveforms.ECGAsystole;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Path;
import com.androidrec.wallpaper.live.constants.WaveType;

public class LineObjects
{
    private int lastPoint;
    private final int mScreenWidth;
    private WaveType mWaveType;
    private final int mXScale;
    private final int mYScale;
    private int numSamples;
    private final Path path;
    private List<Integer> waveformX;
    private List<Integer> waveformY;
    private int xAxisLocation;
    private final int xOffset;
    private int yAxisLocation;

    public LineObjects(final int mxScale, final int myScale, final int mScreenWidth) {
        this.xAxisLocation = 0;
        this.yAxisLocation = 300;
        this.xOffset = 0;
        this.waveformX = new ArrayList<Integer>();
        this.waveformY = new ArrayList<Integer>();
        this.path = new Path();
        this.mXScale = mxScale;
        this.mYScale = myScale;
        this.mScreenWidth = mScreenWidth;
    }

    private void getASYECG() {
        if (!WaveType.ASYSTOLE.equals(this.mWaveType)) {
            this.waveformX = ECGAsystole.getX(new ArrayList<Integer>(), this.numCycles(116), this.mXScale);
            this.waveformY = ECGAsystole.getY(new ArrayList<Integer>(), this.numCycles(116), this.mYScale);
            this.mWaveType = WaveType.ASYSTOLE;
        }
    }

    private void getNormalECG() {
        if (!WaveType.NORMAL_SINUS.equals(this.mWaveType)) {
            this.waveformX = ECGNormalSinus.getX(new ArrayList<Integer>(), this.numCycles(96), this.mXScale);
            this.waveformY = ECGNormalSinus.getY(new ArrayList<Integer>(), this.numCycles(96), this.mYScale);
            this.mWaveType = WaveType.NORMAL_SINUS;
        }
    }

    private void getSVTECG() {
        if (!WaveType.SVT.equals(this.mWaveType)) {
            this.waveformX = ECGSuperVentricularTachycardia.getX(new ArrayList<Integer>(), this.numCycles(54), this.mXScale);
            this.waveformY = ECGSuperVentricularTachycardia.getY(new ArrayList<Integer>(), this.numCycles(54), this.mYScale);
            this.mWaveType = WaveType.SVT;
        }
    }

    private void getVFIBECG() {
        if (!WaveType.VFIB.equals(this.mWaveType)) {
            this.waveformX = ECGVentricularFibrillation.getX(new ArrayList<Integer>(), this.numCycles(116), this.mXScale);
            this.waveformY = ECGVentricularFibrillation.getY(new ArrayList<Integer>(), this.numCycles(116), this.mYScale);
            this.mWaveType = WaveType.VFIB;
        }
    }

    private void getVTECG() {
        if (!WaveType.VTACH.equals(this.mWaveType)) {
            this.waveformX = ECGVentricularTachycardia.getX(new ArrayList<Integer>(), this.numCycles(78), this.mXScale);
            this.waveformY = ECGVentricularTachycardia.getY(new ArrayList<Integer>(), this.numCycles(78), this.mYScale);
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

    private int numCycles(final int n) {
        if (this.mXScale > 0) {
            return (int)Math.ceil(this.mScreenWidth / (this.mXScale * n));
        }
        return (int)Math.ceil(this.mScreenWidth / n);
    }

    public int getLastPoint() {
        return this.lastPoint;
    }

    public int getNumberOfSamples() {
        return this.numSamples;
    }

    public Path getPath(final WaveType waveType, final int n, final int n2) {
        this.path.reset();
        if (WaveType.VTACH.equals(waveType)) {
            return this.getWaveformPath(n, n2);
        }
        if (WaveType.SVT.equals(waveType)) {
            return this.getWaveformPath(n, n2);
        }
        if (WaveType.VFIB.equals(waveType)) {
            return this.getWaveformPath(n, n2);
        }
        if (WaveType.ASYSTOLE.equals(waveType)) {
            return this.getWaveformPath(n, n2);
        }
        return this.getWaveformPath(n, n2);
    }

    public void reset() {
        this.waveformX = null;
        this.waveformY = null;
    }

    public void setWaveType(final WaveType waveType) {
        if (WaveType.ASYSTOLE.equals(waveType)) {
            this.getASYECG();
            return;
        }
        if (WaveType.VFIB.equals(waveType)) {
            this.getVFIBECG();
            return;
        }
        if (WaveType.VTACH.equals(waveType)) {
            this.getVTECG();
            return;
        }
        if (WaveType.SVT.equals(waveType)) {
            this.getSVTECG();
            return;
        }
        this.getNormalECG();
    }

    public void setXAxisLocation(final int xAxisLocation) {
        this.xAxisLocation = xAxisLocation;
    }

    public void setYAxisLocation(final int yAxisLocation) {
        this.yAxisLocation = yAxisLocation;
    }
}
