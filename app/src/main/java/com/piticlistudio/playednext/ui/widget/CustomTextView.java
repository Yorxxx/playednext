package com.piticlistudio.playednext.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.piticlistudio.playednext.utils.CustomFontHelper;

public class CustomTextView extends TextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}
