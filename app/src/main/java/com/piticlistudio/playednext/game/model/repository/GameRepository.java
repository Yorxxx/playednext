package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Repository for Game entities.
 * Unlike other repositories, and due to limitation on repository datasource implementation, any requests tries to retrieve all
 * associated data of the entity (ie companies, genres...) in case is not loaded.
 * Created by jorge.garcia on 10/02/2017.
 */

public class GameRepository implements IGameRepository {

    private final IGamedataRepository repository;
    private final GameMapper mapper;

    @Inject
    public GameRepository(IGamedataRepository repository, GameMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Loads the game with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the game loaded
     */
    @Override
    public Observable<Game> load(int id) {
        return repository.load(id)
                .map(data -> {
                    Optional<Game> result = mapper.transform(data);
                    if (!result.isPresent())
                        throw new RuntimeException("Cannot map");
                    return result.get();
                });
    }

    /**
     * Searches game that matches the specified query name
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    @Override
    public Observable<List<Game>> search(String query, int offset, int limit) {
        return repository.search(query, offset, limit)
                .flatMap(sources -> {
                    return Observable.fromIterable(sources)
                            .map(mapper::transform)
                            .onErrorReturn(throwable -> Optional.absent())
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList().toObservable();
                });
    }
}
