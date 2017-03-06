package com.piticlistudio.playednext.company.model;

import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.di.module.IGDBModule;

import dagger.Component;

@Component(modules = {CompanyModule.class, IGDBModule.class})
public interface CompanyComponent {

    ICompanyRepository repository();
}
