package com.androidrec.wallpaper.live.waveforms;

public class ECGAsystole extends ECGWave {
    public static final int MAX = 116;

    public static final int[] X_COORDS = {0, 4, 10, 12, 16, 18, 20, 22, 24, 30, 36, 38, 48, 54, 60, 66, 72, 78, 84, 92, 98, 104, 110, 116};
    public static final int[] Y_COORDS = {0, 0, 0, 0, 0, -2, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};

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