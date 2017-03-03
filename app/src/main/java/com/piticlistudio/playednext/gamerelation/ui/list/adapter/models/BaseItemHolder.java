package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseItemHolder extends EpoxyHolder {

    @BindView(R.id.title)
    TextView text;

    @BindView(R.id.cover)
    ImageView cover;

    @BindView(R.id.subtitle)
    TextView subtitle;

    @BindView(R.id.overlay)
    View itemView;

    @BindView(R.id.destroyText)
    TextView rightSwipeMessage;

    @BindView(R.id.positiveText)
    TextView leftSwipeMessage;

    @CallSuper
    @Override
    protected void bindView(View itemView) {
        ButterKnife.bind(this, itemView);
        this.rightSwipeMessage = (TextView) itemView.findViewById(R.id.destroyText);
        this.leftSwipeMessage = (TextView) itemView.findViewById(R.id.positiveText);
    }
}
