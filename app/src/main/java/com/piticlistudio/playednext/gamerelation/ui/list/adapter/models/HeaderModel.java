package com.piticlistudio.playednext.gamerelation.ui.list.adapter.models;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel.HeaderHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View model representing a header
 * Created by jorge.garcia on 01/03/2017.
 */

public class HeaderModel extends EpoxyModelWithHolder<HeaderHolder> {

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    @DrawableRes
    int icon;

    @EpoxyAttribute
    @ColorInt
    int color;

    @Override
    protected HeaderHolder createNewHolder() {
        return new HeaderHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.gamerelation_list_header;
    }

    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    @Override
    public void bind(HeaderHolder holder) {
        holder.text.setText(title);
        holder.icon.setImageResource(icon);
        holder.text.setTextColor(color);
        holder.icon.setColorFilter(color);
        holder.border.setBackgroundColor(color);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    static class HeaderHolder extends EpoxyHolder {

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.image)
        ImageView icon;

        @BindView(R.id.border)
        View border;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
            this.text = (TextView) itemView.findViewById(R.id.text);
            this.icon = (ImageView) itemView.findViewById(R.id.image);
            this.border = itemView.findViewById(R.id.border);
        }
    }
}
