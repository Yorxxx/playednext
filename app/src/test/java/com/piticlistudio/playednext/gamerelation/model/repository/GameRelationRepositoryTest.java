package com.piticlistudio.playednext.gamerelation.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelation;
import com.piticlistudio.playednext.gamerelation.model.entity.GameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.RealmGameRelationMapper;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.gamerelation.model.repository.datasource.IGameRelationRepositoryDatasource;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases for GameRelationRepository
 * Created by jorge.garcia on 27/02/2017.
 */
public class GameRelationRepositoryTest extends BaseTest {

    @Mock
    GameRelationMapper mapper;

    @Mock
    RealmGameRelationMapper realmMapper;

    @Mock
    IGameRelationRepositoryDatasource<IGameRelationDatasource> localImpl;

    @InjectMocks
    GameRelationRepository repository;

    @Test
    public void Given_MapError_When_SavingData_Then_EmitsError() throws Exception {

        GameRelation data = GameRelation.create(Game.create(10, "title"), 1000);
        doReturn(Optional.absent()).when(realmMapper).transform(data);

        // Act
        TestObserver<GameRelation> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(Throwable.class);
    }

    @Test
    public void Given_MapErrorAfterSavingData_When_SavesData_Then_EmitsError() throws Exception {

        GameRelation data = GameRelation.create(Game.create(10, "title"), 1000);
        RealmGameRelation realmData = new RealmGameRelation();
        doReturn(Optional.of(realmData)).when(realmMapper).transform(data);
        doReturn(Optional.absent()).when(mapper).transform(realmData);
        doAnswer(invocation -> Single.just(invocation.getArguments()[0])).when(localImpl).save(any());

        // Act
        TestObserver<GameRelation> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(Throwable.class);
        verify(localImpl).save(realmData);
    }

    @Test
    public void Given_SaveError_When_SavingData_Then_EmitsError() throws Exception {

        GameRelation data = GameRelation.create(Game.create(10, "title"), 1000);
        RealmGameRelation realmData = new RealmGameRelation();
        doReturn(Optional.of(realmData)).when(realmMapper).transform(data);
        doReturn(Optional.of(data)).when(mapper).transform(realmData);
        Throwable error = new Exception("bla");
        doAnswer(__ -> Single.error(error)).when(localImpl).save(any());

        // Act
        TestObserver<GameRelation> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(error);
        verify(localImpl).save(realmData);
    }

    @Test
    public void Given_MappableEntity_When_SavingData_Then_SavesDataAndEmitsValue() throws Exception {

        GameRelation data = GameRelation.create(Game.create(10, "title"), 1000);
        RealmGameRelation realmData = new RealmGameRelation();
        doReturn(Optional.of(realmData)).when(realmMapper).transform(data);
        doReturn(Optional.of(data)).when(mapper).transform(realmData);

        doAnswer(invocation -> Single.just(invocation.getArguments()[0])).when(localImpl).save(any());

        // Act
        TestObserver<GameRelation> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertValue(data)
                .assertValueCount(1)
                .assertComplete()
                .assertNoErrors();
        verify(localImpl).save(realmData);
    }

    @Test
    public void Given_LoadError_When_LoadingData_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doAnswer(__ -> Single.error(error)).when(localImpl).load(10);

