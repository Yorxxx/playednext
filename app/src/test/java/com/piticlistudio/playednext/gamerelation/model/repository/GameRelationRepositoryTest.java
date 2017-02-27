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

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
}