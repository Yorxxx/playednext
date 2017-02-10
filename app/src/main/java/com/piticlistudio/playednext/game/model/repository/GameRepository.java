package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

/**
 * Repository for Game entities.
 * Unlike other repositories, and due to limitation on repository datasource implementation, any requests tries to retrieve all
 * associated data of the entity (ie companies, genres...) in case is not loaded.
 * Created by jorge.garcia on 10/02/2017.
 */

public class GameRepository implements IGameRepository {

    private final IGamedataRepository repository;
    private final GameMapper mapper;
    private final ICollectionRepository collectionRepository;

    private final PublishSubject<Game> publishSubject = PublishSubject.create();

    @Inject
    public GameRepository(IGamedataRepository repository, GameMapper mapper, ICollectionRepository collectionRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.collectionRepository = collectionRepository;
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
                .flatMap(new Function<IGameDatasource, ObservableSource<Game>>() {
                    @Override
                    public ObservableSource<Game> apply(IGameDatasource iGameDatasource) throws Exception {
                        return mapSource(iGameDatasource)
                                .flatMap(new Function<Game, ObservableSource<Game>>() {
                                    @Override
                                    public ObservableSource<Game> apply(Game game) throws Exception {
                                        return Observable.merge(Observable.just(game), loadCollection(iGameDatasource, game));
                                    }
                                });
                    }
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
                .flatMap(sources -> Observable.fromIterable(sources)
                        .map(mapper::transform)
                        .onErrorReturn(throwable -> Optional.absent())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList().toObservable());
    }

    private Observable<Game> mapSource(IGameDatasource iGameDatasource) {
        Optional<Game> result = mapper.transform(iGameDatasource);
        if (!result.isPresent())
            return Observable.error(new Exception("Cannot map"));
        return Observable.just(result.get());
    }

    /**
     * Loads the collection from the supplied source
     * By checking on the source data, requests the collection associated and updates the requested
     * Game.
     * If game has collection already loaded or source has no data to load, nothing will be emitted.
     * @param from the source containing the collection info
     * @param to the entity in which to set the data
     * @return an Observable that emits the request with the collection on the source, loaded.
     */
    protected Observable<Game> loadCollection(IGameDatasource from, Game to) {
        if (to.collection.isPresent()) {
            return Observable.empty();
        }
        if (!from.getCollection().isPresent()) {
            return Observable.empty();
        }
        return collectionRepository.load(from.getCollection().get().id)
                .map(collection -> {
                    to.collection = Optional.fromNullable(collection);
                    return to;
                });
    }
}
