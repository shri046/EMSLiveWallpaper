package com.androidrec.wallpaper.live.waveforms;

import java.util.ArrayList;
import java.util.List;

public abstract class ECGWave {

    public abstract int[] getXCoordinates();

    public abstract int[] getYCoordinates();

    public abstract int getMax();

    public List<Integer> getX(int paramInt1, int scale) {
        return this.getCoordinates(this.getXCoordinates(), scale);
    }

    public List<Integer> getY(int paramInt1, int scale) {
        return this.getCoordinates(this.getYCoordinates(), scale);
    }

    private List<Integer> getCoordinates(int[] coords, int scale) {
        List<Integer> paramList = new ArrayList<Integer>();
        for (int y_cord : coords) {
            paramList.add(scale * y_cord);
        }
        return paramList;
    }
}