package com.piticlistudio.playednext.gamerelation.ui.list.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.WaitingItemModel_;
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
    private boolean isDisplayingPendingItems = true;

    private GameRelationAdapterListener listener;

    @Inject
    GameRelationListAdapter(Picasso picasso, Context ctx) {
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
                .toggle(R.drawable.gamerelation_list_header_show)
                .toggleClickListener(view -> onTogglePendingItemsVisibility(isDisplayingPendingItems))
                .subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, todoItems.size(), todoItems.size()));

        addModels(completedHeaderModel, currentHeaderModel, pendingHeaderModel);
    }

    public void setListener(GameRelationAdapterListener listener) {
        this.listener = listener;
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

    /**
     * Called whenever the visibility icon on the pending list has been clicked
     *
     * @param isEnabled true if items were previously visible. False otherwise
     */
    private void onTogglePendingItemsVisibility(boolean isEnabled) {
        List<EpoxyModel<?>> itemsToUpdate = new ArrayList<>();
        if (isEnabled) {
            ((HeaderModel_) pendingHeaderModel).toggle(R.drawable.gamerelation_list_header_hide);
            notifyModelChanged(pendingHeaderModel);
        } else {
            ((HeaderModel_) pendingHeaderModel).toggle(R.drawable.gamerelation_list_header_show);
            notifyModelChanged(pendingHeaderModel);
        }
        for (EpoxyModel<?> model : this.models) {
            if (model instanceof WaitingItemModel_) {
                itemsToUpdate.add(model);
            }
        }
        if (isEnabled)
            hideModels(itemsToUpdate);
        else
            showModels(itemsToUpdate);
        this.isDisplayingPendingItems = !isEnabled;
    }

    public void setData(List<GameRelation> completedItems, List<GameRelation> currentItems, List<GameRelation> todoItems) {

        bindCompletedItems(completedItems);
        bindCurrentItems(currentItems);
        bindWaitingItems(todoItems);
    }

    /**
     * Adds the list of items to the adapter as completed items view models.
     *
     * @param newItems the items
     */
    private void bindCompletedItems(List<GameRelation> newItems) {
        if (this.completedItems == null || this.completedItems.isEmpty()) {
            for (GameRelation completedItem : newItems) {
                EpoxyModel viewModel = initModel(completedItem);
                if (viewModel != null) {
                    insertModelBefore(viewModel, currentHeaderModel);
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
                        model.clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
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
        onToggleCompletedItemsVisibility(!isDisplayingCompletedItems);
    }

    /**
     * Adds the list of items to the adapter as current items view models.
     *
     * @param newItems the items
     */
    private void bindCurrentItems(List<GameRelation> newItems) {
        if (this.currentItems == null || this.currentItems.isEmpty()) {
            for (GameRelation currentItem : newItems) {
                EpoxyModel viewModel = initModel(currentItem);
                if (viewModel != null) {
                    insertModelBefore(viewModel, pendingHeaderModel);
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
                        model.clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
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
        onToggleCurrentItemsVisibility(!isDisplayingCurrentItems);
    }

    /**
     * Adds the list of items to the adapter as waiting items view models.
     *
     * @param newItems the items
     */
    private void bindWaitingItems(List<GameRelation> newItems) {
        if (this.todoItems == null || this.todoItems.isEmpty()) {
            for (int i = 0; i < newItems.size(); i++) {
                EpoxyModel viewModel = initModel(newItems.get(i));
                if (viewModel != null) {
                    ((WaitingItemModel_)viewModel).backgroundColor(backgroundColorForTodoItemAtIndex(i, newItems.size()));
                    addModel(viewModel);
                }
            }
        } else {
            int index = 0;
            for (int i = 1; i < this.models.size(); i++) { // First one is always the header
                if (this.models.get(i) instanceof WaitingItemModel_) {
                    WaitingItemModel_ model = ((WaitingItemModel_) models.get(i));
                    if (index >= newItems.size()) {
                        removeModel(model);
                    } else {
                        GameRelation item = newItems.get(index);
                        model.title(item.game().title());
                        model.imageURL(item.game().getThumbCoverUrl());
                        model.backgroundColor(backgroundColorForTodoItemAtIndex(index, newItems.size()));
                        model.clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
                    }
                    notifyModelChanged(model);
                    index++;
                }
            }
            if (index < newItems.size()) {
                for (int i = index; i < newItems.size(); i++) {
                    EpoxyModel viewModel = initModel(newItems.get(i));
                    if (viewModel != null) {
                        ((WaitingItemModel_)viewModel).backgroundColor(backgroundColorForTodoItemAtIndex(i, newItems.size()));
                        addModel(viewModel);
                    }
                }
            }
        }
        this.todoItems = newItems;

        ((HeaderModel_) pendingHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, todoItems.size(),
                todoItems.size()));
        notifyModelChanged(pendingHeaderModel);
        onTogglePendingItemsVisibility(!isDisplayingPendingItems);
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
                        .imageloader(picasso)
                        .clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
            case PLAYING:
                return new CurrentItemModel_()
                        .title(item.game().title())
                        .subtitle(item.getCurrent().get().getDisplayDate(ctx, Calendar.getInstance(), is24hFormat()))
                        .imageURL(item.game().getThumbCoverUrl())
                        .imageloader(picasso)
                        .clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
            case PENDING:
                return new WaitingItemModel_()
                        .title(item.game().title())
                        .subtitle(item.getCurrent().get().getDisplayDate(ctx, Calendar.getInstance(), is24hFormat()))
                        .imageURL(item.game().getThumbCoverUrl())
                        .imageloader(picasso)
                        .clickListener(view -> {
                            if (listener != null)
                                listener.onGameRelationClicked(item);
                        });
        }
        return null;
    }

    private boolean is24hFormat() {
        return android.text.format.DateFormat.is24HourFormat(ctx);
    }

    private int backgroundColorForTodoItemAtIndex(int index, int total) {
        double value = ((float) index / (float) total);
        return Color.rgb(255, (int) (value * 153), 0);
    }

    public interface GameRelationAdapterListener {

        /**
         * Callback whenever a relation has been clicked
         *
         * @param clickedRelation the relation clicked
         */
        void onGameRelationClicked(GameRelation clickedRelation);
    }
}
