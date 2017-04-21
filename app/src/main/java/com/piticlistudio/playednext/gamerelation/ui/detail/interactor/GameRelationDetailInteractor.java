package com.piticlistudio.playednext.gamerelation.ui.detail.interactor;

import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Implementation
 * Created by jorge.garcia on 27/02/2017.
 */

public class GameRelationDetailInteractor implements GameRelationDetailContract.Interactor {

    private final IGameRelationRepository repository;
    private final IGameRepository gameRepository;
    private final RelationIntervalRepository intervalRepository;

    @Inject
    public GameRelationDetailInteractor(IGameRelationRepository repository, IGameRepository gameRepository, RelationIntervalRepository intervalRepository) {
        this.repository = repository;
        this.gameRepository = gameRepository;
        this.intervalRepository = intervalRepository;
    }

    /**
     * Loads the entity with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the entity loaded
     */
    @Override
    public Observable<GameRelation> load(int id) {
        return repository.load(id);
    }

    /**
     * Creates a new relation with the specified id.
     *
     * @param id the id to create.
     * @return an Observable that emits the created relation
     */
    @Override
    public Observable<GameRelation> create(int id) {
        return gameRepository.load(id).lastOrError()
                .map(game -> GameRelation.create(game, System.currentTimeMillis())).toObservable();
    }

    /**
     * Saves the relation.
     *
     * @param data the data to save
     * @return an Observable that returns the saved data
     */
    @Override
    public Completable save(GameRelation data) {
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
}
