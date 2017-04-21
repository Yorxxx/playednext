package com.piticlistudio.playednext.gamerelation.ui.list.interactor;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.IGameRelationRepository;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.RelationIntervalRepository;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 01/03/2017.
 */
public class GameRelationListInteractorTest extends BaseTest {

//    @Rule
//    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    @Mock
    IGameRelationRepository relationRepository;

    @InjectMocks
    GameRelationListInteractor interactor;

    @Mock
    RelationIntervalRepository intervalRepository;

    @Test
    public void given_EmptyRepository_When_LoadCompletedItems_Then_EmitsEmptyList() throws Exception {

        List<GameRelation> list = new ArrayList<>();
        doReturn(Observable.just(list)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadCompletedItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(data -> assertTrue(data.isEmpty())));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_EmptyRepository_When_LoadCurrentItems_Then_EmitsEmptyList() throws Exception {

        List<GameRelation> list = new ArrayList<>();
        doReturn(Observable.just(list)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadCurrentItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(data -> assertTrue(data.isEmpty())));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_EmptyRepository_When_loadWaitingItems_Then_EmitsEmptyList() throws Exception {

        List<GameRelation> list = new ArrayList<>();
        doReturn(Observable.just(list)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadWaitingItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(data -> assertTrue(data.isEmpty())));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_mixedItemType_When_LoadCompletedItems_Then_EmitsOnlyCompletedItemsSortered() throws Exception {

        List<GameRelation> data = new ArrayList<>();
        GameRelation noneRelation = mock(GameRelation.class);
        when(noneRelation.getCurrent()).thenReturn(Optional.absent());

        GameRelation playingRelation = mock(GameRelation.class);
        RelationInterval playingInterval = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1000);
        when(playingRelation.getCurrent()).thenReturn(Optional.of(playingInterval));

        GameRelation completedRelation = mock(GameRelation.class);
        RelationInterval completedInterval = RelationInterval.create(2, RelationInterval.RelationType.DONE, 1000);
        completedInterval.setEndAt(2000);
        when(completedRelation.getCurrent()).thenReturn(Optional.of(completedInterval));

        GameRelation completedRelation2 = mock(GameRelation.class);
        RelationInterval completedInterval2 = RelationInterval.create(2, RelationInterval.RelationType.DONE, 1500);
        completedInterval2.setEndAt(2000);
        when(completedRelation2.getCurrent()).thenReturn(Optional.of(completedInterval2));

        GameRelation completedRelation3 = mock(GameRelation.class);
        RelationInterval completedInterval3 = RelationInterval.create(2, RelationInterval.RelationType.DONE, 800);
        completedInterval3.setEndAt(1000);
        when(completedRelation3.getCurrent()).thenReturn(Optional.of(completedInterval3));

        GameRelation waitingRelation = mock(GameRelation.class);
        RelationInterval waitingInterval = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 2000);
        when(waitingRelation.getCurrent()).thenReturn(Optional.of(waitingInterval));

        data.add(noneRelation);
        data.add(playingRelation);
        data.add(completedRelation);
        data.add(completedRelation3);
        data.add(waitingRelation);
        data.add(completedRelation);
        data.add(completedRelation2);
        data.add(playingRelation);
        data.add(noneRelation);

        doReturn(Observable.just(data)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadCompletedItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(relations -> {
                    assertEquals(4, relations.size());
                    for (GameRelation relation : relations) {
                        assertTrue(relation.getCurrent().isPresent());
                        assertEquals(RelationInterval.RelationType.DONE, relation.getCurrent().get().type());
                    }
                }))
                .assertValue(check(relations -> {
                    // Check items are ordered
                    long previous = 0;
                    for (int i = 0; i < relations.size(); i++) {
                        assertTrue(relations.get(i).getCurrent().get().startAt() >= previous);
                        previous = relations.get(i).getCurrent().get().startAt();
                    }
                }));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_mixedItemType_When_loadCurrentItems_Then_EmitsOnlySorteredCurrentItems() throws Exception {

        List<GameRelation> data = new ArrayList<>();
        GameRelation noneRelation = mock(GameRelation.class);
        when(noneRelation.getCurrent()).thenReturn(Optional.absent());

        GameRelation playingRelation = mock(GameRelation.class);
        RelationInterval playingInterval = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1000);
        when(playingRelation.getCurrent()).thenReturn(Optional.of(playingInterval));

        GameRelation playingRelation2 = mock(GameRelation.class);
        RelationInterval playingInterval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 800);
        when(playingRelation2.getCurrent()).thenReturn(Optional.of(playingInterval2));

        GameRelation playingRelation3 = mock(GameRelation.class);
        RelationInterval playingInterval3 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1100);
        when(playingRelation3.getCurrent()).thenReturn(Optional.of(playingInterval3));

