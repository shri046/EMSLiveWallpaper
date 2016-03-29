package com.androidrec.wallpaper.live.waveforms;

public class ECGNormalSinus extends ECGWave {
    public static final int MAX = 96;
    public static final int[] X_COORDS = {0, 4, 8, 12, 16, 18, 22, 26, 30, 32, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 60, 64, 68, 72, 76, 80, 84, 90, 96};
    public static final int[] Y_COORDS = {0, 0, 0, 0, 0, -4, -8, -4, 0, 0, 0, 4, 8, -30, -62, -62, -30, 0, 20, 0, 0, 0, -2, -6, -10, -6, -2, 0, 0, 0};

    @Override
    public int[] getXCoordinates() {
        return X_COORDS;
    }

    @Override
    public int[] getYCoordinates() {
        return Y_COORDS;
    }

    @Override
    public int getMax() {
        return MAX;
    }
}