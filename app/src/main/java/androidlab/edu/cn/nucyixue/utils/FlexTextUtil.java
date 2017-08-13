package androidlab.edu.cn.nucyixue.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by dreamY on 2017/7/26.
 */

public class FlexTextUtil {
    public static int pixelToDp(Context context, int pixel) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return pixel < 0 ? pixel : Math.round(pixel / displayMetrics.density);
    }
    public static int dpToPixel(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return dp < 0 ? dp : Math.round(dp * displayMetrics.density);
    }
}
