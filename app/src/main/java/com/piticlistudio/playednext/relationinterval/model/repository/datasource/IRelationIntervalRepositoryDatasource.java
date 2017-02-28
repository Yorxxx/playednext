package com.piticlistudio.playednext.relationinterval.model.repository.datasource;

import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRepositoryDataSource;

public interface IRelationIntervalRepositoryDatasource<T> extends BaseRepositoryDataSource<T> {

    /**
     * Returns a new id to be used for a new relation
     *
     * @return a new Id
     */
    int getAutoincrementId();
}
