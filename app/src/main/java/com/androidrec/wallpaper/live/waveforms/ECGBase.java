// 
// Decompiled by Procyon v0.5.30
// 

package com.androidrec.wallpaper.live.waveforms;

import java.util.List;

public interface ECGBase
{
    List<Integer> getX(final List<Integer> p0, final int p1, final int p2);
    
    List<Integer> getY(final List<Integer> p0, final int p1, final int p2);
}
