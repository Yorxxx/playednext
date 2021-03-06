package com.piticlistudio.playednext.platform.ui.grid.adapter;

import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlatformLabelViewModel extends EpoxyModelWithHolder<PlatformLabelViewModel.Holder> {

    @EpoxyAttribute
    String text;

    @ColorInt
    @EpoxyAttribute
    int background;

    @ColorInt
    @EpoxyAttribute
    int textColor;

    @EpoxyAttribute(hash = false)
    View.OnClickListener clickListener;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.platform_label_row;
    }

    @Override
    public void bind(Holder holder) {
        holder.platform.setText(text);
        holder.platform.setBackgroundColor(background);
        holder.platform.setTextColor(textColor);
        holder.platform.setOnClickListener(clickListener);
    }

    static class Holder extends EpoxyHolder {

        @BindView(R.id.platform)
        TextView platform;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
