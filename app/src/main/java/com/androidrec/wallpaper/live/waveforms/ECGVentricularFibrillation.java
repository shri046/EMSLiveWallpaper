package com.androidrec.wallpaper.live.waveforms;

public class ECGVentricularFibrillation extends ECGWave {
    public static final int MAX = 116;

    private static final int[] X_COORDS = {0, 4, 6, 12, 18, 24, 30, 38, 40, 48, 54, 62, 72, 82, 86, 94, 96, 100, 104, 108, 116};

    private static final int[] Y_COORDS = {0, -32, -52, -52, -18, 14, 24, -22, -56, -58, -42, 18, 20, -32, -48, -46, -34, 0, 36, 44, 0};

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