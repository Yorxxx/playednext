package com.piticlistudio.playednext.gamerelation.ui.list;

import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.list.interactor.GameRelationListInteractor;
import com.piticlistudio.playednext.gamerelation.ui.list.presenter.GameRelationListPresenter;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GameRelationListModule {

    @Provides
    protected GameRelationListContract.Interactor provideInteractor(IGameRelationRepository relationRepository,
                                                                    RelationIntervalRepository intervalRepository) {
        return new GameRelationListInteractor(relationRepository, intervalRepository);
    }

    @Provides
    protected GameRelationListContract.Presenter providePresenter(GameRelationListContract.Interactor interactor) {
        return new GameRelationListPresenter(interactor);
    }
}
