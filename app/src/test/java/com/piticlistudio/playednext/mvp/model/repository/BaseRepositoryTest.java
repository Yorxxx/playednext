package com.piticlistudio.playednext.mvp.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.IGDBCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.mvp.model.entity.Mapper;
import com.piticlistudio.playednext.mvp.model.repository.datasource.BaseRepositoryDataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test cases for BaseRepository
 * Created by jorge.garcia on 13/02/2017.
 */
public class BaseRepositoryTest extends BaseTest {

    private final ICollectionData localData = new RealmCollection(100, "name");
    private final ICollectionData remoteData = IGDBCollection.create(100, "name", "url", 1000, 2000, new ArrayList<>());
    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    BaseRepositoryDataSource<ICollectionData> dbImpl;
    @Mock
    BaseRepositoryDataSource<ICollectionData> netImpl;
    @Mock
    Mapper<Collection, ICollectionData> mapper;

    // TODO injectMocks does not work with @Named??
    BaseRepository<Collection, ICollectionData> repository;

    @Before
    public void setUp() throws Exception {

        repository = new BaseRepository<>(dbImpl, netImpl, mapper);

        doAnswer(invocation -> {
            ICollectionData request = (ICollectionData) invocation.getArguments()[0];
            return Optional.of(Collection.create(request.getId(), request.getName()));
        }).when(mapper).transform(any());
    }

    @Test
    public void load() throws Exception {

        when(dbImpl.load(anyInt())).thenReturn(Single.just(localData).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors();
        result.assertValueCount(1);
        result.assertComplete();
        verifyZeroInteractions(netImpl);
        verify(mapper).transform(localData);
        verify(dbImpl).load(0);
    }

    @Test
    public void load_error() throws Exception {

        Throwable error = new Exception("Not found");
        when(dbImpl.load(anyInt())).thenReturn(Single.error(error));
        when(netImpl.load(anyInt())).thenReturn(Single.just(remoteData).delay(2, TimeUnit.SECONDS));

        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors();
        result.assertValueCount(1);
        result.assertComplete();
        verify(mapper).transform(remoteData);
        verify(netImpl).load(0);
        verify(dbImpl).load(0);
    }

    @Test
    public void load_localData_mapError() throws Exception {

        doAnswer(invocation -> Optional.absent()).when(mapper).transform(localData);

        doAnswer(invocation -> {
            ICollectionData request = (ICollectionData) invocation.getArguments()[0];
            return Optional.of(Collection.create(request.getId(), request.getName()));
        }).when(mapper).transform(remoteData);

        when(dbImpl.load(anyInt())).thenReturn(Single.just(localData).delay(2, TimeUnit.SECONDS));
        when(netImpl.load(anyInt())).thenReturn(Single.just(remoteData).delay(10, TimeUnit.SECONDS));

        // Act
        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        // Check that error from dbsource is not propagated
        result.assertNoErrors();
        result.assertNoValues();
        result.assertNotComplete();

        // Act
        testSchedulerRule.getTestScheduler().advanceTimeBy(20, TimeUnit.SECONDS);

        // Assert
        // Check final emission
        result.assertNoErrors();
        result.assertValueCount(1);
        result.assertComplete();
        verify(netImpl).load(0);
        verify(dbImpl).load(0);
        verify(mapper).transform(localData);
        verify(mapper).transform(remoteData);
    }

    @Test
    public void load_netMapError() throws Exception {

        doAnswer(invocation -> Optional.absent()).when(mapper).transform(remoteData);

        Throwable error = new Exception("Not found");
        when(dbImpl.load(anyInt())).thenReturn(Single.error(error));
        when(netImpl.load(anyInt())).thenReturn(Single.just(remoteData).delay(10, TimeUnit.SECONDS));

        // Act
        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        // Check that error from dbsource is not propagated
        result.assertNoErrors();
        result.assertNoValues();
        result.assertNotComplete();

        // Act
        testSchedulerRule.getTestScheduler().advanceTimeBy(20, TimeUnit.SECONDS);

        // Assert
        // Check final emission
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verify(netImpl).load(0);
        verify(dbImpl).load(0);
        verify(mapper).transform(remoteData);
    }
}