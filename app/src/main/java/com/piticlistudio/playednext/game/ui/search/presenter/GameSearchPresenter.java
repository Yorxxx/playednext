package com.piticlistudio.playednext.game.ui.search.presenter;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;
import com.piticlistudio.playednext.mvp.ui.MvpPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Presenter implementation
 * Created by jorge.garcia on 21/02/2017.
 */

public class GameSearchPresenter extends MvpPresenter<GameSearchContract.View> implements GameSearchContract.Presenter<GameSearchContract
        .View> {

    private final GameSearchContract.Interactor interactor;
    private PublishSubject<SearchQuery> searchSubject = PublishSubject.create();
    private Disposable searchDisposable;

    @Inject
    GameSearchPresenter(GameSearchContract.Interactor interactor) {
        this.interactor = interactor;
    }

    /**
     * Attachs the view
     *
     * @param view the view to attach
     */
    @Override
    public void attachView(GameSearchContract.View view) {
        super.attachView(view);
        searchDisposable = searchSubject
                .debounce(250, TimeUnit.MILLISECONDS)
                .switchMap(s -> interactor.search(s.query, s.offset, s.limit))
                .subscribeOn(Schedulers.io())
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
        searchDisposable.dispose();
    }

    /**
     * Searches games with the specified query match
     *
     * @param query  the name of the game to search
     * @param offset the number of items to skip
     * @param limit  the max amount items to return
     */
    @Override
    public void search(String query, int offset, int limit) {
        if (isViewAvailable() && getView() != null) {
            getView().showLoading();
            searchSubject.onNext(new SearchQuery(query, offset, limit));
        }
    }

    private void showError(Throwable error) {
        if (isViewAvailable() && getView() != null) {
            getView().showError(error);
        }
    }

    private void showData(List<Game> data) {
        if (isViewAvailable() && getView() != null) {
            getView().setData(data);
            getView().showContent();
        }
    }

    class SearchQuery {

        private String query;
        private int offset;
        private int limit;

        public SearchQuery(String query, int offset, int limit) {
            this.query = query;
            this.offset = offset;
            this.limit = limit;
        }
    }
}
