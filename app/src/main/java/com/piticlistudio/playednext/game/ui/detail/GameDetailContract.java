package com.piticlistudio.playednext.game.ui.detail;

import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.mvp.ui.IMvpPresenter;
import com.piticlistudio.playednext.mvp.ui.MvpView;

import io.reactivex.Observable;

public interface GameDetailContract {

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
        void setData(Game data);

        /**
         * Shows an error
         *
         * @param error the error to show.
         */
        void showError(Throwable error);

        /**
         * Loads the data for the specified identifier
         *
         * @param gameId the id of the game to load.
         */
        void loadData(int gameId);
    }

    interface Presenter<V extends MvpView> extends IMvpPresenter<V> {

        /**
         * Loads the data
         *
         * @param id the id to load
         */
        void loadData(int id);
    }

    interface Interactor {

        /**
         * Loads the game with the specified id.
         *
         * @param id the id to load
         * @return an Observable that emits the loaded data
         */
        Observable<Game> load(int id);
    }
}
