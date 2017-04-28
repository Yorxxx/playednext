package com.piticlistudio.playednext.gamerelation.ui.list;


import com.piticlistudio.playednext.di.PerActivity;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.view.GameRelationListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {AppComponent.class}, modules = GameRelationListModule.class)
public interface GameRelationListComponent {

    void inject(GameRelationListActivity view);
}
