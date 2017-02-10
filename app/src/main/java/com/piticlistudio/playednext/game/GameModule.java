package com.piticlistudio.playednext.game;

import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;

import dagger.Module;
import dagger.Provides;

/**
 * A module that provides Game classes
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class GameModule {

    @Provides
    public GameRepository provideRepository(IGamedataRepository repository,
                                            GameMapper mapper) {
        return new GameRepository(repository, mapper);
    }
}
