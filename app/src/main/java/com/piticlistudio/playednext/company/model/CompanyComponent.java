package com.piticlistudio.playednext.company.model;

import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.di.module.NetModule;

import dagger.Component;

@Component(modules = {CompanyModule.class, NetModule.class})
public interface CompanyComponent {

    ICompanyRepository repository();
}
