package com.piticlistudio.playednext.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class UIUtils {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    /**
     * Returns the number of pixels corresponding to the specified amount of dp.
     *
     * @param dp the number of density pixels to convert.
     * @return the amount of pixels.
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    /**
     * Returns a suitable text color that allows to be readable
     *
     * @param backgroundColor the background of the text
     * @return a textcolor that allows reading even when using a background behind it
     */
    public static int getTextColorForBackground(int backgroundColor) {
        int d = 0;
        int red = Color.red(backgroundColor);
        int green = Color.green(backgroundColor);
        int blue = Color.blue(backgroundColor);

        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * red + 0.587 * green + 0.114 * blue) / 255;

        if (a < 0.5)
            d = 0; // bright colors - black font
        else
            d = 255; // dark colors - white font

        return Color.rgb(d, d, d);
    }
}
