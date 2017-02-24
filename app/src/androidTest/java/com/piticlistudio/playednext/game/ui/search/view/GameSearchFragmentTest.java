package com.piticlistudio.playednext.game.ui.search.view;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.CustomMatchers;
import com.piticlistudio.playednext.EmptyActivity;
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
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;
import com.piticlistudio.playednext.genre.GenreModule;
import com.piticlistudio.playednext.platform.PlatformModule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Test cases for GameSearchFragment
 * Created by jorge.garcia on 23/02/2017.
 */
public class GameSearchFragmentTest {


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
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class, false, false);

    @Mock
    GameSearchContract.Interactor interactor;

    @Test
    public void given_NoPreviousData_When_NoSearch_Then_showsInitialViews() throws Exception {

        activityTestRule.launchActivity(null);

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())));
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.initialstateview)).check(matches(isDisplayed()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withText(R.string.game_search_emptyview_title)).check(matches(isDisplayed()));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));
        verifyZeroInteractions(interactor);
    }

    @Test
    public void given_HasMoreDataToLoad_When_SearchTerms_Then_LoadsMoreItems() throws Exception {

        // Ensure that keyboard is hidden
        /// adb shell settings put secure show_ime_with_hard_keyboard 0

        activityTestRule.launchActivity(null);

        // Arrange
        doAnswer(invocation -> {
            String query = (String) invocation.getArguments()[0];
            int offset = (int) invocation.getArguments()[1];
            int limit = (int) invocation.getArguments()[2];

            List<Game> data = new ArrayList<>();
            for (int i = offset; i < offset + limit; i++) {
                data.add(GameFactory.provide(i, query + i));
            }
            return Observable.just(data).delay(2, TimeUnit.SECONDS);
        }).when(interactor).search(anyString(), anyInt(), anyInt());

        onView(withId(R.id.searchview)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"), pressImeActionButton());

        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000); // Wait to fill data

        verify(interactor).search("mario", 0, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(16)); //+1 for the loading more
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));

        // Scroll to bottom to load more items
        onView(withId(R.id.searchlist)).perform(ViewActions.swipeUp());
        onView(withId(R.id.loadmore_progress)).check(matches(isDisplayed()));
        Thread.sleep(3000);

        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(31)); //+1 for the loading more
        verify(interactor).search("mario", 16, 15);
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_HasNoMoreDataToLoad_When_SearchTerms_Then_DoesNotRequestMoreItems() throws Exception {

        // Ensure that keyboard is hidden
        /// adb shell settings put secure show_ime_with_hard_keyboard 0
        activityTestRule.launchActivity(null);

        // Arrange
        doAnswer(invocation -> {
            String query = (String) invocation.getArguments()[0];
            int offset = (int) invocation.getArguments()[1];
            int limit = (int) invocation.getArguments()[2];

            List<Game> data = new ArrayList<>();
            for (int i = offset; i < limit / 2; i++) {
                data.add(GameFactory.provide(i, query + i));
            }
            return Observable.just(data).delay(2, TimeUnit.SECONDS);
        }).when(interactor).search(anyString(), anyInt(), anyInt());

        onView(withId(R.id.searchview)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"), pressImeActionButton());

        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(7));
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));

        // Scroll to bottom to load more items
        onView(withId(R.id.searchlist)).perform(ViewActions.swipeUp());
        onView(withId(R.id.loadmore_progress)).check(doesNotExist());

        Thread.sleep(3000);

        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(7)); //+1 for the loading more
        verify(interactor).search("mario", 0, 15);
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_RequestError_when_SearchTerms_Then_ShowErrorView() throws Exception {

        activityTestRule.launchActivity(null);

        // Arrange
        final Throwable error = new Exception("Could not connect");
        doAnswer(invocation -> Observable.error(error).delay(3, TimeUnit.SECONDS)).when(interactor).search(anyString(), anyInt(), anyInt());

        onView(withId(R.id.searchview)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"), pressImeActionButton());

        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        verify(interactor).search("mario", 0, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.withAlpha(1)));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_RequestError_when_LoadingMore_Then_ShowErrorAndPreviousData() throws Exception {

        activityTestRule.launchActivity(null);

        // Arrange
        final Throwable error = new Exception("Could not connect");
        doAnswer(new Answer() {

            private int callCount = 0;

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (callCount == 1) {
                    return Observable.error(error).delay(3, TimeUnit.SECONDS);
                }
                callCount++;
                String query = (String) invocation.getArguments()[0];
                int offset = (int) invocation.getArguments()[1];
                int limit = (int) invocation.getArguments()[2];

                List<Game> data = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    data.add(GameFactory.provide(i, query + i));
                }
                return Observable.just(data).delay(2, TimeUnit.SECONDS);
            }
        }).when(interactor).search(anyString(), anyInt(), anyInt());

        onView(withId(R.id.searchview)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"), pressImeActionButton());

        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        verify(interactor).search("mario", 0, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(16));
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));

        // Scroll to bottom to load more items
        onView(withId(R.id.searchlist)).perform(ViewActions.swipeUp());
        onView(withId(R.id.loadmore_progress)).check(doesNotExist());

        Thread.sleep(3000);

        verify(interactor).search("mario", 16, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(15)); // When fails while loading more, remove that cell
        // from the adapter
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText(error.getLocalizedMessage())));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(withText(R.string.game_search_error_retry_button)));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());
        onView(withId(R.id.loadmore_progress)).check(matches(isDisplayed()));
        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        verify(interactor).search("mario", 16, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(31));

//        onView(withText(error.getLocalizedMessage())).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow()
//                .getDecorView())))).check(matches(isDisplayed()));
//        onView(withId(R.id.loadmore_progress)).check(doesNotExist());
//        onView(withText(R.string.game_search_error_retry_button)).inRoot(withDecorView(not(is(activityTestRule.getActivity().getWindow()
//                .getDecorView())))).check(matches(isDisplayed()));
//
//        onView(withText(R.string.game_search_error_retry_button)).perform(click());
//        verify(interactor).search("mario", 16, 15);
//        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isNotVisible()));
    }

    @Test
    public void given_searchHasNoResults_When_searchingName_Then_showEmptyView() throws Exception {

        activityTestRule.launchActivity(null);

        // Arrange
        doAnswer(invocation -> {
            List<Game> data = new ArrayList<>();
            return Observable.just(data).delay(2, TimeUnit.SECONDS);
        }).when(interactor).search(anyString(), anyInt(), anyInt());

        onView(withId(R.id.searchview)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"), pressImeActionButton());

        onView(withId(R.id.progress)).check(matches(isDisplayed()));

        Thread.sleep(3000);

        verify(interactor).search("mario", 0, 15);
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isNotVisible()));
        onView(withId(R.id.emptystateview)).check(matches(not(CustomMatchers.isNotVisible())));
    }
}