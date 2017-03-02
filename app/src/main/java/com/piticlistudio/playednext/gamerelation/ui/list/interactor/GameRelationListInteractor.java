package com.piticlistudio.playednext.gamerelation.ui.list.interactor;


import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Interactor implementation for GameRelationListContract
 * Created by jorge.garcia on 01/03/2017.
 */

public class GameRelationListInteractor implements GameRelationListContract.Interactor {

    private final IGameRelationRepository repository;
    private final RelationIntervalRepository intervalRepository;

    @Inject
    public GameRelationListInteractor(IGameRelationRepository repository, RelationIntervalRepository intervalRepository) {
        this.repository = repository;
        this.intervalRepository = intervalRepository;
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

    /**
     * Saves the relation.
     *
     * @param data the data to save
     * @return an Observable that returns the saved data
     */
    @Override
    public Observable<GameRelation> save(GameRelation data) {
        return repository.save(data);
    }

    /**
     * Creates a new relationInterval
     *
     * @param type the type to create
     * @return an Interval
     */
    @Override
    public RelationInterval create(RelationInterval.RelationType type) {
        return intervalRepository.create(type);
    }

    private Observable<List<GameRelation>> loadFilteredByType(RelationInterval.RelationType type) {
        return repository.loadAll()
                .flatMap(gameRelations -> Observable.fromIterable(gameRelations)
                        .filter(gameRelation -> gameRelation.getCurrent().isPresent())
                        .filter(gameRelation -> gameRelation.getCurrent().get().type() == type)
                        .toSortedList(this::compareRelations).toObservable());
    }

    /**
     * Compares the relations
     * @param gr1 the first relation
     * @param gr2 the second relation
     * @return the comparison result
     */
    private Integer compareRelations(GameRelation gr1, GameRelation gr2) {
        if (!gr1.getCurrent().isPresent() && gr2.getCurrent().isPresent())
            return 0;
        if (!gr1.getCurrent().isPresent() && gr2.getCurrent().isPresent())
            return 1;
        if (gr1.getCurrent().isPresent() && !gr2.getCurrent().isPresent())
            return -1;
        RelationInterval interval1 = gr1.getCurrent().get();
        RelationInterval interval2 = gr2.getCurrent().get();

        if (interval1.type() != interval2.type())
            return 0;

        switch (interval1.type()) {
            case DONE:
                if (interval1.startAt() > interval2.startAt())
                    return 1;
                if (interval1.startAt() < interval2.startAt())
                    return -1;
                return 0;
        }
        return 1;
    }
}