package com.piticlistudio.playednext.game.ui.detail.presenter;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.mvp.ui.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Presenter implementation
 * Created by jorge.garcia on 15/02/2017.
 */

public class GameDetailPresenter extends MvpPresenter<GameDetailContract.View> implements GameDetailContract.Presenter<GameDetailContract
        .View> {

    private final GameDetailContract.Interactor interactor;
    private Disposable gameLoadDisposable;

    @Inject
    public GameDetailPresenter(GameDetailContract.Interactor interactor) {
        this.interactor = interactor;
    }

    /**
     * Detachs the view
     *
     * @param retainInstance determines if is retaining state
     */
    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        if (gameLoadDisposable != null && !gameLoadDisposable.isDisposed()) {
            gameLoadDisposable.dispose();
        }
    }

    /**
     * Loads the data
     *
     * @param gameId the id of the game to load
     */
    public void loadData(int gameId) {
        if (isViewAvailable() && getView() != null) {
            getView().showLoading();
            if (gameLoadDisposable != null && !gameLoadDisposable.isDisposed()) {
                gameLoadDisposable.dispose();
            }
            gameLoadDisposable = interactor.load(gameId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::showData, this::showError);
        }
    }


    private void showData(Game data) {
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
