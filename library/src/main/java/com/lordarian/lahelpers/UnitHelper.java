package com.lordarian.lahelpers;

import android.content.Context;
import android.util.DisplayMetrics;

public class UnitHelper {

    public static int dpToPx(Context context, int dps) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}