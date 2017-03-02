package com.piticlistudio.playednext.gamerelation.ui.list.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.CompletedItemModel_;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.CurrentItemModel_;
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
    private final Resources res;
    private HeaderModel completedHeaderModel;
    private HeaderModel pendingHeaderModel;
    private HeaderModel currentHeaderModel;

    private List<GameRelation> completedItems = new ArrayList<>();
    private List<GameRelation> currentItems = new ArrayList<>();
    private List<GameRelation> todoItems = new ArrayList<>();

    private boolean isDisplayingCompletedItems = true;
    private boolean isDisplayingCurrentItems = true;

    @Inject
    public GameRelationListAdapter(Picasso picasso, Context ctx) {
        this.picasso = picasso;
        this.ctx = ctx;
        res = ctx.getResources();


        completedHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_done))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_completed_color))
                .icon(R.drawable.gamerelation_completed_status)
                .toggle(R.drawable.gamerelation_list_header_show)
                .toggleClickListener(view -> onToggleCompletedItemsVisibility(isDisplayingCompletedItems))
                .subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, completedItems.size(), completedItems.size()));
        currentHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_playing))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_current_color))
                .icon(R.drawable.gamerelation_playing_status)
                .toggle(R.drawable.gamerelation_list_header_show)
                .toggleClickListener(view -> onToggleCurrentItemsVisibility(isDisplayingCurrentItems))
                .subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, currentItems.size(), currentItems.size()));
        pendingHeaderModel = new HeaderModel_()
                .title(ctx.getString(R.string.gamerelation_list_header_title_pending))
                .color(ContextCompat.getColor(ctx, R.color.gamerelation_pending_color))
                .icon(R.drawable.gamerelation_waiting_status)
                .toggle(R.drawable.gamerelation_list_header_show);

        addModels(completedHeaderModel, currentHeaderModel, pendingHeaderModel);
    }

    /**
     * Called whenever the visibility icon on the completed list has been clicked
     *
     * @param isEnabled true if items were previously visible. False otherwise
     */
    private void onToggleCompletedItemsVisibility(boolean isEnabled) {
        List<EpoxyModel<?>> itemsToUpdate = new ArrayList<>();
        if (isEnabled) {
            ((HeaderModel_) completedHeaderModel).toggle(R.drawable.gamerelation_list_header_hide);
            notifyModelChanged(completedHeaderModel);
        } else {
            ((HeaderModel_) completedHeaderModel).toggle(R.drawable.gamerelation_list_header_show);
            notifyModelChanged(completedHeaderModel);
        }
        for (EpoxyModel<?> model : this.models) {
            if (model instanceof CompletedItemModel_) {
                itemsToUpdate.add(model);
            }
        }
        if (isEnabled)
            hideModels(itemsToUpdate);
        else
            showModels(itemsToUpdate);
        this.isDisplayingCompletedItems = !isEnabled;
    }

    /**
     * Called whenever the visibility icon on the current list has been clicked
     *
     * @param isEnabled true if items were previously visible. False otherwise
     */
    private void onToggleCurrentItemsVisibility(boolean isEnabled) {
        List<EpoxyModel<?>> itemsToUpdate = new ArrayList<>();
        if (isEnabled) {
            ((HeaderModel_) currentHeaderModel).toggle(R.drawable.gamerelation_list_header_hide);
        } else {
            ((HeaderModel_) currentHeaderModel).toggle(R.drawable.gamerelation_list_header_show);
        }
        notifyModelChanged(currentHeaderModel);
        for (EpoxyModel<?> model : this.models) {
            if (model instanceof CurrentItemModel_) {
                itemsToUpdate.add(model);
            }
        }
        if (isEnabled)
            hideModels(itemsToUpdate);
        else
            showModels(itemsToUpdate);
        this.isDisplayingCurrentItems = !isEnabled;
    }

    public void setData(List<GameRelation> completedItems, List<GameRelation> currentItems, List<GameRelation> todoItems) {

        bindCompletedItems(completedItems);
        bindCurrentItems(currentItems);
    }

    /**
     * Adds the list of items to the adapter as completed items view models.
     * @param newItems the items
     */
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

        ((HeaderModel_) completedHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, completedItems.size(), completedItems.size()));
        notifyModelChanged(completedHeaderModel);
        onToggleCompletedItemsVisibility(isDisplayingCompletedItems);
    }

    /**
     * Adds the list of items to the adapter as current items view models.
     * @param newItems the items
     */
    private void bindCurrentItems(List<GameRelation> newItems) {
        if (this.currentItems == null || this.currentItems.isEmpty()) {
            for (GameRelation currentItem : newItems) {
                EpoxyModel viewModel = initModel(currentItem);
                if (viewModel != null) {
                    insertModelAfter(viewModel, currentHeaderModel);
                }
            }
        } else {
            int index = 0;
            for (int i = 1; i < this.models.size(); i++) { // First one is always the header
                if (this.models.get(i) instanceof CurrentItemModel_) {
                    CurrentItemModel_ model = ((CurrentItemModel_) models.get(i));
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
                        insertModelBefore(viewModel, pendingHeaderModel);
                    }
                }
            }
        }
        this.currentItems = newItems;

        ((HeaderModel_) currentHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, currentItems.size(), currentItems.size()));
        notifyModelChanged(currentHeaderModel);
        onToggleCurrentItemsVisibility(isDisplayingCurrentItems);
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
            case PLAYING:
                return new CurrentItemModel_()
                        .title(item.game().title())
                        .subtitle(item.getCurrent().get().getDisplayDate(ctx, Calendar.getInstance(), is24hFormat()))
                        .imageURL(item.game().getThumbCoverUrl())
                        .imageloader(picasso);
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
