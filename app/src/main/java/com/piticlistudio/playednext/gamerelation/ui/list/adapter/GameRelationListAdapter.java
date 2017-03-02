package com.piticlistudio.playednext.gamerelation.ui.list.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.CompletedItemModel_;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel_;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter that displays a list of gamerelations
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListAdapter extends EpoxyAdapter {

    private final Picasso picasso;
    private final Context ctx;
    private HeaderModel completedHeaderModel;
    private HeaderModel pendingHeaderModel;
    private HeaderModel currentHeaderModel;

    private List<GameRelation> completedItems = new ArrayList<>();
    private List<GameRelation> currentItems = new ArrayList<>();
    private List<GameRelation> todoItems = new ArrayList<>();

    @Inject
    public GameRelationListAdapter(Picasso picasso, Context ctx) {
        this.picasso = picasso;
        this.ctx = ctx;

        completedHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_done))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_completed_color))
                .icon(R.drawable.gamerelation_completed_status);
        currentHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_playing))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_current_color))
                .icon(R.drawable.gamerelation_playing_status);
        pendingHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_pending))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_pending_color))
                .icon(R.drawable.gamerelation_waiting_status);

        addModels(completedHeaderModel, currentHeaderModel, pendingHeaderModel);
    }

    public void setData(List<GameRelation> completedItems, List<GameRelation> currentItems, List<GameRelation> todoItems) {

        bindCompletedItems(completedItems);
    }

    private void bindCompletedItems(List<GameRelation> newItems) {
        if (this.completedItems == null || this.completedItems.isEmpty()) {
            for (GameRelation completedItem : newItems) {
                EpoxyModel viewModel = initModel(completedItem);
                if (viewModel != null) {
                    insertModelAfter(viewModel, completedHeaderModel);
                }
            }
        } else {
            int index = 0;
            for (int i = 1; i < this.models.size(); i++) { // First one is always the header
                if (this.models.get(i) instanceof CompletedItemModel_) {
                    CompletedItemModel_ model = ((CompletedItemModel_) models.get(i));
                    if (index >= newItems.size()) {
                        removeModel(model);
                    } else {
                        GameRelation item = newItems.get(index);
                        model.title(item.game().title());
                        model.imageURL(item.game().getThumbCoverUrl());
                    }
                    notifyModelChanged(model);
                    index++;
                }
            }
            if (index < newItems.size()) {
                for (int i = index; i < newItems.size(); i++) {
                    EpoxyModel viewModel = initModel(newItems.get(i));
                    if (viewModel != null) {
                        insertModelBefore(viewModel, currentHeaderModel);
                    }
                }
            }
        }
        this.completedItems = newItems;
    }

    @Nullable
    private EpoxyModel initModel(GameRelation item) {
        if (!item.getCurrent().isPresent())
            return null;
        switch (item.getCurrent().get().type()) {
            case DONE:
                return new CompletedItemModel_()
                        .title(item.game().title())
                        .subtitle(item.getCurrent().get().getDisplayDate(ctx, Calendar.getInstance(), is24hFormat()))
                        .imageURL(item.game().getThumbCoverUrl())
                        .imageloader(picasso);
//            case DOING:
//                return new CurrentModel_()
//                        .title(item.getGame().title())
//                        .subtitle("Playing since 01/12/16")
//                        .imageURL(item.getGame().getThumbCoverUrl())
//                        .imageloader(picasso);
//            case TOBEDONE:
//                return new PendingModel_()
//                        .title(item.getGame().title())
//                        .subtitle("Added on 14/09/16")
//                        .imageURL(item.getGame().getThumbCoverUrl())
//                        .imageloader(picasso)
//                        .boostValue((int)item.getGame().getRating());
        }
        return null;
    }

    private boolean is24hFormat() {
        return android.text.format.DateFormat.is24HourFormat(ctx);
    }
}
