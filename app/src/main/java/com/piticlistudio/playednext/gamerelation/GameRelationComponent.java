package com.piticlistudio.playednext.gamerelation;

import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.repository.IGameRepository;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.relationinterval.RelationIntervalModule;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(dependencies = {GameComponent.class},
        modules = {GameRelationModule.class, RelationIntervalModule.class})
public interface GameRelationComponent {

    IGameRelationRepository repository();
    IGameRepository gameRepository();
    RelationIntervalRepository intervalRepository();

//    GameRelationDetailPresenter detailPresenter();
//
//    GameRelationMapper mapper();
//
//    GameRelationRepository repository();
//
//    GameRelationListPresenter listPresenter();
//
//    GameRelationListAdapter listAdapter();
}
