package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.squareup.picasso.Picasso;

/**
 * Base view model for gamerelation items
 * Created by jorge.garcia on 02/03/2017.
 */

public abstract class BaseItemModel<T extends EpoxyHolder> extends EpoxyModelWithHolder<T> {

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
}
