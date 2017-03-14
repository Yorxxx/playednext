package com.piticlistudio.playednext.gamerelation.ui.detail.presenter;

import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.mvp.ui.MvpPresenter;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Presenter implementation
 * Created by jorge.garcia on 27/02/2017.
 */

public class GameRelationDetailPresenter extends MvpPresenter<GameRelationDetailContract.View> implements GameRelationDetailContract
        .Presenter<GameRelationDetailContract.View> {

    private final GameRelationDetailContract.Interactor interactor;
    Disposable loadDisposable;
    Disposable saveDisposable;
    private PublishSubject<UpdateRelationEntity> saveSubject = PublishSubject.create();
    private Subject<Integer> loadSubject = PublishSubject.create();

    @Inject
    GameRelationDetailPresenter(GameRelationDetailContract.Interactor interactor) {
        this.interactor = interactor;
    }

    /**
     * Attachs the view
     *
     * @param view the view to attach
     */
    @Override
    public void attachView(GameRelationDetailContract.View view) {
        super.attachView(view);
        saveDisposable = saveSubject.debounce(250, TimeUnit.MILLISECONDS)
                .map(value -> {
                    GameRelation data = value.getData();
                    RelationInterval.RelationType type = value.getType();
                    for (RelationInterval interval : data.getStatuses()) {
                        if (interval.getEndAt() == 0)
                            interval.setEndAt(System.currentTimeMillis());
                    }
                    if (value.isActive()) {
                        data.getStatuses().add(interactor.create(type));
                    }
                    data.setUpdatedAt(System.currentTimeMillis());
                    return data;
                })
                .flatMap(interactor::save)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showData, this::showError);

        loadDisposable = loadSubject.debounce(250, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .flatMap(integer -> interactor.load(integer)
                        .onErrorResumeNext(throwable -> {
                            return interactor.create(integer);
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showData, this::showError);
    }

    /**
     * Detachs the view
     *
     * @param retainInstance determines if is retaining state
     */
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (!retainInstance) {
            if (loadDisposable != null)
                loadDisposable.dispose();
            saveDisposable.dispose();
        }
    }

    /**
     * Loads the relation with the specified id.
     *
     * @param id the id to load.
     */
    @Override
    public void loadData(int id) {
        if (getView() != null) {
            getView().showLoading();
            loadSubject.onNext(id);
        }
    }

    /**
     * Saves the relation.
     *
     * @param data   the data to save.
     * @param type   the type of the relation modified
     * @param active boolean indicating if relation type associated to the relation is active
     */
    @Override
    public void save(GameRelation data, RelationInterval.RelationType type, boolean active) {
        saveSubject.onNext(new UpdateRelationEntity(data, type, active));
    }

    private void showData(GameRelation data) {
        if (getView() != null) {
            getView().setData(data);
            getView().showContent();
        }
    }

    private void showError(Throwable error) {
        if (getView() != null) {
            getView().showError(error);
        }
    }

    private class UpdateRelationEntity {

        private final GameRelation data;
        private final RelationInterval.RelationType type;
        private final boolean active;

        UpdateRelationEntity(GameRelation data, RelationInterval.RelationType type, boolean active) {
            this.data = data;
            this.type = type;
            this.active = active;
        }

        public GameRelation getData() {
            return data;
        }

        public RelationInterval.RelationType getType() {
            return type;
        }

        public boolean isActive() {
            return active;
        }
    }
}
