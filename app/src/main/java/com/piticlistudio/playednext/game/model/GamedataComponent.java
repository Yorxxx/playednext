package com.piticlistudio.playednext.game.model;


import com.piticlistudio.playednext.di.module.NetModule;
import com.piticlistudio.playednext.game.model.repository.datasource.GamedataRepository;

import dagger.Component;

@Component(modules = {GamedataModule.class, NetModule.class})
public interface GamedataComponent {

    GamedataRepository repository();
}
