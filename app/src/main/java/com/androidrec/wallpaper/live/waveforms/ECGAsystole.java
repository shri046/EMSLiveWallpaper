// 
// Decompiled by Procyon v0.5.30
// 

package com.androidrec.wallpaper.live.waveforms;

import java.util.List;

public class ECGAsystole
{
    public static final int MAX = 116;
    
    public static List<Integer> getX(final List<Integer> list, final int n, final int n2) {
        int intValue = 0;
        for (int i = 1; i <= n + 1; ++i) {
            int n3 = 0;
            if (i > 1) {
                n3 = 0 + intValue;
            }
            list.add(n2 * 0 + n3);
            list.add(n2 * 4 + n3);
            list.add(n2 * 10 + n3);
            list.add(n2 * 12 + n3);
            list.add(n2 * 16 + n3);
            list.add(n2 * 18 + n3);
            list.add(n2 * 20 + n3);
            list.add(n2 * 22 + n3);
            list.add(n2 * 24 + n3);
            list.add(n2 * 30 + n3);
            list.add(n2 * 36 + n3);
            list.add(n2 * 38 + n3);
            list.add(n2 * 48 + n3);
            list.add(n2 * 54 + n3);
            list.add(n2 * 60 + n3);
            list.add(n2 * 66 + n3);
            list.add(n2 * 72 + n3);
            list.add(n2 * 78 + n3);
            list.add(n2 * 84 + n3);
            list.add(n2 * 92 + n3);
            list.add(n2 * 98 + n3);
            list.add(n2 * 104 + n3);
            list.add(n2 * 110 + n3);
            list.add(n2 * 116 + n3);
            intValue = list.get(list.size() - 1);
        }
        return list;
    }
    
    public static List<Integer> getY(final List<Integer> list, final int n, final int n2) {
        for (int i = 1; i <= n + 1; ++i) {
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * -2);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 1);
            list.add(n2 * 2);
            list.add(n2 * 1);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 1);
            list.add(n2 * 0);
            list.add(n2 * 0);
        }
        return list;
    }
}
