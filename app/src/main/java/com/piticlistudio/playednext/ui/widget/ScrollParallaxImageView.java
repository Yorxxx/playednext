package com.piticlistudio.playednext.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.piticlistudio.playednext.utils.UIUtils;

/**
 * An ImageView which scales down or up based on its position within the window
 * You need to invalidate the view whenever a change on scroll happens
 * Created by jorge.garcia on 16/02/2017.
 */

public class ScrollParallaxImageView extends ImageView {

    private final float SCALE_RATIO = 0.25f;
    private int[] viewLocation = new int[2];

    public ScrollParallaxImageView(Context context) {
        super(context);
    }

    public ScrollParallaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollParallaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            getLocationInWindow(viewLocation);
            applyScaleTransformation(canvas, viewLocation[0], viewLocation[1]);
        }

        super.onDraw(canvas);
    }

    private void applyScaleTransformation(Canvas canvas, int x, int y) {

        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        int deviceHeight = UIUtils.getScreenHeight(getContext());

        if (y == 0) {
            return;
        }

        float scale = 2 * (1 - SCALE_RATIO) * (y + height) / (deviceHeight + height) + SCALE_RATIO;
        canvas.scale(scale, scale, width / 2, height / 2);
    }
}
