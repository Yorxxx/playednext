package com.piticlistudio.playednext.gamerelation.ui.detail;

import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.interactor.GameRelationDetailInteractor;
import com.piticlistudio.playednext.gamerelation.ui.detail.presenter.GameRelationDetailPresenter;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GameRelationDetailModule {

    @Provides
    protected GameRelationDetailContract.Interactor provideInteractor(IGameRelationRepository relationRepository,
                                                                      IGameRepository gameRepository,
                                                                      RelationIntervalRepository intervalRepository) {
        return new GameRelationDetailInteractor(relationRepository, gameRepository, intervalRepository);
    }

    @Provides
    protected GameRelationDetailContract.Presenter providePresenter(GameRelationDetailContract.Interactor interactor) {
        return new GameRelationDetailPresenter(interactor);
    }
}
