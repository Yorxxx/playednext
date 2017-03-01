package com.piticlistudio.playednext.gamerelation.model.repository.datasource;

import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRepositoryDataSource;

import java.util.List;

import io.reactivex.Observable;


public interface IGameRelationRepositoryDatasource<T> extends BaseRepositoryDataSource<T> {

    /**
     * Loads all items
     *
     * @return an Observable that emits all items available
     */
    Observable<List<IGameRelationDatasource>> loadAll();
}
