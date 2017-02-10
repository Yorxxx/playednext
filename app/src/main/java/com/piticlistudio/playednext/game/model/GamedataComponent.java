package com.piticlistudio.playednext.game.model;


import com.piticlistudio.playednext.di.module.NetModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.repository.datasource.GamedataRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;

import dagger.Component;

@Component(modules = {GamedataModule.class, NetModule.class})
public interface GamedataComponent {

    GamedataRepository repository();
    GameMapper mapper();

    GameComponent plus(GameModule module);
}
