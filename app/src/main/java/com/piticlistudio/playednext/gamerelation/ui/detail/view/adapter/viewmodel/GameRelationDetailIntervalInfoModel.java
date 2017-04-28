package com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel;

import android.support.annotation.DrawableRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a brief information about a RelationInterval
  */

public class GameRelationDetailIntervalInfoModel extends EpoxyModelWithHolder<GameRelationDetailIntervalInfoModel.Holder> {

    @EpoxyAttribute
    String description;

    @EpoxyAttribute
    @DrawableRes
    int icon;

    public GameRelationDetailIntervalInfoModel(RelationInterval.RelationType type) {
        super((long)type.ordinal());
    }

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.gamerelation_detail_info;
    }

    @Override
    public void bind(Holder holder) {
        holder.status.setImageResource(icon);
        if (description != null) {
            final SpannableString spannableString = new SpannableString(description);
            int start = -1;
            int end = -1;
            for (int i = 0; i < description.length(); i++) {
                char c = description.charAt(i);
                if ((c >= '0' && c <= '9')) {
                    if (start == -1) {
                        start = i;
                    } else {
                        end = i;
                    }
                }
            }
            if (end == -1)
                end = start;
            end++;

            spannableString.setSpan(new RelativeSizeSpan(2.0f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.text.setText(spannableString, TextView.BufferType.SPANNABLE);
        }
    }

    static class Holder extends EpoxyHolder {

        @BindView(R.id.text)
        TextView text;


        @BindView(R.id.status)
        ImageView status;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
