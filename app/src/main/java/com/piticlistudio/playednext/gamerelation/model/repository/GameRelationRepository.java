package com.piticlistudio.playednext.gamerelation.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.RealmGameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.datasource.IGameRelationRepositoryDatasource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Repository implementation
 * Created by jorge.garcia on 27/02/2017.
 */

public class GameRelationRepository implements IGameRelationRepository {

    private final IGameRelationRepositoryDatasource<IGameRelationDatasource> dbImpl;
    private final GameRelationMapper mapper;
    private final RealmGameRelationMapper realmMapper;

    @Inject
    public GameRelationRepository(IGameRelationRepositoryDatasource<IGameRelationDatasource> dbImpl,
                                  GameRelationMapper mapper, RealmGameRelationMapper realmMapper) {
        this.dbImpl = dbImpl;
        this.mapper = mapper;
        this.realmMapper = realmMapper;
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return the saved data.
     */
    @Override
    public Completable save(GameRelation data) {
        Optional<RealmGameRelation> result = realmMapper.transform(data);
        if (!result.isPresent())
            return Completable.error(new Exception("Could not map " + data + " into Realm entity"));
        return dbImpl.save(result.get());
    }

    /**
     * Loads the relation with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the relation loaded
     */
    @Override
    public Observable<GameRelation> load(int id) {
        return dbImpl.load(id)
                .map(data -> {
                    Optional<GameRelation> result = mapper.transform(data);
                    if (!result.isPresent())
                        throw new RuntimeException("Cannot map");
                    return result.get();
                }).toObservable();
    }

    /**
     * Loads all items from the repository
     *
     * @return an Observable that emits all items
     */
    @Override
    public Observable<List<GameRelation>> loadAll() {
        return dbImpl.loadAll()
                .flatMap(data -> Observable.fromIterable(data)
                        .map(mapper::transform)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList()
                        .toObservable());
    }
}
