package com.piticlistudio.playednext.gamerelation.ui.detail;

import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.mvp.ui.IMvpPresenter;
import com.piticlistudio.playednext.mvp.ui.MvpView;

import io.reactivex.Observable;

public interface GameRelationDetailContract {

    interface View extends MvpView {

        /**
         * Sets the data
         *
         * @param data the data to set
         */
        void setData(GameRelation data);

        /**
         * Loads the data
         *
         * @param id the id of the relation to load.
         */
        void loadData(int id);

        /**
         * Shows a loading view
         */
        void showLoading();

        /**
         * Shows the main content
         */
        void showContent();

        /**
         * Shows the error
         *
         * @param error the error to show
         */
        void showError(Throwable error);
    }

    interface Presenter<V extends MvpView> extends IMvpPresenter<V> {

        /**
         * Loads the relation with the specified id.
         *
         * @param id the id to load.
         */
        void loadData(int id);

        /**
         * Saves the relation.
         *
         * @param data the data to save.
         */
        void save(GameRelation data);
    }

    interface Interactor {

        /**
         * Loads the entity with the specified id.
         *
         * @param id the id to load.
         * @return an Observable that emits the entity loaded
         */
        Observable<GameRelation> load(int id);

        /**
         * Creates a new relation with the specified id.
         *
         * @param id the id to create.
         * @return an Observable that emits the created relation
         */
        Observable<GameRelation> create(int id);

        /**
         * Saves the relation.
         *
         * @param data the data to save
         * @return an Observable that returns the saved data
         */
        Observable<GameRelation> save(GameRelation data);
    }
}
