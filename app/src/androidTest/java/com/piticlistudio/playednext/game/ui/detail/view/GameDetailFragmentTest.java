package com.piticlistudio.playednext.game.ui.detail.view;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

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

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


/**
 * Test cases for GameDetailFragment
 * Created by jorge.garcia on 22/02/2017.
 */
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
    GameDetailContract.Interactor interactor;

    private Intent getLaunchIntent() {
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent result = new Intent(targetContext, GameDetailActivity.class);
        result.putExtra("gameId", GAMEID);
        return result;
    }

    @Test
    public void showData() throws Exception {

        // Arrange
        Game game = GameFactory.provide(GAMEID, "Game title");
        doReturn(Observable.just(game).delay(3, TimeUnit.SECONDS)).when(interactor).load(GAMEID);
        activityTestRule.launchActivity(getLaunchIntent());

        onView(withId(R.id.loading)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.error)).check(matches(not(isDisplayed())));

        Thread.sleep(5000);

        onView(withId(R.id.backdropTitle)).check(matches(withText(game.title())));
        onView(withId(R.id.platformslist)).check(new RecyclerViewItemCountAssertion(game.platforms.size()));
        onView(withId(R.id.loading)).check(matches(CustomMatchers.withAlpha(0)));

    }

    @Test
    public void showError() throws Exception {

        // Arrange
        Throwable error = new Throwable("bla");
        Game data = GameFactory.provide(GAMEID, "gameTitle");
        doAnswer(new Answer() {

            private int count = 0;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (count == 0) {
                    count++;
                    return Observable.error(error).delay(3, TimeUnit.SECONDS);
                }
                else {
                    count++;
                    return Observable.just(data).delay(5, TimeUnit.SECONDS);
                }
            }
        }).when(interactor).load(GAMEID);
        activityTestRule.launchActivity(getLaunchIntent());

        Thread.sleep(5000);

        onView(withId(R.id.error)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.loading)).check(matches(CustomMatchers.withAlpha(0)));
        onView(withText("bla")).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));

        onView(withId(R.id.retry)).perform(click());

        Thread.sleep(7000);

        onView(withId(R.id.loading)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.error)).check(matches(not(isDisplayed())));
        verify(interactor).load(GAMEID);
    }
}