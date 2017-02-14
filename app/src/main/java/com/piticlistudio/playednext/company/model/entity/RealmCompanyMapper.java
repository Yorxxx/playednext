package com.piticlistudio.playednext.company.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Maps a Company into a RealmCompany
 * Created by jorge.garcia on 14/02/2017.
 */

public class RealmCompanyMapper implements Mapper<RealmCompany, Company> {

    @Inject
    public RealmCompanyMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<RealmCompany> transform(Company data) {
        if (data == null)
            return Optional.absent();
        return Optional.of(new RealmCompany(data.id(), data.name()));
    }
}
