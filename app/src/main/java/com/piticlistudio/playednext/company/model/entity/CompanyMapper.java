package com.piticlistudio.playednext.company.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;

import javax.inject.Inject;

/**
 * Mapper of ICompanyData entities into Company models.
 * Created by jorge.garcia on 13/02/2017.
 */

public class CompanyMapper implements Mapper<Company, ICompanyData> {

    @Inject
    public CompanyMapper() {
    }

    /**
     * Transforms the data into an Optional model.
     *
     * @param data the data to transform
     * @return an Optional model.
     */
    @Override
    public Optional<Company> transform(ICompanyData data) {
        if (data == null || data.getName() == null)
            return Optional.absent();
        return Optional.of(Company.create(data.getId(), data.getName()));
    }
}
