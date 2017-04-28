package com.piticlistudio.playednext.gamerelation.ui.list.view;

import android.support.test.rule.ActivityTestRule;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListModule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.verify;

/**
 * Test cases for GameRelationListActivity
 * Created by jorge.garcia on 07/03/2017.
 */
public class GameRelationListActivityTest extends BaseAndroidTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    GameRelationListContract.Presenter presenter;

    @Rule
    public DaggerMockRule<GameRelationListComponent> mockRule = new DaggerMockRule<>(GameRelationListComponent.class, new
            GameRelationListModule())
            .addComponentDependency(AppComponent.class, new AppModule(getApp()))
            .set(component -> getApp().setRelationListComponent(component));

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