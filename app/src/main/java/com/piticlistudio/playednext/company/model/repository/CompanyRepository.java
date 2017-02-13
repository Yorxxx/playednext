package com.piticlistudio.playednext.company.model.repository;

import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.repository.datasource.ICompanyRepositoryDataSource;
import com.piticlistudio.playednext.mvp.model.repository.BaseRepository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Company repository
 * Retrieves data from its associated datasources, and returns them as domain entities.
 * Created by jorge.garcia on 10/02/2017.
 */

public class CompanyRepository extends BaseRepository<Company, ICompanyData> implements ICompanyRepository {

    @Inject
    public CompanyRepository(@Named("db") ICompanyRepositoryDataSource<ICompanyData> dbImpl,
                             @Named("net") ICompanyRepositoryDataSource<ICompanyData> netImpl,
                             CompanyMapper mapper) {
        super(dbImpl, netImpl, mapper);
    }
}
