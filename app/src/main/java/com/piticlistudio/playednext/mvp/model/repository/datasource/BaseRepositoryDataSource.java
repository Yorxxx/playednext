package com.piticlistudio.playednext.mvp.model.repository.datasource;

import io.reactivex.Single;

/**
 * Base repository for datasources
 * Created by jorge.garcia on 10/02/2017.
 */

public interface BaseRepositoryDataSource<T> {

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    Single<T> load(int id);
}
