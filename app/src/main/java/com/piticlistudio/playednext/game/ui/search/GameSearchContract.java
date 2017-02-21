package com.piticlistudio.playednext.game.ui.search;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.mvp.ui.IMvpPresenter;
import com.piticlistudio.playednext.mvp.ui.MvpView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract for the view delimiting the search of a game
 * Created by jorge.garcia on 21/02/2017.
 */

public interface GameSearchContract {

    interface View extends MvpView {

        /**
         * Shows the loading status.
         */
        void showLoading();

        /**
         * Shows the main content of the view
         */
        void showContent();

        /**
         * Sets the data to show.
         *
         * @param data the data to show
         */
        void setData(List<Game> data);

        /**
         * Shows an error
         *
         * @param error the error to show.
         */
        void showError(Throwable error);

        /**
         * Searches games with the specified query match
         *
         * @param query  the name of the game to search
         * @param offset the number of items to skip
         * @param limit  the max amount items to return
         */
        void search(String query, int offset, int limit);
    }

    interface Presenter<V extends MvpView> extends IMvpPresenter<V> {

        /**
         * Searches games with the specified query match
         *
         * @param query  the name of the game to search
         * @param offset the number of items to skip
         * @param limit  the max amount items to return
         */
        void search(String query, int offset, int limit);
    }

    interface Interactor {

        /**
         * Searches games with the specified query match
         *
         * @param query  the name of the game to search
         * @param offset the number of items to skip
         * @param limit  the max amount items to return
         * @return an Observable that emits the list of games loaded
         */
        Observable<List<Game>> search(String query, int offset, int limit);
    }
}
