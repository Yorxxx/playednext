package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.piticlistudio.playednext.R;


public class WaitingItemModel extends BaseItemModel<WaitingItemModel.WaitingHolder> {

    @EpoxyAttribute
    int backgroundColor;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected WaitingHolder createNewHolder() {
        return new WaitingHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.gamerelation_list_adapter_row_waiting;
    }

    @Override
    public void bind(WaitingHolder holder) {
        super.bind(holder);
        imageloader.load(imageURL)
                .fit()
                .centerCrop()
                .into(holder.cover);
        holder.itemView.setBackgroundColor(backgroundColor);
    }

    static class WaitingHolder extends BaseItemHolder {

    }
}
