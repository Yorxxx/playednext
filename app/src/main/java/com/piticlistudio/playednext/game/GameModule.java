package com.piticlistudio.playednext.game;

import com.piticlistudio.playednext.collection.model.repository.CollectionRepository;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.repository.GameRepository;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.game.ui.detail.interactor.GameDetailInteractor;

import dagger.Module;
import dagger.Provides;

/**
 * A module that provides Game classes
 * Created by jorge.garcia on 10/02/2017.
 */
@Module
public class GameModule {

    @Provides
    public GameDetailContract.Interactor provideDetailInteractor(GameRepository repository) {
        return new GameDetailInteractor(repository);
    }

//    @Provides
//    public GameRepository provideRepository(IGamedataRepository repository,
//                                            GameMapper mapper,
//                                            ICollectionRepository collectionRepository,
//                                            ICompanyRepository companyRepository) {
//        return new GameRepository(repository, mapper, collectionRepository, companyRepository);
//    }
}
