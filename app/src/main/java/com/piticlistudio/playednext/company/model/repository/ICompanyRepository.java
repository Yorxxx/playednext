package com.piticlistudio.playednext.company.model.repository;


import com.piticlistudio.playednext.company.model.entity.Company;

import io.reactivex.Observable;

/**
 * Definition methods
 * Created by jorge.garcia on 22/11/2016.
 */
public interface ICompanyRepository {

    /**
     * Loads the Company with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the collection loaded
     */
    Observable<Company> load(int id);
}
