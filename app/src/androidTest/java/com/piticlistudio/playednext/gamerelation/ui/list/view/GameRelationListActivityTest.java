package com.piticlistudio.playednext.gamerelation.ui.list.view;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.ui.detail.view.GameDetailActivity;
import com.piticlistudio.playednext.gamerelation.GameRelationComponent;
import com.piticlistudio.playednext.gamerelation.GameRelationModule;
import com.piticlistudio.playednext.gamerelation.ui.list.presenter.GameRelationListPresenter;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.verify;

/**
 * Test cases for GameRelationListActivity
 * Created by jorge.garcia on 07/03/2017.
 */
public class GameRelationListActivityTest extends BaseAndroidTest {

    @Mock
    GameRelationListPresenter presenter;

//    @Rule
//    public DaggerMockRule<GameRelationComponent> mockRule = new DaggerMockRule<>(GameRelationComponent.class, new
//            GameRelationModule())
//            .addComponentDependency(AppComponent.class, new AppModule(null))
//            .addComponentDependency(GamedataComponent.class, GameComponent.class)
//            .set(component -> {
//                presenter = component.listPresenter();
//            });

    @Rule
    public DaggerMockRule<GameRelationComponent> mockRule = new DaggerMockRule<>(GameRelationComponent.class, new
            GameRelationModule())
            .addComponentDependency(AppComponent.class, new AppModule(null))
            .addComponentDependency(GameComponent.class);

//    @Rule
//    public DaggerMockRule<GamedataComponent> componentDaggerMockRule = new DaggerMockRule<>(GamedataComponent.class)
//            .set(component -> {
//                AndroidApplication app = (AndroidApplication) InstrumentationRegistry.getInstrumentation().getTargetContext()
//                        .getApplicationContext();
//                GameComponent gameComponent = component.plus(new AppModule(app), new GameModule(), new CollectionModule(), new
//                        CompanyModule(), new GenreModule(), new PlatformModule());
//                app.setGameComponent(gameComponent);
//            })
//            .set(GameRelationComponent.class, new DaggerMockRule.ComponentSetter<GameRelationComponent>() {
//                @Override
//                public void setComponent(GameRelationComponent component) {
//                    presenter = component.listPresenter();
//                }
//            });

    @Rule
    public ActivityTestRule<GameRelationListActivity> activityTestRule = new ActivityTestRule<>(GameRelationListActivity.class, false, false);

    @Test
    public void given_activity_when_startsView_Then_RequestsPresenterToLoadData() throws Exception {

        activityTestRule.launchActivity(null);

        // Assert
        verify(presenter).attachView(activityTestRule.getActivity());
        verify(presenter).loadData();
    }
}