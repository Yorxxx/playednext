package com.piticlistudio.playednext.collection.model.repository;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.CollectionComponent;
import com.piticlistudio.playednext.collection.CollectionModule;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.IGDBCollection;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.collection.model.repository.datasource.ICollectionRepositoryDatasource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import it.cosenonjaviste.daggermock.InjectFromComponent;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test caes for Collection repository implementation
 * Created by jorge.garcia on 10/02/2017.
 */
public class CollectionRepositoryTest extends BaseTest {

    private final ICollectionData localData = new RealmCollection(100, "name");
    private final ICollectionData remoteData = IGDBCollection.create(100, "name", "url", 1000, 2000, new ArrayList<>());
    @Rule
    public DaggerMockRule<CollectionComponent> rule = new DaggerMockRule<>(CollectionComponent.class, new CollectionModule());
    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    @Named("db")
    ICollectionRepositoryDatasource dbSource;
    @Mock
    @Named("net")
    ICollectionRepositoryDatasource netSource;
    @InjectFromComponent
    private ICollectionRepository repository;

    @Before
    public void setUp() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void load_localDatasource() throws Exception {

        when(dbSource.load(anyInt())).thenReturn(Single.just(localData).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors();
        result.assertValueCount(1);
        result.assertComplete();
        result.assertValue(check(gameCollection -> {
            assertEquals(localData.getId(), gameCollection.id());
            assertEquals(localData.getName(), gameCollection.name());
        }));
        verifyZeroInteractions(netSource);
        verify(dbSource).load(0);
    }

    @Test
    public void load_localDatasource_mapError() throws Exception {

        ICollectionData data = new RealmCollection();
        when(dbSource.load(anyInt())).thenReturn(Single.just(data).delay(2, TimeUnit.SECONDS));
        when(netSource.load(anyInt())).thenReturn(Single.just(remoteData).delay(10, TimeUnit.SECONDS));

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
        result.assertValue(check(gameCollection -> {
            assertEquals(remoteData.getId(), gameCollection.id());
            assertEquals(remoteData.getName(), gameCollection.name());
        }));
        verify(netSource).load(0);
        verify(dbSource).load(0);
    }

    @Test
    public void load_remoteDatasource() throws Exception {

        Throwable error = new Exception("Not found");
        when(dbSource.load(anyInt())).thenReturn(Single.error(error));
        when(netSource.load(anyInt())).thenReturn(Single.just(remoteData).delay(2, TimeUnit.SECONDS));

        TestObserver<Collection> result = repository.load(0).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors();
        result.assertValueCount(1);
        result.assertComplete();
        result.assertValue(check(gameCollection -> {
            assertEquals(remoteData.getId(), gameCollection.id());
            assertEquals(remoteData.getName(), gameCollection.name());
        }));
        verify(netSource).load(0);
        verify(dbSource).load(0);
    }


}