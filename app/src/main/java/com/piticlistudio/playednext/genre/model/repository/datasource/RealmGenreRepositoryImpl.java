package com.piticlistudio.playednext.genre.model.repository.datasource;

import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Implementation of IGenreRepositoryDatasource on Realm
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmGenreRepositoryImpl extends BaseRealmRepository<RealmGenre> implements IGenreRepositoryDatasource {

    public RealmGenreRepositoryImpl() {
        super(RealmGenre.class);
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<IGenreData> load(int id) {
        return super.find(id)
                .map(data -> data);
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Completable save(IGenreData data) {
        return super.store((RealmGenre) data);
    }
}
