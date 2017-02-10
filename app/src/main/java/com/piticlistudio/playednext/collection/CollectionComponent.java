package com.piticlistudio.playednext.collection;

import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.di.module.NetModule;

import dagger.Component;

@Component(modules = {CollectionModule.class, NetModule.class})
public interface CollectionComponent {

    ICollectionRepository repository();
}
