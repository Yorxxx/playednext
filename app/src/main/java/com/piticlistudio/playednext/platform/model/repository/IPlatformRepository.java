package com.piticlistudio.playednext.platform.model.repository;


import com.piticlistudio.playednext.platform.model.entity.Platform;

import io.reactivex.Observable;

/**
 * Definition methods
 * Created by jorge.garcia on 15/02/2017.
 */
public interface IPlatformRepository {

    /**
     * Loads the platform with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the platform loaded
     */
    Observable<Platform> load(int id);
}
