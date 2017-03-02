package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.graphics.Paint;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.ui.picasso.GrayscaleTransformation;

/**
 * Adapter model representing a completed relation entity
 * Created by jorge.garcia on 01/03/2017.
 */
public class CompletedItemModel extends BaseItemModel<CompletedItemModel.CompletedHolder> {

    @EpoxyAttribute
    String title;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected CompletedHolder createNewHolder() {
        return new CompletedHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.gamerelation_list_adapter_row_completed;
    }

    @Override
    public void bind(CompletedHolder holder) {
        super.bind(holder);
        holder.text.setText(title);
        holder.text.setPaintFlags(holder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        imageloader.load(imageURL)
                .transform(new GrayscaleTransformation())
                .fit()
                .centerInside()
                .into(holder.cover);
    }

    static class CompletedHolder extends BaseItemHolder {

    }
}
