package com.piticlistudio.playednext.gamerelation.ui.list.view;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.piticlistudio.playednext.AndroidApplication;
import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.CustomMatchers;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.RecyclerViewItemCountAssertion;
import com.piticlistudio.playednext.RecyclerViewMatcher;
import com.piticlistudio.playednext.di.component.AppComponent;
import com.piticlistudio.playednext.di.module.AppModule;
import com.piticlistudio.playednext.game.GameComponent;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.GameRelationComponent;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListComponent;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListContract;
import com.piticlistudio.playednext.gamerelation.ui.list.GameRelationListModule;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Test cases for GameRelationListActivity
 * Created by jorge.garcia on 07/03/2017.
 */
public class GameRelationListActivityTest extends BaseAndroidTest {

    @Mock
    GameRelationListContract.Presenter presenter;

    @Rule
    public DaggerMockRule<GameRelationListComponent> mockRule = new DaggerMockRule<>(GameRelationListComponent.class, new
            GameRelationListModule())
            .addComponentDependency(AppComponent.class, new AppModule(getApp()))
            .addComponentDependency(GameRelationComponent.class)
            .addComponentDependency(GameRelationComponent.class, GameComponent.class)
            .set(component -> { getApp().setRelationListComponent(component);});
    @Rule
    public ActivityTestRule<GameRelationListActivity> activityTestRule = new ActivityTestRule<>(GameRelationListActivity.class, false, false);

