package com.piticlistudio.playednext.ui.recyclerview;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount = 1;

    public SpacesItemDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

//        Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) >= spanCount) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
