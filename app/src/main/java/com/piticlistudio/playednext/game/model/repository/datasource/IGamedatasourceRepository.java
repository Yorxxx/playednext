package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRepositoryDataSource;

import java.util.List;

import io.reactivex.Single;

public interface IGamedatasourceRepository extends BaseRepositoryDataSource<IGameDatasource> {

    /**
     * Searches game that matches the specified query name
     *
     * @param query  the name to query.
     * @param offset the offset of the query
     * @param limit  the max amount of items to return
     * @return a list of games matching the query.
     */
    Single<List<IGameDatasource>> search(String query, int offset, int limit);
}
