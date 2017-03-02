package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.piticlistudio.playednext.R;

/**
 * View model for current items
 * Created by jorge.garcia on 02/03/2017.
 */
public class CurrentItemModel extends BaseItemModel<CurrentItemModel.CurrentHolder> {

    @EpoxyAttribute
    String title;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected CurrentHolder createNewHolder() {
        return new CurrentHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.gamerelation_list_adapter_row_current;
    }

    @Override
    public void bind(CurrentHolder holder) {
        super.bind(holder);
        holder.text.setText(title);
        imageloader.load(imageURL)
                .fit()
                .centerCrop()
                .into(holder.cover);
    }

    static class CurrentHolder extends BaseItemHolder {

    }
}
