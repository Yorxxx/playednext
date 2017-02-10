package com.piticlistudio.playednext.collection.model.repository;


import com.piticlistudio.playednext.collection.model.entity.Collection;

import io.reactivex.Observable;

/**
 * Definition methods
 * Created by jorge.garcia on 22/11/2016.
 */
public interface ICollectionRepository {

    /**
     * Loads the collection with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the collection loaded
     */
    Observable<Collection> load(int id);
}