        // Act
        TestObserver<GameRelation> result = repository.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(error);
        verify(localImpl).load(10);
    }

    @Test
    public void Given_MapError_When_LoadingData_Then_EmitsMapError() throws Exception {

        RealmGameRelation realmData = new RealmGameRelation();
        doReturn(Optional.absent()).when(mapper).transform(realmData);
        doReturn(Single.just(realmData)).when(localImpl).load(10);

        // Act
        TestObserver<GameRelation> result = repository.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(Throwable.class);
        verify(localImpl).load(10);
    }

    @Test
    public void Given_LoadAndMapSuccess_When_LoadingData_Then_EmitsData() throws Exception {

        GameRelation data = GameRelation.create(Game.create(10, "title"), 1000);
        RealmGameRelation realmData = new RealmGameRelation();
        doReturn(Optional.of(realmData)).when(realmMapper).transform(data);
        doReturn(Optional.of(data)).when(mapper).transform(realmData);
        doReturn(Single.just(realmData)).when(localImpl).load(10);

        // Act
        TestObserver<GameRelation> result = repository.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertValueCount(1)
                .assertValue(data)
                .assertComplete()
                .assertNoErrors();
        verify(localImpl).load(10);
    }

    @Test
    public void given_loadError_When_LoadAll_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doAnswer(__ -> Observable.error(error)).when(localImpl).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = repository.loadAll().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoValues()
                .assertNotComplete()
                .assertError(error);
        verify(localImpl).loadAll();
    }

    @Test
    public void given_itemMapError_When_LoadAll_Then_FiltersItemAndEmitsRemaining() throws Exception {

        List<IGameRelationDatasource> datasourceList = new ArrayList<>();
        RealmGameRelation realmGameRelation1 = new RealmGameRelation();
        GameRelation relation1 = GameRelation.create(Game.create(0, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation2 = new RealmGameRelation();
        GameRelation relation2 = GameRelation.create(Game.create(1, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation3 = new RealmGameRelation();
        GameRelation relation3 = GameRelation.create(Game.create(2, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation4 = new RealmGameRelation();
        GameRelation relation4 = GameRelation.create(Game.create(3, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation5 = new RealmGameRelation();
        GameRelation relation5 = GameRelation.create(Game.create(4, "title"), System.currentTimeMillis());
        doReturn(Optional.of(relation1)).when(mapper).transform(realmGameRelation1);
        doReturn(Optional.of(relation2)).when(mapper).transform(realmGameRelation2);
        doReturn(Optional.absent()).when(mapper).transform(realmGameRelation3);
        doReturn(Optional.of(relation4)).when(mapper).transform(realmGameRelation4);
        doReturn(Optional.absent()).when(mapper).transform(realmGameRelation5);
        datasourceList.add(realmGameRelation1);
        datasourceList.add(realmGameRelation2);
        datasourceList.add(realmGameRelation3);
        datasourceList.add(realmGameRelation4);
        datasourceList.add(realmGameRelation5);

        when(localImpl.loadAll()).thenReturn(Observable.just(datasourceList).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<List<GameRelation>> result = repository.loadAll().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertValueCount(1)
                .assertNoErrors()
                .assertValue(check(values -> {
                    assertEquals(3, values.size());
                    assertTrue(values.contains(relation1));
                    assertTrue(values.contains(relation2));
                    assertFalse(values.contains(relation3));
                    assertTrue(values.contains(relation4));
                    assertFalse(values.contains(relation5));
                }));
    }

    @Test
    public void given_localImplementationEmitsMultipleTimes_When_loadsAll_Then_EmitsMultipleTimes() throws Exception {

        RealmGameRelation realmGameRelation1 = new RealmGameRelation();
        GameRelation relation1 = GameRelation.create(Game.create(0, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation2 = new RealmGameRelation();
        GameRelation relation2 = GameRelation.create(Game.create(1, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation3 = new RealmGameRelation();
        RealmGameRelation realmGameRelation4 = new RealmGameRelation();
        GameRelation relation4 = GameRelation.create(Game.create(3, "title"), System.currentTimeMillis());
        RealmGameRelation realmGameRelation5 = new RealmGameRelation();
        doReturn(Optional.of(relation1)).when(mapper).transform(realmGameRelation1);
        doReturn(Optional.of(relation2)).when(mapper).transform(realmGameRelation2);
        doReturn(Optional.absent()).when(mapper).transform(realmGameRelation3);
        doReturn(Optional.of(relation4)).when(mapper).transform(realmGameRelation4);
        doReturn(Optional.absent()).when(mapper).transform(realmGameRelation5);

        List<IGameRelationDatasource> firstEmissionList = new ArrayList<>();
        firstEmissionList.add(realmGameRelation1);

        List<IGameRelationDatasource> secondEmissionList = new ArrayList<>();
        secondEmissionList.add(realmGameRelation1);
        secondEmissionList.add(realmGameRelation2);
        secondEmissionList.add(realmGameRelation3);

        List<IGameRelationDatasource> thirdEmissionList = new ArrayList<>();
        thirdEmissionList.add(realmGameRelation1);
        thirdEmissionList.add(realmGameRelation2);
        thirdEmissionList.add(realmGameRelation3);
        thirdEmissionList.add(realmGameRelation4);
        thirdEmissionList.add(realmGameRelation5);

        doAnswer(invocation -> Observable.interval(1, TimeUnit.SECONDS)
                .take(3)
                .map(aLong -> {
                    if (aLong == 0)
                        return firstEmissionList;
                    if (aLong == 1)
                        return secondEmissionList;
                    return thirdEmissionList;
                })).when(localImpl).loadAll();

        // Act
        TestObserver<List<GameRelation>> result = repository.loadAll().test();
        result.awaitTerminalEvent();

        // Assert
        result.assertValueCount(3)
                .assertNoErrors()
                .assertValueAt(0, data -> data.size() == 1 && data.contains(relation1))
                .assertValueAt(1, data -> data.size() == 2 && data.contains(relation1) && data.contains(relation2))
                .assertValueAt(2, data -> data.size() == 3 && data.contains(relation1) && data.contains(relation2) && data.contains(relation4));

    }
}