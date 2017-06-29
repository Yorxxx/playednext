package com.piticlistudio.playednext.gamerelation.service;


import com.piticlistudio.playednext.di.PerActivity;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.gamerelation.GameRelationModule;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListModule;
import com.piticlistudio.playednext.gamerelation.ui.list.view.GameRelationListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {AppComponent.class})
public interface GameRelationServiceComponent {

    void inject(GameRelationSyncService service);

    GameRelationSyncManager manager();
}
