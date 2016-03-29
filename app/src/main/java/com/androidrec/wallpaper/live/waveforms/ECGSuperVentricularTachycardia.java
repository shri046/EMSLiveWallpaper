package com.androidrec.wallpaper.live.waveforms;

public class ECGSuperVentricularTachycardia extends ECGWave {
    public static final int MAX = 54;

    public static final int[] X_COORDS = {0, 4, 10, 12, 16, 18, 20, 22, 24, 30, 36, 38, 48, 54};

    public static final int[] Y_COORDS = {0, 0, -4, -24, -50, -62, -30, -10, 0, 8, 12, 4, 0, 0};

    @Override
    public int[] getXCoordinates() { return X_COORDS; }

    @Override
    public int[] getYCoordinates() {
        return Y_COORDS;
    }

    @Override
    public int getMax() { return MAX; }
}