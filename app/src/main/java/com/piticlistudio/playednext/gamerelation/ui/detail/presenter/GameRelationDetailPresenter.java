package com.piticlistudio.playednext.gamerelation.ui.detail.presenter;

import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.mvp.ui.MvpPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Presenter implementation
 * Created by jorge.garcia on 27/02/2017.
 */

public class GameRelationDetailPresenter extends MvpPresenter<GameRelationDetailContract.View> implements GameRelationDetailContract
        .Presenter<GameRelationDetailContract.View> {

    private final GameRelationDetailContract.Interactor interactor;
    private PublishSubject<GameRelation> saveSubject = PublishSubject.create();
    private Disposable loadDisposable;
    private Disposable saveDisposable;

    @Inject
    public GameRelationDetailPresenter(GameRelationDetailContract.Interactor interactor) {
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
        saveDisposable = saveSubject.debounce(1000, TimeUnit.MILLISECONDS)
                .flatMap(interactor::save)
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
        if (loadDisposable != null)
            loadDisposable.dispose();
        if (saveDisposable != null)
            saveDisposable.dispose();
    }

    /**
     * Loads the relation with the specified id.
     *
     * @param id the id to load.
     */
    @Override
    public void loadData(int id) {
        if (isViewAvailable() && getView() != null) {
            getView().showLoading();
            loadDisposable = interactor.load(id)
                    .onErrorResumeNext(throwable -> {
                        return interactor.create(id);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showData, this::showError);
        }
    }

    /**
     * Saves the relation.
     *
     * @param data the data to save.
     */
    @Override
    public void save(GameRelation data) {
        saveSubject.onNext(data);
    }

    private void showData(GameRelation data) {
        if (isViewAvailable() && getView() != null) {
            getView().setData(data);
            getView().showContent();
        }
    }

    private void showError(Throwable error) {
        if (isViewAvailable() && getView() != null) {
            getView().showError(error);
        }
    }
}
