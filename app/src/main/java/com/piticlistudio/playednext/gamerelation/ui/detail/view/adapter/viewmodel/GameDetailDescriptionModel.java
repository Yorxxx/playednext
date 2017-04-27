package com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays a brief description introducing the game
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailDescriptionModel extends EpoxyModelWithHolder<GameDetailDescriptionModel.Holder> {

    @EpoxyAttribute
    String description;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.game_detail_description;
    }

    @Override
    public void bind(Holder holder) {
        final SpannableString spannableString = new SpannableString(description);
        int position = 0;
        for (int i = 0, ei = description.length(); i < ei; i++) {
            char c = description.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                position = i;
                break;
            }
        }
        spannableString.setSpan(new RelativeSizeSpan(3.0f), position, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.text.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    static class Holder extends EpoxyHolder {

        @BindView(R.id.text)
        TextView text;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
