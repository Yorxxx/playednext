package com.piticlistudio.playednext.game.ui.search.view.adapter;


import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ProgressBar;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View model for for the view that allows to load more data on the model.
 * Created by jorge.garcia on 13/12/2016.
 */

public class GameSearchLoadMoreModel extends EpoxyModelWithHolder<GameSearchLoadMoreModel.LoadMoreHolder> {

    @EpoxyAttribute
    @StringRes
    int text;

    /**
     * Subclasses can override this if they want their view to take up more than one span in a grid
     * layout.
     *
     * @param totalSpanCount The number of spans in the grid
     * @param position       The position of the model
     * @param itemCount      The total number of items in the adapter
     */
    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected LoadMoreHolder createNewHolder() {
        return new LoadMoreHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.game_search_loadingmore;
    }

    @Override
    public void bind(LoadMoreHolder holder) {
//        holder.text.setText(text);
    }

    static class LoadMoreHolder extends EpoxyHolder {

        @BindView(R.id.progress)
        ProgressBar progress;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
