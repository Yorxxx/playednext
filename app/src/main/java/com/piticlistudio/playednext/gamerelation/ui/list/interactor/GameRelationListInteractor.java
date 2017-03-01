package com.piticlistudio.playednext.gamerelation.ui.list.interactor;


import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interactor implementation for GameRelationListContract
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListInteractor implements GameRelationListContract.Interactor {

    private final GameRelationRepository repository;

    @Inject
    public GameRelationListInteractor(GameRelationRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns an Observable that emits the list of completed items
     *
     * @return an Observable
     */
    @Override
    public Observable<List<GameRelation>> loadCompletedItems() {
        return loadFilteredByType(RelationInterval.RelationType.DONE);
    }

    /**
     * Returns an Observable that emits the list of items being completed
     *
     * @return an Observable
     */
    @Override
    public Observable<List<GameRelation>> loadCurrentItems() {
        return loadFilteredByType(RelationInterval.RelationType.PLAYING);
    }

    /**
     * Returns an Observable that emits the list of items being on hold
     *
     * @return an Observable
     */
    @Override
    public Observable<List<GameRelation>> loadWaitingItems() {
        return loadFilteredByType(RelationInterval.RelationType.PENDING);
    }

    private Observable<List<GameRelation>> loadFilteredByType(RelationInterval.RelationType type) {
        return repository.loadAll()
                .flatMap(gameRelations -> Observable.fromIterable(gameRelations)
                        .filter(gameRelation -> gameRelation.getCurrent().isPresent())
                        .filter(gameRelation -> gameRelation.getCurrent().get().type() == type)
                        .toList().toObservable());
    }
}
