package com.piticlistudio.playednext.game.ui.detail.view;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.CustomMatchers;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.RecyclerViewItemCountAssertion;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.GameModule;
import com.piticlistudio.playednext.game.model.GamedataComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.detail.GameDetailContract;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Test cases for GameDetailFragment
 * Created by jorge.garcia on 22/02/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameDetailFragmentTest {

    private final static int GAMEID = 100;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public DaggerMockRule<GamedataComponent> componentDaggerMockRule = new DaggerMockRule<>(GamedataComponent.class)
            .set(component -> {
                AndroidApplication app = (AndroidApplication) InstrumentationRegistry.getInstrumentation().getTargetContext()
                        .getApplicationContext();
                GameComponent gameComponent = component.plus(new AppModule(app), new GameModule(), new CollectionModule(), new
                        CompanyModule(), new GenreModule(), new PlatformModule());
                app.setGameComponent(gameComponent);
            });

    @Rule
    public ActivityTestRule<GameDetailActivity> activityTestRule = new ActivityTestRule<>(GameDetailActivity.class, false, false);

    @Mock
    GameDetailContract.Presenter presenter;

    private Intent getLaunchIntent() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent result = new Intent(targetContext, GameDetailActivity.class);
        result.putExtra("gameId", GAMEID);
        return result;
    }

    private GameDetailFragment getFragment() {
        return activityTestRule.getActivity().currentFragment;
    }

    @Before
    public void setUp() throws Exception {
        activityTestRule.launchActivity(getLaunchIntent());
    }

    @Test
    public void given_createdView_When_ActivityCreated_Then_AttachesViewToPresenter() throws Exception {

        // Assert
        verify(presenter).attachView(activityTestRule.getActivity().currentFragment);
    }

    @Test
    public void given_createdViewWithArgs_When_ActivityCreated_Then_RequestsPresenter() throws Exception {

        verify(presenter).loadData(GAMEID);
    }

    @Test
    public void given_idleStatus_When_ActivityCreated_Then_LoadingAndErrorAreHidden() throws Exception {

        onView(withId(R.id.loading)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.error)).check(matches(not(isDisplayed())));
        onView(withId(R.id.platformslist)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void given_showLoading_When_Idle_Then_ShowsLoading() throws Throwable {

        activityTestRule.runOnUiThread(() -> getFragment().showLoading());

        // Assert
        onView(withId(R.id.loading)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.error)).check(matches(not(isDisplayed())));
    }

    @Test
    public void given_showError_When_Loading_Then_ShowsError() throws Throwable {

        final Exception error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(new Exception("bla")));

        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.loading)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.errorMsg)).check(matches(withText(error.getLocalizedMessage())));
        onView(withId(R.id.retry)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.retryLoading)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_retry_When_error_Then_RetriesRequest() throws Throwable {

        // Arrange
        final Exception error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));
        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));

        // Act
        onView(withId(R.id.retry)).perform(click());

        // Assert
        verify(presenter, times(2)).loadData(GAMEID);
        onView(withId(R.id.retry)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_showLoading_When_Retrying_Then_ShowsLoading() throws Throwable {

        // Arrange
        final Exception error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));
        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));

        onView(withId(R.id.retry)).perform(click());

        // Act
        activityTestRule.runOnUiThread(() -> getFragment().showLoading());

        // Assert
        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.loading)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.errorMsg)).check(matches(withText(error.getLocalizedMessage())));
        onView(withId(R.id.retry)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.retryLoading)).check(matches(CustomMatchers.withAlpha(1)));
    }

    @Test
    public void given_setData_When_setData_Then_SetsData() throws Throwable {

        Game game = GameFactory.provide(GAMEID, "Game title");
        assertNotNull(game.storyline);
        assertTrue(game.developers.size() > 0);

        activityTestRule.runOnUiThread(() -> getFragment().setData(game));

        // Assert
//        onView(withId(R.id.toolbar)).check(matches(withText("Game title")));
        onView(withId(R.id.backdropTitle)).check(matches(withText("Game title")));
        onView(withId(R.id.platformslist)).check(new RecyclerViewItemCountAssertion(game.platforms.size()));
        onView(withId(R.id.gamerelation_switch_box)).check(matches(isDisplayed()));
        onView(withId(R.id.listview)).check(new RecyclerViewItemCountAssertion(2)); // Should display storyline and info rows
    }

    @Test
    public void given_loadingStatus_When_showContent_Then_HidesLoading() throws Throwable {

        activityTestRule.runOnUiThread(() -> getFragment().showLoading());
        onView(withId(R.id.loading)).check(matches(isDisplayed()));

        activityTestRule.runOnUiThread(() -> getFragment().showContent());
        onView(withId(R.id.loading)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_errorStatus_When_ShowContent_Then_HidesError() throws Throwable {

        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));
        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));

        // Act
        activityTestRule.runOnUiThread(() -> getFragment().showContent());

        // Assert
        onView(withId(R.id.error)).check(matches(CustomMatchers.isNotVisible()));
    }
}