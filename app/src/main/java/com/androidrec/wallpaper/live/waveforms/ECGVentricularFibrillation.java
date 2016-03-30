// 
// Decompiled by Procyon v0.5.30
// 

package com.androidrec.wallpaper.live.waveforms;

import java.util.List;

public class ECGVentricularFibrillation
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
            list.add(n2 * 6 + n3);
            list.add(n2 * 12 + n3);
            list.add(n2 * 18 + n3);
            list.add(n2 * 24 + n3);
            list.add(n2 * 30 + n3);
            list.add(n2 * 38 + n3);
            list.add(n2 * 40 + n3);
            list.add(n2 * 48 + n3);
            list.add(n2 * 54 + n3);
            list.add(n2 * 62 + n3);
            list.add(n2 * 72 + n3);
            list.add(n2 * 82 + n3);
            list.add(n2 * 86 + n3);
            list.add(n2 * 94 + n3);
            list.add(n2 * 96 + n3);
            list.add(n2 * 100 + n3);
            list.add(n2 * 104 + n3);
            list.add(n2 * 108 + n3);
            list.add(n2 * 116 + n3);
            intValue = list.get(list.size() - 1);
        }
        return list;
    }
    
    public static List<Integer> getY(final List<Integer> list, final int n, final int n2) {
        for (int i = 1; i <= n + 1; ++i) {
            list.add(n2 * 0);
            list.add(n2 * -32);
            list.add(n2 * -52);
            list.add(n2 * -52);
            list.add(n2 * -18);
            list.add(n2 * 14);
            list.add(n2 * 24);
            list.add(n2 * -22);
            list.add(n2 * -56);
            list.add(n2 * -58);
            list.add(n2 * -42);
            list.add(n2 * 18);
            list.add(n2 * 20);
            list.add(n2 * -32);
            list.add(n2 * -48);
            list.add(n2 * -46);
            list.add(n2 * -34);
            list.add(n2 * 0);
            list.add(n2 * 36);
            list.add(n2 * 44);
            list.add(n2 * 0);
        }
        return list;
    }
}
