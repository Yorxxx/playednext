package com.piticlistudio.playednext.game;


import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.game.model.repository.GameRepository;

import dagger.Subcomponent;

@Subcomponent(modules = {GameModule.class, CollectionModule.class})
public interface GameComponent {

    GameRepository repository();
}
