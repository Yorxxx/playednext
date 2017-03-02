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
    @DrawableRes
    int toggle;

    @EpoxyAttribute
    View.OnClickListener toggleClickListener;

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
        holder.toggle.setImageResource(toggle);
        holder.toggle.setColorFilter(color);
        holder.toggle.setOnClickListener(toggleClickListener);
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

        @BindView(R.id.toggle)
        ImageView toggle;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
