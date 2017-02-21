package com.piticlistudio.playednext.game.ui.search.view.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * View model for game search
 * Created by jorge.garcia on 13/12/2016.
 */

public class GameSearchModel extends EpoxyModelWithHolder<GameSearchModel.GameSearchHolder> {

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    Picasso imageloader;

    @EpoxyAttribute
    String imageURL;

    @EpoxyAttribute
    View.OnClickListener clickListener;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected GameSearchHolder createNewHolder() {
        return new GameSearchHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.game_search_adapter_cell;
    }

    @Override
    public void bind(GameSearchHolder holder) {
        holder.text.setText(title);
        imageloader.load(imageURL)
                .fit()
                .centerInside()
                .into(holder.image);
        holder.image.setOnClickListener(clickListener);
    }

    static class GameSearchHolder extends EpoxyHolder {

        @BindView(R.id.title)
        TextView text;

        @BindView(R.id.image)
        ImageView image;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
