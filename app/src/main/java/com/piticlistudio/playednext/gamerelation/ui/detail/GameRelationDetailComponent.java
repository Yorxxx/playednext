package com.piticlistudio.playednext.gamerelation.ui.detail;


import com.piticlistudio.playednext.di.PerActivity;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.gamerelation.GameRelationComponent;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.GameRelationDetailFragment;
import com.piticlistudio.playednext.gamerelation.ui.detail.view.GameRelationDetailView;

import dagger.Component;

@PerActivity
@Component(dependencies = {AppComponent.class, GameRelationComponent.class}, modules = GameRelationDetailModule.class)
public interface GameRelationDetailComponent {

    void inject(GameRelationDetailView view);
    void inject(GameRelationDetailFragment fragment);
}
