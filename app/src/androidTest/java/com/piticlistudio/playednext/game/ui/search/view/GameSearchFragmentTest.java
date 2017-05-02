package com.piticlistudio.playednext.game.ui.search.view;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.WindowManager;
import android.widget.EditText;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.CustomMatchers;
import com.piticlistudio.playednext.EmptyActivity;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.RecyclerViewItemCountAssertion;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.ui.search.GameSearchComponent;
import com.piticlistudio.playednext.game.ui.search.GameSearchContract;
import com.piticlistudio.playednext.game.ui.search.GameSearchModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Test cases for GameSearchFragment
 * Created by jorge.garcia on 23/02/2017.
 */
public class GameSearchFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Rule
    public DaggerMockRule<GameSearchComponent> componentRule = new DaggerMockRule<>(GameSearchComponent.class, new GameSearchModule())
            .addComponentDependency(AppComponent.class, new AppModule(getApp()))
            .set(component -> getApp().setGameSearchComponent(component));
    @Rule
    public ActivityTestRule<EmptyActivity> activityTestRule = new ActivityTestRule<>(EmptyActivity.class, false, false);
    @Mock
    GameSearchContract.Presenter presenter;

    private GameSearchFragment getFragment() {
        return activityTestRule.getActivity().currentFragment;
    }

    private AndroidApplication getApp() {
        return (AndroidApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Before
    public void setUp() throws Exception {
        activityTestRule.launchActivity(null);
        EmptyActivity activity = activityTestRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Test
    public void Given_Launched_When_Attaches_Then_AttachesPresenter() throws Exception {
        verify(presenter).attachView(getFragment());
    }

    @Test
    public void Given_Launched_When_onDestroyView_Then_DetachesPresenter() throws Exception {
        // Act
        getFragment().onDestroyView();

        // Assert
        verify(presenter).detachView(false);
    }

    @Test
    public void Given_Idle_When_ActivityCreated_Then_ShowsInitialView() throws Exception {

        onView(withId(R.id.progress)).check(matches(not(isDisplayed())));
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.initialstateview)).check(matches(isDisplayed()));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isVisibleToUser(true)));
        onView(withText(R.string.game_search_emptyview_title)).check(matches(isDisplayed()));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.searchview)).check(matches(isDisplayed()));
        onView(withId(R.id.searchview)).check(matches(CustomMatchers.isSearchIconified(false)));
    }

    @Test
    public void Given_SearchInput_When_InputsText_Then_RequestsPresenter() throws Exception {

        onView(withId(R.id.searchview)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("mario"));

        verify(presenter).search("m", 0, getFragment().loadLimit);
        verify(presenter).search("ma", 0, getFragment().loadLimit);
        verify(presenter).search("mar", 0, getFragment().loadLimit);
        verify(presenter).search("mari", 0, getFragment().loadLimit);
        verify(presenter).search("mario", 0, getFragment().loadLimit);
    }

    @Test
    public void Given_Idle_When_PressCloseButton_Then_NotifiesListener() throws Exception {

        GameSearchFragment.IGameSearchFragmentListener listener = mock(GameSearchFragment.IGameSearchFragmentListener.class);

        getFragment().setListener(listener);

        // Act
        onView(withId(R.id.closeBtn)).perform(click());

        Thread.sleep(500);

        // Assert
        verify(listener).onCloseSearchClicked(any());
    }

    @Test
    public void Given_Idle_When_ShowLoading_Then_ShowsLoading() throws Throwable {

        Espresso.closeSoftKeyboard();
        activityTestRule.runOnUiThread(() -> getFragment().showLoading());

        onView(withId(R.id.progress)).check(matches(CustomMatchers.isVisibleToUser(true)));
    }

    //@Test
    public void Given_InitialViewState_When_ShowContent_Then_HidesInitialView() throws Throwable {

        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isVisibleToUser(true)));

        activityTestRule.runOnUiThread(() -> getFragment().showContent());

        Thread.sleep(500);
        // Assert
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
    }

    @Test
    public void Given_ErrorViewState_When_ShowContent_Then_HidesErrorView() throws Throwable {

        activityTestRule.runOnUiThread(() -> getFragment().showError(new Exception("bla")));

        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(true)));

        activityTestRule.runOnUiThread(() -> getFragment().showContent());

        // Assert
        onView(withId(R.id.progress)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(false)));
    }

    @Test
    public void Given_EmptyList_When_SetData_Then_ShowsData() throws Throwable {

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(GameFactory.provide(10, "title"));
        }

        // Act
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));

        // Assert
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(10));
        onView(withId(R.id.loadmore_progress)).check(doesNotExist());
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
    }

    @Test
    public void Given_EmptyList_When_SetEmptyData_Then_ShowsEmptyStateView() throws Throwable {

        List<Game> data = new ArrayList<>();

        // Act
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));

        // Assert
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(0));
        onView(withId(R.id.loadmore_progress)).check(doesNotExist());
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(true)));
    }

    @Test
    public void Given_settingMaxItems_When_SetData_Then_ShowDataAndLoadMoreRow() throws Throwable {

        Espresso.closeSoftKeyboard();
        int maxItems = getFragment().loadLimit;
        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title"));
        }

        // Act
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));

        // Assert
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(maxItems + 1)); // Plus load more item
    }

    @Test
    public void Given_MaxLoadedList_When_SwipingDown_Then_RequestsMoreData() throws Throwable {

        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.scrollToPosition(maxItems));

        // Assert
        onView(withId(R.id.loadmore_progress)).check(matches(isDisplayed()));
        verify(presenter).search("", maxItems + 1, maxItems);
    }

    @Test
    public void Given_NotFilledList_When_SwipingDown_Then_DoesNotRequestMoreData() throws Throwable {

        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems / 2; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.scrollToPosition(maxItems));

        // Assert
        verify(presenter, never()).search("", maxItems + 1, maxItems);
    }

    @Test
    public void Given_FilledList_When_SetData_Then_AppendsData() throws Throwable {

        // Arrange
        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(maxItems + 1));
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.scrollToPosition(maxItems));


        // Act
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));

        // Assert
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(maxItems * 2));
    }

    @Test
    public void Given_InitialViewState_When_ShowError_Then_ShowsErrorAndHideInitialView() throws Throwable {

        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isVisibleToUser(true)));

        // Act
        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));

        Thread.sleep(500);
        // Assert
        onView(withId(R.id.initialstateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(true)));
        onView(withText("bla")).check(matches(isDisplayed()));
    }

    @Test
    public void Given_EmptyViewState_When_ShowError_Then_ShowsErrorAndHidesEmptyView() throws Throwable {

        // Arrange
        List<Game> data = new ArrayList<>();
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(true)));

        // Act
        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));

        Thread.sleep(500);

        // Assert
        onView(withId(R.id.emptystateview)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(true)));
        onView(withText("bla")).check(matches(isDisplayed()));
    }

    @Test
    public void Given_IdleList_When_ShowError_Then_ShowsError() throws Throwable {

        // Arrange
        int maxItems = getFragment().loadLimit;
        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems / 2; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> {
            getFragment().setData(data);
            getFragment().showContent();
        });
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(maxItems / 2));
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(false)));

        // Act
        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));

        // Assert
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(true)));
        onView(withText("bla")).check(matches(isDisplayed()));

    }

    @Test
    public void Given_LoadingMoreItems_When_ShowError_Then_ShowsSnackbarError() throws Throwable {

        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.searchlist)).check(new RecyclerViewItemCountAssertion(maxItems + 1));
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.scrollToPosition(maxItems));

        // Act
        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));

        Thread.sleep(1000);

        // Assert
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.searchlist)).check(matches(isDisplayingAtLeast(100)));
//        onView(allOf(withId(android.support.design.R.id.snackbar_text), withParent(withId(R.id.content)))).check(matches(isDisplayed()));
        onView(withText("bla")).check(matches(CustomMatchers.isVisibleToUser(true)));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(withText(R.string.game_search_error_retry_button)));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(isDisplayed()));
    }

    @Test
    public void Given_LoadingMoreErrorIsDisplayed_When_ClickOnRetry_Then_RetriesRequest() throws Throwable {

        // Arrange
        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title"));
        }
        activityTestRule.runOnUiThread(() -> getFragment().setData(data));
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.scrollToPosition(maxItems));

        Throwable error = new Exception("bla");
        activityTestRule.runOnUiThread(() -> getFragment().showError(error));
        Thread.sleep(1000);

        // Act
        onView(withId(R.id.gamesearch_error)).check(matches(CustomMatchers.isVisibleToUser(false)));
        onView(withId(R.id.searchlist)).check(matches(isDisplayingAtLeast(100)));
        onView(withId(android.support.design.R.id.snackbar_action)).check(matches(isDisplayed()));
        onView(withId(android.support.design.R.id.snackbar_action)).perform(click());

        // Assert
        verify(presenter).search("", maxItems + 1, maxItems);
    }

    @Test
    public void Given_FilledList_When_ClickOnItem_Then_LaunchesDetailView() throws Throwable {

// Arrange
        int maxItems = getFragment().loadLimit;

        Espresso.closeSoftKeyboard();

        List<Game> data = new ArrayList<>();
        for (int i = 0; i < maxItems; i++) {
            data.add(GameFactory.provide(10, "title_" + i));
        }
        activityTestRule.runOnUiThread(() -> {
            getFragment().setData(data);
            getFragment().showContent();
        });

        // ACt
        onView(withId(R.id.searchlist)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("title_5")), click()));

        // Assert
        onView(withId(R.id.searchlist)).check(doesNotExist());
        onView(withId(R.id.backdrop)).check(matches(CustomMatchers.isVisibleToUser(true)));
    }
}