    private AndroidApplication getApp() {
        return (AndroidApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Before
    public void setUp() throws Exception {
        activityTestRule.launchActivity(null);
    }

    @Test
    public void given_activity_when_startsView_Then_RequestsPresenterToLoadData() throws Exception {

        // Assert
        verify(presenter).attachView(activityTestRule.getActivity());
        verify(presenter).loadData();
    }

    @Test
    public void given_noData_When_ViewCreated_Then_showsHeaders() throws Exception {

        onView(withId(R.id.listview)).check(new RecyclerViewItemCountAssertion(3));
        onView(withRecyclerView(R.id.listview).atPosition(0)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_done))));
        onView(withRecyclerView(R.id.listview).atPosition(1)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_playing))));
        onView(withRecyclerView(R.id.listview).atPosition(2)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_pending))));
        onView(withId(R.id.addBtn)).check(matches(CustomMatchers.isVisibleToUser(true)));
    }

    @Test
    public void given_empty_when_setData_Then_SetsData() throws Exception {

        List<GameRelation> completedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameRelation completedItem = GameRelation.create(Game.create(i, "completed_"+i), i);
            completedItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.DONE, i));
            completedItems.add(completedItem);
        }
        List<GameRelation> currentItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GameRelation currentItem = GameRelation.create(Game.create(10+i, "current_"+i), i);
            currentItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PLAYING, i));
            currentItems.add(currentItem);
        }
        List<GameRelation> pendingItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            GameRelation pendingItem = GameRelation.create(Game.create(100+i, "todo_"+i), i);
            pendingItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PENDING, i));
            pendingItems.add(pendingItem);
        }

        activityTestRule.getActivity().runOnUiThread(() -> {
            activityTestRule.getActivity().setData(completedItems, currentItems, pendingItems);
        });

        Thread.sleep(5000);

        onView(withId(R.id.listview)).check(new RecyclerViewItemCountAssertion(3+completedItems.size()+currentItems.size()+pendingItems.size()));
        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(hasDescendant(withText("completed_"+i))));
        }
        onView(withRecyclerView(R.id.listview).atPosition(0)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_done))));

        String expectedCompletedSubtitle = getApp().getResources().getQuantityString(R.plurals.gamerelation_list_header_subtitle, completedItems.size(), completedItems.size());
        onView(withRecyclerView(R.id.listview).atPosition(0)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_done))));
        onView(withRecyclerView(R.id.listview).atPosition(0)).check(matches(hasDescendant(withText(expectedCompletedSubtitle))));

        int currentStartPos = completedItems.size() + 1;
        onView(withId(R.id.listview)).perform(RecyclerViewActions.scrollToPosition(currentStartPos));
        String expectedCurrentSubtitle = getApp().getResources().getQuantityString(R.plurals.gamerelation_list_header_subtitle, currentItems.size(), currentItems.size());
        onView(withRecyclerView(R.id.listview).atPosition(currentStartPos)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_playing))));
        onView(withRecyclerView(R.id.listview).atPosition(currentStartPos)).check(matches(hasDescendant(withText(expectedCurrentSubtitle))));

        int pendingStartPos = currentStartPos + currentItems.size() +1;
        onView(withId(R.id.listview)).perform(RecyclerViewActions.scrollToPosition(pendingStartPos));
        String expectedWaitingSubtitle = getApp().getResources().getQuantityString(R.plurals.gamerelation_list_header_subtitle, pendingItems.size(), pendingItems.size());
        onView(withRecyclerView(R.id.listview).atPosition(pendingStartPos)).check(matches(hasDescendant(withText(R.string.gamerelation_list_header_title_pending))));
        onView(withRecyclerView(R.id.listview).atPosition(pendingStartPos)).check(matches(hasDescendant(withText(expectedWaitingSubtitle))));
    }

    @Test
    public void given_visibleItems_when_clickOnToggle_Then_HidesItems() throws Exception {

        List<GameRelation> completedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameRelation completedItem = GameRelation.create(Game.create(i, "completed_"+i), i);
            completedItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.DONE, i));
            completedItems.add(completedItem);
        }
        List<GameRelation> currentItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GameRelation currentItem = GameRelation.create(Game.create(10+i, "current_"+i), i);
            currentItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PLAYING, i));
            currentItems.add(currentItem);
        }
        List<GameRelation> pendingItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            GameRelation pendingItem = GameRelation.create(Game.create(100+i, "todo_"+i), i);
            pendingItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PENDING, i));
            pendingItems.add(pendingItem);
        }

        activityTestRule.getActivity().runOnUiThread(() -> {
            activityTestRule.getActivity().setData(completedItems, currentItems, pendingItems);
        });

        Thread.sleep(5000);
        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(CustomMatchers.isVisibleToUser(true)));
        }

        // Act
        onView(withId(R.id.listview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.toggle)));
        Thread.sleep(1000);

        // Assert
        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(CustomMatchers.isVisibleToUser(false)));
        }

    }

    @Test
    public void given_hiddenItems_when_clickOnToggle_Then_ShowsItems() throws Exception {

        List<GameRelation> completedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameRelation completedItem = GameRelation.create(Game.create(i, "completed_"+i), i);
            completedItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.DONE, i));
            completedItems.add(completedItem);
        }
        List<GameRelation> currentItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GameRelation currentItem = GameRelation.create(Game.create(10+i, "current_"+i), i);
            currentItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PLAYING, i));
            currentItems.add(currentItem);
        }
        List<GameRelation> pendingItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            GameRelation pendingItem = GameRelation.create(Game.create(100+i, "todo_"+i), i);
            pendingItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.PENDING, i));
            pendingItems.add(pendingItem);
        }

        activityTestRule.getActivity().runOnUiThread(() -> {
            activityTestRule.getActivity().setData(completedItems, currentItems, pendingItems);
        });

        Thread.sleep(5000);
        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(CustomMatchers.isVisibleToUser(true)));
        }

        onView(withId(R.id.listview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.toggle)));
        Thread.sleep(1000);

        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(CustomMatchers.isVisibleToUser(false)));
        }

        // Act
        onView(withId(R.id.listview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.toggle)));
        Thread.sleep(1000);

        for (int i = 0; i < completedItems.size(); i++) {
            onView(withRecyclerView(R.id.listview).atPosition(i+1)).check(matches(CustomMatchers.isVisibleToUser(true)));
        }
    }

    @Test
    public void given_idle_when_loadData_Then_RequestsPresenter() throws Exception {

        // Act
        activityTestRule.getActivity().loadData();

        // Assert
        verify(presenter, atLeastOnce()).loadData();
    }

    @Test
    public void given_searchNotDisplayed_When_ClickOnAddButton_Then_DisplaysSearch() throws Exception {

        onView(withId(R.id.searchview)).check(doesNotExist());

        // Act
        onView(withId(R.id.addBtn)).perform(click());
        Thread.sleep(2500);

        // Assert
        onView(withId(R.id.searchview)).check(matches(isDisplayed()));
    }

    @Test
    public void given_filledList_when_ClickOnRelation_Then_DisplaysDetail() throws Exception {

        List<GameRelation> completedItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GameRelation completedItem = GameRelation.create(Game.create(i, "completed_"+i), i);
            completedItem.getStatuses().add(RelationInterval.create(i, RelationInterval.RelationType.DONE, i));
            completedItems.add(completedItem);
        }
        List<GameRelation> currentItems = new ArrayList<>();
        List<GameRelation> pendingItems = new ArrayList<>();

        activityTestRule.getActivity().runOnUiThread(() -> {
            activityTestRule.getActivity().setData(completedItems, currentItems, pendingItems);
        });

        // Act
        onView(withId(R.id.listview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.backdrop)).check(matches(isDisplayed()));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}