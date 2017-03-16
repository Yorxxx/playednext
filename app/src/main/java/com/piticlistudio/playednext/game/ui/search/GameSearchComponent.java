package com.piticlistudio.playednext.game.ui.search;


import com.piticlistudio.playednext.di.PerActivity;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.ui.search.view.GameSearchFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = {AppComponent.class, GameComponent.class}, modules = GameSearchModule.class)
public interface GameSearchComponent {

    void inject(GameSearchFragment fragment);
}
