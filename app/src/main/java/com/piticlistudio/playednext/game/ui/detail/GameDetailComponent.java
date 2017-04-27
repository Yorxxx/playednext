package com.piticlistudio.playednext.game.ui.detail;


import com.piticlistudio.playednext.di.PerActivity;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.ui.detail.view.GameDetailFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = GameComponent.class, modules = GameDetailModule.class)
public interface GameDetailComponent {

    //void inject(GameDetailFragment fragment);
}
