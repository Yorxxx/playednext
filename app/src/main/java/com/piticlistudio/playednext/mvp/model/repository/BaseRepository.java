package com.piticlistudio.playednext.mvp.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRepositoryDataSource;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;

/**
 * Base repository
 * Created by jorge.garcia on 13/02/2017.
 */

public class BaseRepository<M, T> implements IBaseRepository<M> {

    private final BaseRepositoryDataSource<T> dbImpl;
    private final BaseRepositoryDataSource<T> netImpl;
    private final Mapper<M, T> mapper;

    @Inject
    public BaseRepository(@Named("db") BaseRepositoryDataSource<T> localImpl,
                          @Named("net") BaseRepositoryDataSource<T> netImpl,
                          Mapper<M, T> mapper) {
        this.dbImpl = localImpl;
        this.netImpl = netImpl;
        this.mapper = mapper;
    }

    /**
     * +
     * Loads the model with the specified id
     *
     * @param id the id to load
     * @return an Observable that emits the loaded model.
     */
    @Override
    public Observable<M> load(int id) {
        return dbImpl.load(id)
                .map(data -> {
                    Optional<M> result = mapper.transform(data);
                    if (!result.isPresent())
                        throw new RuntimeException("Cannot map");
                    return result.get();
                })
                .onErrorResumeNext(__ -> {
                    return netImpl.load(id)
                            .map(data -> {
                                Optional<M> result = mapper.transform(data);
                                if (!result.isPresent())
                                    throw new RuntimeException("Cannot map");
                                return result.get();
                            });
                })
                .toObservable();
    }
}
