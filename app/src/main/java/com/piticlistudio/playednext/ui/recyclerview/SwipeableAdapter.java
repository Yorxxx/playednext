package com.piticlistudio.playednext.ui.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Interface defining method for an adapter that can swipe items
 * Created by jorge.garcia on 03/03/2017.
 */

public interface SwipeableAdapter {

    /**
     * Returns if the specified position has swipe to right enabled.
     *
     * @param position the position to check.
     * @return true if swipe is enabled. False otherwise.
     */
    boolean isSwipeToRightEnabled(int position);

    /**
     * Returns if the specified position has swipe to left enabled.
     *
     * @param position the position to check.
     * @return true if swipe is enabled. False otherwise.
     */
    boolean isSwipeToLeftEnabled(int position);

    /**
     * Indicates that the holder has been swiped
     *
     * @param viewHolder the holder.
     * @param position   the position swiped
     * @param direction  the direction of the swipe.
     */
    void onItemSwipe(RecyclerView.ViewHolder viewHolder, int position, int direction);

    /**
     * Returns the view that the swipe translation will be applied to
     *
     * @param viewHolder the holder requesting the view
     * @return the view
     */
    View getSwipeView(RecyclerView.ViewHolder viewHolder);

    void onSwiping(RecyclerView.ViewHolder viewHolder, float dX, float dY);
}
