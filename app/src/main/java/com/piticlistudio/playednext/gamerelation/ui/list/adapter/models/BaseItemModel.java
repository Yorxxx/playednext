package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.graphics.Paint;
import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.ui.picasso.GrayscaleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public void bind(T holder) {
        super.bind(holder);
        holder.text.setText(title);
        holder.subtitle.setText(subtitle);
        holder.itemView.setOnClickListener(clickListener);
    }
}
