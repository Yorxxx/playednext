package com.piticlistudio.playednext.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * A default progressbar which just replaces the indeterminate drawable when running tests
 * to avoid Espresso to keep stuck waiting for Idle status.
 * @see https://stackoverflow.com/questions/33289152/progressbars-and-espresso
 * Created by jorge on 22/5/17.
 */

public class CustomProgressBar extends ProgressBar {

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (isRunningTest()) {
            d = new ColorDrawable(0xffff0000);
        }
        super.setIndeterminateDrawable(d);
    }

    private boolean isRunningTest() {
        try {
            Class.forName("android.support.test.espresso.Espresso");
            return true;
        } catch (ClassNotFoundException e) {
        /* no-op */
        }
        return false;
    }
}
