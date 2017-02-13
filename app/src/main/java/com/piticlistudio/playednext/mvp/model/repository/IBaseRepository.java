package com.piticlistudio.playednext.mvp.model.repository;

import com.piticlistudio.playednext.company.model.entity.Company;

import io.reactivex.Observable;

public interface IBaseRepository<M> {

    /**
     * +
     * Loads the model with the specified id
     *
     * @param id the id to load
     * @return an Observable that emits the loaded model.
     */
    Observable<M> load(int id);

}
