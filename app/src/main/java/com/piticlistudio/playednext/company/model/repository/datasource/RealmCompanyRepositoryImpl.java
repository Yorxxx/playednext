package com.piticlistudio.playednext.company.model.repository.datasource;

import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRealmRepository;

/**
 * Repository for RealmCompany entities
 * Created by jorge.garcia on 13/02/2017.
 */

public class RealmCompanyRepositoryImpl extends BaseRealmRepository<RealmCompany> implements ICompanyRepositoryDataSource<RealmCompany> {

    public RealmCompanyRepositoryImpl() {
        super(RealmCompany.class);
    }
}
