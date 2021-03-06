package com.piticlistudio.playednext.company.model.repository.datasource;

import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Repository for RealmCompany entities
 * Created by jorge.garcia on 13/02/2017.
 */

public class RealmCompanyRepositoryImpl extends BaseRealmRepository<RealmCompany> implements ICompanyRepositoryDataSource {

    public RealmCompanyRepositoryImpl() {
        super(RealmCompany.class);
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<ICompanyData> load(int id) {
        return super.find(id)
                .map(realmCompany -> realmCompany);
    }

    /**
     * Saves the data
     *
     * @param data the data to save
     * @return an Observable that emits the saved data
     */
    @Override
    public Completable save(ICompanyData data) {
        return super.store((RealmCompany) data);
    }
}
