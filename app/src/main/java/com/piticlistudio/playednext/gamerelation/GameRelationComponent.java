package com.piticlistudio.playednext.gamerelation;

import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.interactor.GameRelationDetailInteractor;
import com.piticlistudio.playednext.gamerelation.ui.detail.presenter.GameRelationDetailPresenter;

import dagger.Component;

@Component(dependencies = {GameComponent.class},
        modules = {GameRelationModule.class})
public interface GameRelationComponent {

    GameRelationDetailPresenter detailPresenter();

    GameRelationDetailInteractor detailInteractor();

    GameRelationMapper mapper();

    GameRelationRepository repository();
}
