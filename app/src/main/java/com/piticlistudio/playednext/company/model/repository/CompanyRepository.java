package com.piticlistudio.playednext.company.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.NetCompany;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.datasource.ICompanyRepositoryDataSource;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;

/**
 * Company repository
 * Retrieves data from its associated datasources, and returns them as domain entities.
 * Created by jorge.garcia on 10/02/2017.
 */

public class CompanyRepository implements ICompanyRepository {

    private final ICompanyRepositoryDataSource<RealmCompany> dbImpl;
    private final ICompanyRepositoryDataSource<NetCompany> netImpl;
    private final CompanyMapper mapper;

    @Inject
    public CompanyRepository(@Named("db") ICompanyRepositoryDataSource<RealmCompany> dbImpl,
                             @Named("net") ICompanyRepositoryDataSource<NetCompany> netImpl,
                             CompanyMapper mapper) {
        this.dbImpl = dbImpl;
        this.netImpl = netImpl;
        this.mapper = mapper;
    }

    /**
     * Loads the Company with the specified id.
     *
     * @param id the id to load.
     * @return an Observable that emits the collection loaded
     */
    @Override
    public Observable<Company> load(int id) {
        return dbImpl.load(id)
                .map(data -> {
                    Optional<Company> result = mapper.transform(data);
                    if (!result.isPresent())
                        throw new RuntimeException("Cannot map");
                    return result.get();
                })
                .onErrorResumeNext(__ -> {
                    return netImpl.load(id)
                            .map(data -> {
                                Optional<Company> result = mapper.transform(data);
                                if (!result.isPresent())
                                    throw new RuntimeException("Cannot map");
                                return result.get();
                            });
                })
                .toObservable();
    }
}
