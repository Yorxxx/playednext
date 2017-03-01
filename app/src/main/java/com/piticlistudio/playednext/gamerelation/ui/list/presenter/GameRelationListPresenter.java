package com.piticlistudio.playednext.gamerelation.ui.list.presenter;

import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.mvp.ui.MvpPresenter;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Presenter implementation
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListPresenter extends MvpPresenter<GameRelationListContract.View> implements GameRelationListContract
        .Presenter<GameRelationListContract.View> {

    private final GameRelationListContract.Interactor interactor;

    private Disposable loadDisposable;

    @Inject
    public GameRelationListPresenter(GameRelationListContract.Interactor interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the data
     */
    @Override
    public void loadData() {
        if (isViewAvailable() && getView() != null) {
            getView().showLoading();
            loadDisposable = Observable.combineLatest(interactor.loadCompletedItems(), interactor.loadCurrentItems(), interactor
                    .loadWaitingItems(), ItemsResult::new)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showData, this::showError);
        }
    }

    /**
     * Detachs the view
     *
     * @param retainInstance determines if is retaining state
     */
    @Override
    public void detachView(boolean retainInstance) {
        if (loadDisposable != null && !loadDisposable.isDisposed())
            loadDisposable.dispose();
        loadDisposable = null;
        super.detachView(retainInstance);
    }

    /**
     * Saves the relation.
     *
     * @param data    the data to save.
     * @param newType the new type to save into the relation
     */
    @Override
    public void save(GameRelation data, RelationInterval.RelationType newType) {
        if (data.getCurrent().isPresent()) {
            data.getCurrent().get().setEndAt(System.currentTimeMillis());
        }
        data.getStatuses().add(interactor.create(newType));
        data.setUpdatedAt(System.currentTimeMillis());
        interactor.save(data).subscribe();
    }

    private void showData(ItemsResult result) {
        if (isViewAvailable() && getView() != null) {
            getView().setData(result.completedItems, result.currentItems, result.waitingItems);
            getView().showContent();
        }
    }

    private void showError(Throwable error) {
        if (isViewAvailable() && getView() != null) {
            getView().showError(error);
        }
    }

    private class ItemsResult {

        List<GameRelation> completedItems;
        List<GameRelation> currentItems;
        List<GameRelation> waitingItems;

        public ItemsResult(List<GameRelation> completedItems, List<GameRelation> currentItems, List<GameRelation> waitingItems) {
            this.completedItems = completedItems;
            this.currentItems = currentItems;
            this.waitingItems = waitingItems;
        }
    }
}
