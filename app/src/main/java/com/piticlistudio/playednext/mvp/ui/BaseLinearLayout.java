package com.piticlistudio.playednext.mvp.ui;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.di.component.AppComponent;

public class BaseLinearLayout extends LinearLayout {

    public BaseLinearLayout(Context context) {
        super(context);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public BaseLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected AppComponent getApplicationComponent() {
        Activity activity = getActivity();
        if (activity != null) {
            return ((AndroidApplication)activity.getApplication()).getApplicationComponent();
        }
        return null;
    }

    @Nullable
    protected Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
