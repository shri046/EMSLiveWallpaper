// 
// Decompiled by Procyon v0.5.30
// 

package com.androidrec.wallpaper.live.waveforms;

import java.util.List;

public class ECGNormalSinus
{
    public static final int MAX = 96;
    
    public static List<Integer> getX(final List<Integer> list, final int n, final int n2) {
        int intValue = 0;
        for (int i = 1; i <= n + 1; ++i) {
            int n3 = 0;
            if (i > 1) {
                n3 = 0 + intValue;
            }
            list.add(n2 * 0 + n3);
            list.add(n2 * 4 + n3);
            list.add(n2 * 8 + n3);
            list.add(n2 * 12 + n3);
            list.add(n2 * 16 + n3);
            list.add(n2 * 18 + n3);
            list.add(n2 * 22 + n3);
            list.add(n2 * 26 + n3);
            list.add(n2 * 30 + n3);
            list.add(n2 * 32 + n3);
            list.add(n2 * 36 + n3);
            list.add(n2 * 38 + n3);
            list.add(n2 * 40 + n3);
            list.add(n2 * 42 + n3);
            list.add(n2 * 44 + n3);
            list.add(n2 * 46 + n3);
            list.add(n2 * 48 + n3);
            list.add(n2 * 50 + n3);
            list.add(n2 * 52 + n3);
            list.add(n2 * 54 + n3);
            list.add(n2 * 60 + n3);
            list.add(n2 * 64 + n3);
            list.add(n2 * 68 + n3);
            list.add(n2 * 72 + n3);
            list.add(n2 * 76 + n3);
            list.add(n2 * 80 + n3);
            list.add(n2 * 84 + n3);
            list.add(n2 * 90 + n3);
            list.add(n2 * 96 + n3);
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
            list.add(n2 * 0);
            list.add(n2 * -4);
            list.add(n2 * -8);
            list.add(n2 * -4);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 4);
            list.add(n2 * 8);
            list.add(n2 * -30);
            list.add(n2 * -62);
            list.add(n2 * -62);
            list.add(n2 * -30);
            list.add(n2 * 0);
            list.add(n2 * 20);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * -2);
            list.add(n2 * -6);
            list.add(n2 * -10);
            list.add(n2 * -6);
            list.add(n2 * -2);
            list.add(n2 * 0);
            list.add(n2 * 0);
            list.add(n2 * 0);
        }
        return list;
    }
}
