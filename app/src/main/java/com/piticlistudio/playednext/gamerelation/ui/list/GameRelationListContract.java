package com.piticlistudio.playednext.gamerelation.ui.list;


import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.mvp.ui.IMvpPresenter;
import com.piticlistudio.playednext.mvp.ui.MvpView;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.List;

import io.reactivex.Observable;

/**
 * Contract for the list of Game relations
 * Created by jorge.garcia on 28/02/2017.
 */

public interface GameRelationListContract {

    interface View extends MvpView {

        /**
         * Sets the data
         *
         * @param completed the list of items completed
         * @param current   the list of items being completed
         * @param waiting   the list of items waiting to be completed
         */
        void setData(List<GameRelation> completed, List<GameRelation> current, List<GameRelation> waiting);

        /**
         * Loads the data
         */
        void loadData();

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
         * Loads the data
         */
        void loadData();

        /**
         * Saves the relation.
         *
         * @param data    the data to save.
         * @param newType the new type to save into the relation
         */
        void save(GameRelation data, RelationInterval.RelationType newType);
    }

    interface Interactor {

        /**
         * Returns an Observable that emits the list of completed items
         *
         * @return an Observable
         */
        Observable<List<GameRelation>> loadCompletedItems();

        /**
         * Returns an Observable that emits the list of items being completed
         *
         * @return an Observable
         */
        Observable<List<GameRelation>> loadCurrentItems();

        /**
         * Returns an Observable that emits the list of items being on hold
         *
         * @return an Observable
         */
        Observable<List<GameRelation>> loadWaitingItems();
    }
}
