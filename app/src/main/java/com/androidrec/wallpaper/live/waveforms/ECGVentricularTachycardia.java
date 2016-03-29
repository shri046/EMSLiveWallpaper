package com.androidrec.wallpaper.live.waveforms;

public class ECGVentricularTachycardia extends ECGWave {
    public static final int MAX = 78;

    public static final int[] X_COORDS = {0, 8, 18, 22, 24, 26, 30, 34, 38, 40, 42, 46, 50, 58, 64, 70, 78};

    public static final int[] Y_COORDS = {0, -6, 0, -18, -32, -52, -68, -64, -60, -62, -38, 16, 4, -2, 2, 10, 0};

    @Override
    public int[] getXCoordinates() {
        return X_COORDS;
    }

    @Override
    public int[] getYCoordinates() {
        return Y_COORDS;
    }

    @Override
    public int getMax() { return MAX; }
}