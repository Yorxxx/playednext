package com.piticlistudio.playednext.gamerelation.ui.list.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyViewHolder;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.CompletedItemModel_;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.CurrentItemModel_;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.HeaderModel_;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.models.WaitingItemModel_;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.ui.recyclerview.SwipeableAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Adapter that displays a list of gamerelations
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListAdapter extends EpoxyAdapter implements SwipeableAdapter {

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
        updateCompletedHeaderWithItemsInfo(this.completedItems);
    }

    /**
     * Binds the CompletedHeaderModel with the info provided by the items
     *
     * @param items the items related to the header
     */
    private void updateCompletedHeaderWithItemsInfo(List<GameRelation> items) {
        ((HeaderModel_) completedHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, items.size(), items.size()));
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
        updateCurrentHeaderWithItemsInfo(this.currentItems);
    }

    /**
     * Binds the CurrentHeaderModel with the info provided by the items
     *
     * @param items the items related to the header
     */
    private void updateCurrentHeaderWithItemsInfo(List<GameRelation> items) {
        ((HeaderModel_) currentHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, items.size(), items.size()));
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
                    ((WaitingItemModel_) viewModel).backgroundColor(backgroundColorForTodoItemAtIndex(i, newItems.size()));
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
                        ((WaitingItemModel_) viewModel).backgroundColor(backgroundColorForTodoItemAtIndex(i, newItems.size()));
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

    /**
     * Binds the PendingHeaderModel with the info provided by the items
     *
     * @param items the items related to the header
     */
    private void updateTodoHeaderWithItemsInfo(List<GameRelation> items) {
        ((HeaderModel_) pendingHeaderModel).subtitle(res.getQuantityString(R.plurals.gamerelation_list_header_subtitle, items.size(), items.size()));
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

    /**
     * Returns if the specified position has swipe to right enabled.
     *
     * @param position the position to check.
     * @return true if swipe is enabled. False otherwise.
     */
    @Override
    public boolean isSwipeToRightEnabled(int position) {
        if (position >= this.models.size())
            return false;
        EpoxyModel model = this.models.get(position);
        return !(model instanceof HeaderModel);
    }

    /**
     * Returns if the specified position has swipe to left enabled.
     *
     * @param position the position to check.
     * @return true if swipe is enabled. False otherwise.
     */
    @Override
    public boolean isSwipeToLeftEnabled(int position) {
        if (position >= this.models.size())
            return false;
        EpoxyModel model = this.models.get(position);
        if (model instanceof HeaderModel || model instanceof CompletedItemModel_) {
            return false;
        }
        return true;
    }

    /**
     * Indicates that the holder has been swiped
     *
     * @param viewHolder the holder.
     * @param position   the position swiped
     * @param direction  the direction of the swipe.
     */
    @Override
    public void onItemSwipe(RecyclerView.ViewHolder viewHolder, int position, int direction) {
        EpoxyModel model = ((EpoxyViewHolder) viewHolder).getModel();
        removeModel(model);
        if (model instanceof CompletedItemModel_) {
            GameRelation relation = completedItems.get(position - 1);
            completedItems.remove(relation);
            updateCompletedHeaderWithItemsInfo(completedItems);
            if (listener != null) {
                if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
                    listener.onGameRelationChanged(relation, RelationInterval.RelationType.DONE, RelationInterval.RelationType.NONE);
                }
            }
        } else if (model instanceof CurrentItemModel_) {
            GameRelation relation = currentItems.get(position - completedItems.size() - 2);
            currentItems.remove(relation);
            updateCurrentHeaderWithItemsInfo(currentItems);
            if (listener != null) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.START) {
                    listener.onGameRelationChanged(relation, RelationInterval.RelationType.PLAYING, RelationInterval.RelationType.DONE);
                }
                else if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
                    listener.onGameRelationChanged(relation, RelationInterval.RelationType.PLAYING, RelationInterval.RelationType.NONE);
                }
            }

        } else if (model instanceof WaitingItemModel_) {
            GameRelation relation = todoItems.get(position - completedItems.size() - currentItems.size() - 3);
            todoItems.remove(relation);
            updateTodoHeaderWithItemsInfo(todoItems);
            if (listener != null) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.START) {
                    listener.onGameRelationChanged(relation, RelationInterval.RelationType.PENDING, RelationInterval.RelationType.PLAYING);
                }
                else if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
                    listener.onGameRelationChanged(relation, RelationInterval.RelationType.PENDING, RelationInterval.RelationType.NONE);
                }
            }
        }
    }

    @Override
    public View getSwipeView(RecyclerView.ViewHolder viewHolder) {
        return ((EpoxyViewHolder) viewHolder).itemView.findViewById(R.id.overlay);
    }

    public interface GameRelationAdapterListener {

        /**
         * Callback whenever a relation has been clicked
         *
         * @param clickedRelation the relation clicked
         */
        void onGameRelationClicked(GameRelation clickedRelation);

        /**
         * Callback when a change on the current relation type has been requested
         *
         * @param relation    the relation requested to update its type
         * @param currentType the current type of the relation
         * @param newType     the new requested type
         */
        void onGameRelationChanged(GameRelation relation, RelationInterval.RelationType currentType, RelationInterval.RelationType newType);
    }
}
