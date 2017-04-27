package com.piticlistudio.playednext.gamerelation.ui.detail.view.adapter.viewmodel;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.piticlistudio.playednext.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter view model for displaying game detail
 * Created by jorge.garcia on 16/02/2017.
 */

public class GameDetailInfoModel extends EpoxyModelWithHolder<GameDetailInfoModel.Holder> {

    @EpoxyAttribute
    String developer;

    @EpoxyAttribute
    String publisher;

    @EpoxyAttribute
    String genre;

    @EpoxyAttribute
    String saga;

    @EpoxyAttribute
    boolean showDeveloper;

    @EpoxyAttribute
    boolean showPublisher;

    @EpoxyAttribute
    boolean showGenre;

    @EpoxyAttribute
    boolean showSaga;

    /**
     * This should return a new instance of your {@link EpoxyHolder} class.
     */
    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.game_detail_info;
    }

    /**
     * Subclasses can override this if they want their view to take up more than one span in a grid
     * layout.
     *
     * @param totalSpanCount The number of spans in the grid
     * @param position       The position of the model
     * @param itemCount      The total number of items in the adapter
     */
    @Override
    public int getSpanSize(int totalSpanCount, int position, int itemCount) {
        return totalSpanCount;
    }

    @Override
    public void bind(Holder holder) {
        holder.developer.setText(developer);
        holder.publisher.setText(publisher);
        holder.genre.setText(genre);
        holder.saga.setText(saga);
        holder.developer_row.setVisibility(showDeveloper ? View.VISIBLE : View.GONE);
        holder.saga_row.setVisibility(showSaga ? View.VISIBLE : View.GONE);
    }

    static class Holder extends EpoxyHolder {

        @BindView(R.id.developer)
        TextView developer;

        @BindView(R.id.publisher)
        TextView publisher;

        @BindView(R.id.genre)
        TextView genre;

        @BindView(R.id.saga)
        TextView saga;

        @BindView(R.id.developer_row)
        View developer_row;

        @BindView(R.id.publisher_row)
        View publisher_row;

        @BindView(R.id.genre_row)
        View genre_row;

        @BindView(R.id.saga_row)
        View saga_row;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
