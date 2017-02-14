package com.piticlistudio.playednext.genre.model.repository;


import com.piticlistudio.playednext.genre.model.entity.Genre;

import io.reactivex.Observable;

/**
 * Definition methods
 * Created by jorge.garcia on 22/11/2016.
 */
public interface IGenreRepository {

    /**
     * Loads the collection with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the collection loaded
     */
    Observable<Genre> load(int id);
}
