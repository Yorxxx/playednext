package com.piticlistudio.playednext.gamerelation;

import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.repository.GameRelationRepository;
import com.piticlistudio.playednext.gamerelation.ui.detail.presenter.GameRelationDetailPresenter;
import com.piticlistudio.playednext.gamerelation.ui.list.adapter.GameRelationListAdapter;
import com.piticlistudio.playednext.gamerelation.ui.list.presenter.GameRelationListPresenter;
import com.piticlistudio.playednext.relationinterval.RelationIntervalModule;

import dagger.Component;

@Component(dependencies = {GameComponent.class, AppComponent.class},
        modules = {GameRelationModule.class, RelationIntervalModule.class})
public interface GameRelationComponent {

    GameRelationDetailPresenter detailPresenter();

    GameRelationMapper mapper();

    GameRelationRepository repository();

    GameRelationListPresenter listPresenter();

    GameRelationListAdapter listAdapter();
}