        GameRelation completedRelation = mock(GameRelation.class);
        RelationInterval completedInterval = RelationInterval.create(2, RelationInterval.RelationType.DONE, 1000);
        when(completedRelation.getCurrent()).thenReturn(Optional.of(completedInterval));

        GameRelation waitingRelation = mock(GameRelation.class);
        RelationInterval waitingInterval = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 2000);
        when(waitingRelation.getCurrent()).thenReturn(Optional.of(waitingInterval));

        data.add(noneRelation);
        data.add(playingRelation3);
        data.add(playingRelation);
        data.add(completedRelation);
        data.add(waitingRelation);
        data.add(playingRelation3);
        data.add(playingRelation2);
        data.add(completedRelation);
        data.add(playingRelation);
        data.add(playingRelation2);
        data.add(noneRelation);

        doReturn(Observable.just(data)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadCurrentItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(relations -> {
                    assertEquals(6, relations.size());
                    for (GameRelation relation : relations) {
                        assertTrue(relation.getCurrent().isPresent());
                        assertEquals(RelationInterval.RelationType.PLAYING, relation.getCurrent().get().type());
                    }
                }))
                .assertValue(check(relations -> {
                    // Check items are ordered
                    long previous = 0;
                    for (int i = 0; i < relations.size(); i++) {
                        assertTrue(relations.get(i).getCurrent().get().startAt() >= previous);
                        previous = relations.get(i).getCurrent().get().startAt();
                    }
                }));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_mixedItemType_When_loadWaitingItems_Then_EmitsOnlyWaitingItems() throws Exception {

        List<GameRelation> data = new ArrayList<>();
        GameRelation noneRelation = mock(GameRelation.class);
        when(noneRelation.getCurrent()).thenReturn(Optional.absent());

        GameRelation playingRelation = mock(GameRelation.class);
        RelationInterval playingInterval = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1000);
        when(playingRelation.getCurrent()).thenReturn(Optional.of(playingInterval));

        GameRelation completedRelation = mock(GameRelation.class);
        RelationInterval completedInterval = RelationInterval.create(2, RelationInterval.RelationType.DONE, 1000);
        when(completedRelation.getCurrent()).thenReturn(Optional.of(completedInterval));

        GameRelation waitingRelation = mock(GameRelation.class);
        RelationInterval waitingInterval = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 2000);
        when(waitingRelation.getCurrent()).thenReturn(Optional.of(waitingInterval));

        data.add(noneRelation);
        data.add(playingRelation);
        data.add(completedRelation);
        data.add(waitingRelation);
        data.add(completedRelation);
        data.add(playingRelation);
        data.add(noneRelation);

        doReturn(Observable.just(data)).when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadWaitingItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(relations -> {
                    assertEquals(1, relations.size());
                    for (GameRelation relation : relations) {
                        assertTrue(relation.getCurrent().isPresent());
                        assertEquals(RelationInterval.RelationType.PENDING, relation.getCurrent().get().type());
                    }
                }));
        verify(relationRepository).loadAll();
    }

    @Test
    public void given_multipleIdenticalEmissionsByRepository_When_LoadCurrentItems_Then_OnlyEmitsIfItemsHaveChanged() throws Exception {

        List<GameRelation> data = new ArrayList<>();
        GameRelation noneRelation = mock(GameRelation.class);
        when(noneRelation.getCurrent()).thenReturn(Optional.absent());

        GameRelation playingRelation = mock(GameRelation.class);
        RelationInterval playingInterval = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1000);
        when(playingRelation.getCurrent()).thenReturn(Optional.of(playingInterval));

        GameRelation playingRelation2 = mock(GameRelation.class);
        RelationInterval playingInterval2 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 800);
        when(playingRelation2.getCurrent()).thenReturn(Optional.of(playingInterval2));

        GameRelation playingRelation3 = mock(GameRelation.class);
        RelationInterval playingInterval3 = RelationInterval.create(1, RelationInterval.RelationType.PLAYING, 1100);
        when(playingRelation3.getCurrent()).thenReturn(Optional.of(playingInterval3));

        GameRelation completedRelation = mock(GameRelation.class);
        RelationInterval completedInterval = RelationInterval.create(2, RelationInterval.RelationType.DONE, 1000);
        when(completedRelation.getCurrent()).thenReturn(Optional.of(completedInterval));

        GameRelation waitingRelation = mock(GameRelation.class);
        RelationInterval waitingInterval = RelationInterval.create(3, RelationInterval.RelationType.PENDING, 2000);
        when(waitingRelation.getCurrent()).thenReturn(Optional.of(waitingInterval));

        data.add(noneRelation);
        data.add(playingRelation3);
        data.add(playingRelation);
        data.add(completedRelation);
        data.add(waitingRelation);
        data.add(playingRelation3);
        data.add(playingRelation2);
        data.add(completedRelation);
        data.add(playingRelation);
        data.add(playingRelation2);
        data.add(noneRelation);

        doAnswer(invocation -> Observable.interval(1, TimeUnit.SECONDS)
                .take(3)
                .map(aLong -> data))
                .when(relationRepository).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = interactor.loadCurrentItems().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(relations -> {
                    assertEquals(6, relations.size());
                    for (GameRelation relation : relations) {
                        assertTrue(relation.getCurrent().isPresent());
                        assertEquals(RelationInterval.RelationType.PLAYING, relation.getCurrent().get().type());
                    }
                }))
                .assertValue(check(relations -> {
                    // Check items are ordered
                    long previous = 0;
                    for (int i = 0; i < relations.size(); i++) {
                        assertTrue(relations.get(i).getCurrent().get().startAt() >= previous);
                        previous = relations.get(i).getCurrent().get().startAt();
                    }
                }));
        verify(relationRepository).loadAll();

    }

    @Test
    public void given_any_When_Save_Then_RequestsRepository() throws Exception {

        GameRelation noneRelation = mock(GameRelation.class);
        doReturn(Completable.complete()).when(relationRepository).save(noneRelation);

        // Act
        TestObserver<Void> result = interactor.save(noneRelation).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
    }

    @Test
    public void given_any_When_Create_Then_RequestsRepository() throws Exception {

        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100);
        doReturn(interval).when(intervalRepository).create(RelationInterval.RelationType.PENDING);

        // Act
        RelationInterval result = interactor.create(RelationInterval.RelationType.PENDING);

        // Assert
        assertEquals(interval, result);
    }

    @Test
    public void given_noneCurrentRelations_When_Compare_Then_ReturnsZero() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.absent());
        when(gr2.getCurrent()).thenReturn(Optional.absent());
//        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void given_firstRelationIsNotCurrent_When_Compare_Then_ReturnsOne() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.absent());
        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void given_secondRelationIsNotCurrent_When_Compare_Then_ReturnsFirst() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr2.getCurrent()).thenReturn(Optional.absent());
        when(gr1.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void given_differentRelationTypes_When_Compare_Then_ReturnsZero() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100)));
        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PLAYING, 100)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void given_secondRelationIsLater_When_Compare_Then_ReturnsFirst() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 100)));
        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 200)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void given_firstRelationIsLater_When_Compare_Then_ReturnsSecond() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 1000)));
        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 200)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void given_sameTimeRelation_When_Compare_Then_ReturnsEquals() throws Exception {

        GameRelation gr1 = mock(GameRelation.class);
        GameRelation gr2 = mock(GameRelation.class);
        when(gr1.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 1000)));
        when(gr2.getCurrent()).thenReturn(Optional.of(RelationInterval.create(10, RelationInterval.RelationType.PENDING, 1000)));

        // Act
        int result = interactor.compareRelations(gr1, gr2);

        // Assert
        assertEquals(0, result);
    }
}