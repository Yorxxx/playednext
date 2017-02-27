package com.piticlistudio.playednext.gamerelation.ui.detail.interactor;

import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Implementation
 * Created by jorge.garcia on 27/02/2017.
 */

public class GameRelationDetailInteractor implements GameRelationDetailContract.Interactor {

    private final GameRelationRepository repository;
    private final IGameRepository gameRepository;

    @Inject
    public GameRelationDetailInteractor(GameRelationRepository repository, IGameRepository gameRepository) {
        this.repository = repository;
        this.gameRepository = gameRepository;
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
    public Observable<GameRelation> save(GameRelation data) {
        return repository.save(data);
    }
}
