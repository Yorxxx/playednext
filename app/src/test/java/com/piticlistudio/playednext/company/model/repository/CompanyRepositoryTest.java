package com.piticlistudio.playednext.company.model.repository;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.company.model.CompanyComponent;
import com.piticlistudio.playednext.company.model.CompanyModule;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.IGDBCompany;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.datasource.ICompanyRepositoryDataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

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
 * Test cases
 * Created by jorge.garcia on 13/02/2017.
 */
public class CompanyRepositoryTest extends BaseTest {


    private final ICompanyData localData = new RealmCompany(100, "name");
    private final ICompanyData remoteData = IGDBCompany.create(100, "name", "url", "slug", 1000, 2000);
    @Rule
    public DaggerMockRule<CompanyComponent> rule = new DaggerMockRule<>(CompanyComponent.class, new CompanyModule());
    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    @Named("db")
    ICompanyRepositoryDataSource dbSource;
    @Mock
    @Named("net")
    ICompanyRepositoryDataSource netSource;
    @InjectFromComponent
    private ICompanyRepository repository;

    @Before
    public void setUp() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void load_localDatasource() throws Exception {

        when(dbSource.load(anyInt())).thenReturn(Single.just(localData).delay(2, TimeUnit.SECONDS));

        // Act
        TestObserver<Company> result = repository.load(0).test();
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

        ICompanyData data = new RealmCompany();
        when(dbSource.load(anyInt())).thenReturn(Single.just(data).delay(2, TimeUnit.SECONDS));
        when(netSource.load(anyInt())).thenReturn(Single.just(remoteData).delay(10, TimeUnit.SECONDS));

        // Act
        TestObserver<Company> result = repository.load(0).test();
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

        TestObserver<Company> result = repository.load(0).test();
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