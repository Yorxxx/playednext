package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;
import com.squareup.picasso.Picasso;

/**
 * Base view model for gamerelation items
 * Created by jorge.garcia on 02/03/2017.
 */

public abstract class BaseItemModel<T extends BaseItemHolder> extends EpoxyModelWithHolder<T> {

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    String subtitle;

    @EpoxyAttribute
    String imageURL;

    @EpoxyAttribute(hash = false)
    Picasso imageloader;

    @EpoxyAttribute(hash = false)
    View.OnClickListener clickListener;

    @EpoxyAttribute
    String rightSwipeMessage;

    @EpoxyAttribute
    String leftSwipeMessage;

    @Override
    public void bind(T holder) {
        super.bind(holder);
        holder.text.setText(title);
        holder.subtitle.setText(subtitle);
        holder.itemView.setOnClickListener(clickListener);
        holder.rightSwipeMessage.setText(rightSwipeMessage);
        holder.leftSwipeMessage.setText(leftSwipeMessage);
        holder.itemView.findViewById(R.id.overlay).setTranslationX(0);
    }
}
