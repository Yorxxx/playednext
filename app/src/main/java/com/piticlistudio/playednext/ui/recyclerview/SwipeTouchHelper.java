package com.piticlistudio.playednext.ui.recyclerview;

import android.content.ClipData;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * A touch view helper for swiping and dragging items
 * Created by jorge.garcia on 03/03/2017.
 */
public class SwipeTouchHelper extends ItemTouchHelper.Callback {

    private final SwipeableAdapter adapter;

    public SwipeTouchHelper(SwipeableAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int pos = viewHolder.getAdapterPosition();
        if (adapter.isSwipeToLeftEnabled(pos) || adapter.isSwipeToRightEnabled(pos)) {
            int dragFlags = 0;
            int swipeFlags = 0;
            if (adapter.isSwipeToLeftEnabled(pos)) {
                swipeFlags = ItemTouchHelper.START;
            }
            if (adapter.isSwipeToRightEnabled(pos)) {
                swipeFlags = swipeFlags | ItemTouchHelper.END;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
       adapter.onItemSwipe(viewHolder, viewHolder.getAdapterPosition(), direction);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View view = adapter.getSwipeView(viewHolder);
            if (view != null) {
                view.setTranslationX(dX);
                view.setTranslationY(dY);
            }
            adapter.onSwiping(viewHolder, dX, dY);
            super.onChildDraw(c, recyclerView, viewHolder, 0, 0, actionState, isCurrentlyActive);
        }
    }
}
