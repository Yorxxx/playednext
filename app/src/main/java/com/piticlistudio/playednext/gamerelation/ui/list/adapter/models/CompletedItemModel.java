package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.graphics.Paint;
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
 * Adapter model representing a completed relation entity
 * Created by jorge.garcia on 01/03/2017.
 */

public class CompletedItemModel extends EpoxyModelWithHolder<CompletedItemModel.CompletedHolder> {

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    String subtitle;

    @EpoxyAttribute
    String imageURL;

    @EpoxyAttribute
    Picasso imageloader;

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
        holder.text.setText(title);
        holder.subtitle.setText(subtitle);
        holder.text.setPaintFlags(holder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        imageloader.load(imageURL)
                .transform(new GrayscaleTransformation())
                .fit()
                .centerInside()
                .into(holder.cover);
    }

    static class CompletedHolder extends EpoxyHolder {

        @BindView(R.id.title)
        TextView text;

        @BindView(R.id.cover)
        ImageView cover;

        @BindView(R.id.subtitle)
        TextView subtitle;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
