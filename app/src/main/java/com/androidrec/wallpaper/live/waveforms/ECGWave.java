package com.androidrec.wallpaper.live.waveforms;

import java.util.List;

public abstract class ECGWave {

    public abstract int[] getXCoordinates();
    public abstract int[] getYCoordinates();
    public abstract int getMax();

    public List<Integer> getX(List<Integer> paramList, int paramInt1, int paramInt2) {
        int j = 0;
        int i = 1;
        while (true) {
            if (i > paramInt1 + 1)
                return paramList;
            int k = 0;
            if (i > 1)
                k = j;
            for(int x_cord : this.getXCoordinates()) {
                paramList.add(paramInt2 * x_cord + k);
            }
            j = paramList.get(paramList.size() - 1);
            i += 1;
        }
    }

    public List<Integer> getY(List<Integer> paramList, int paramInt1, int paramInt2) {
        int i = 1;
        while (true) {
            if (i > paramInt1 + 1)
                return paramList;
            for(int y_cord : this.getYCoordinates()) {
                paramList.add(paramInt2 * y_cord);
            }
            i += 1;
        }
    }
}