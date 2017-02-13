package com.piticlistudio.playednext.company.model.repository.datasource;

import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.company.model.entity.datasource.NetCompany;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Repository implementation for Company entities provided by the Net module
 * Created by jorge.garcia on 10/02/2017.
 */
public class NetCompanyRepositoryImpl implements ICompanyRepositoryDataSource<NetCompany> {

    private final CompanyModule.NetService service;

    @Inject
    public NetCompanyRepositoryImpl(CompanyModule.NetService service) {
        this.service = service;
    }

    /**
     * Loads the model with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the model loaded
     */
    @Override
    public Single<NetCompany> load(int id) {
        return service.load(id, "*")
                .retry(1)
                .map(responses -> {
                    if (responses.size() == 0)
                        throw new RuntimeException("Not found");
                    return responses.get(0);
                }).firstOrError();
    }
}
