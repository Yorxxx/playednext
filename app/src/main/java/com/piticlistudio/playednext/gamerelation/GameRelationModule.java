package com.piticlistudio.playednext.gamerelation;


import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.RealmGameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.model.repository.datasource.IGameRelationRepositoryDatasource;
import com.piticlistudio.playednext.gamerelation.model.repository.datasource.RealmGameRelationRepositoryImpl;
import com.piticlistudio.playednext.gamerelation.ui.detail.GameRelationDetailContract;
import com.piticlistudio.playednext.gamerelation.ui.detail.interactor.GameRelationDetailInteractor;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@Module
public class GameRelationModule {

    @Provides
    @Inject
    protected GameRelationMapper relationDataMapper(GameMapper gameDataMapper) {
        return new GameRelationMapper(gameDataMapper);
    }

    @Provides
    protected IGameRelationRepositoryDatasource<IGameRelationDatasource> localRepository() {
        return new RealmGameRelationRepositoryImpl();
    }

    @Provides
    protected IGameRelationRepository provideRepository(IGameRelationRepositoryDatasource<IGameRelationDatasource> realmImpl,
                                                        GameRelationMapper relationDataMapper,
                                                        RealmGameRelationMapper realmMapper) {
        return new GameRelationRepository(realmImpl, relationDataMapper, realmMapper);
    }

    @Provides
    protected GameRelationDetailContract.Interactor provideDetailInteractor(IGameRelationRepository relationRepository, GameRepository
            gameRepository) {
        return new GameRelationDetailInteractor(relationRepository, gameRepository);
    }
}
