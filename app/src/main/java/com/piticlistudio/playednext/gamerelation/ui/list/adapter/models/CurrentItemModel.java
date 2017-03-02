package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        holder.text.setText(title);
        holder.subtitle.setText(subtitle);
        imageloader.load(imageURL)
                .fit()
                .centerCrop()
                .into(holder.cover);
        holder.itemView.setOnClickListener(clickListener);
    }

    static class CurrentHolder extends EpoxyHolder {

        @BindView(R.id.title)
        TextView text;

        @BindView(R.id.cover)
        ImageView cover;

        @BindView(R.id.subtitle)
        TextView subtitle;

        @BindView(R.id.overlay)
        View itemView;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
