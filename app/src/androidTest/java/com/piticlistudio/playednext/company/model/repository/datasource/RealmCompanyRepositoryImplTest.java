package com.piticlistudio.playednext.company.model.repository.datasource;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for RealmCompanyRepository
 * Created by jorge.garcia on 14/02/2017.
 */
public class RealmCompanyRepositoryImplTest extends BaseAndroidTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private RealmCompanyRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        File tempFolder = testFolder.newFolder("realmdata");
        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm.setDefaultConfiguration(config);
        repository = new RealmCompanyRepositoryImpl();
    }

    @Test
    public void load() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmCompany data = realm.createObject(RealmCompany.class);
        data.setName("name");
        data.setId(0);
        realm.commitTransaction();

        // Act
        TestObserver<ICompanyData> result = repository.load(data.getId()).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors();
        result.assertComplete();
        result.assertValueCount(1);
        result.assertValue(check(value -> {
            assertEquals(data.getId(), value.getId());
            assertEquals(data.getName(), value.getName());
        }));
    }

    @Test
    public void load_notFound() throws Exception {
        // Act
        TestObserver<ICompanyData> result = repository.load(10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
        result.assertNotComplete();
    }

    @Test
    public void save() throws Exception {

        RealmCompany data = new RealmCompany(50, "name");

        // Act
        TestObserver<Void> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete();

    }
}