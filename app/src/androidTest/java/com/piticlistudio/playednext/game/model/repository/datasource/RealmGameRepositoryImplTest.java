package com.piticlistudio.playednext.game.model.repository.datasource;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import io.reactivex.observers.TestObserver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmGameRepositoryImplTest extends BaseAndroidTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private RealmGameRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        File tempFolder = testFolder.newFolder("realmdata");
        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm.setDefaultConfiguration(config);
        repository = new RealmGameRepositoryImpl();
    }

    @Test
    public void load() throws Exception {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmGame data = realm.createObject(RealmGame.class);
        data.setName("name");
        data.setId(0);
        realm.commitTransaction();

        // Act
        TestObserver<IGameDatasource> result = repository.load(data.getId()).test();

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
        TestObserver<IGameDatasource> result = repository.load(10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
        result.assertNotComplete();
    }

    @Test
    public void given_nullSearchQuery_When_search_Then_RequestsEmptyString() throws Exception {

        RealmGame data = GameFactory.provideRealmGame(0, "name1");
        RealmGame data2 = GameFactory.provideRealmGame(1, "name2");
        RealmGame data3 = GameFactory.provideRealmGame(2, "another");
        Realm.getDefaultInstance().beginTransaction();
        Realm.getDefaultInstance().copyToRealmOrUpdate(data);
        Realm.getDefaultInstance().copyToRealmOrUpdate(data2);
        Realm.getDefaultInstance().copyToRealmOrUpdate(data3);
        Realm.getDefaultInstance().commitTransaction();

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search(null, 0, 10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(realmGames -> {
                    assertEquals(3, realmGames.size());
                }));
    }

    @Test
    public void search() throws Exception {
        RealmGame data = GameFactory.provideRealmGame(0, "name1");
        RealmGame data2 = GameFactory.provideRealmGame(1, "name2");
        RealmGame data3 = GameFactory.provideRealmGame(2, "another");
        Realm.getDefaultInstance().beginTransaction();
        Realm.getDefaultInstance().copyToRealmOrUpdate(data);
        Realm.getDefaultInstance().copyToRealmOrUpdate(data2);
        Realm.getDefaultInstance().copyToRealmOrUpdate(data3);
        Realm.getDefaultInstance().commitTransaction();

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("na", 0, 10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(realmGames -> {
                    assertEquals(2, realmGames.size());
                    assertEquals(data.getId(), realmGames.get(0).getId());
                    assertEquals(data.getName(), realmGames.get(0).getName());
                    assertEquals(data2.getId(), realmGames.get(1).getId());
                    assertEquals(data2.getName(), realmGames.get(1).getName());
                    for (IGameDatasource realmGame : realmGames) {
                        assertTrue(realmGame.getName().startsWith("na"));
                    }
                }));
    }

    @Test
    public void given_offset_when_search_then_skipsInitialItems() throws Exception {
        Realm.getDefaultInstance().beginTransaction();
        for (int i = 0; i < 50; i++) {
            RealmGame data = GameFactory.provideRealmGame(i, "name" + i);
            Realm.getDefaultInstance().copyToRealmOrUpdate(data);
        }
        Realm.getDefaultInstance().commitTransaction();

        // Act
        TestObserver<List<IGameDatasource>> result = repository.search("na", 10, 100).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(realmGames -> {
                    assertEquals(40, realmGames.size());
                    for (IGameDatasource realmGame : realmGames) {
                        for (int i = 0; i < 10; i++) {
                            assertFalse(i == realmGame.getId());
                        }
                        assertTrue(realmGame.getName().startsWith("na"));
                    }
                }));
    }

    @Test
    public void save() throws Exception {

        RealmGame data = GameFactory.provideRealmGame(50, "name");

        // Act
        TestObserver<Void> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete();

    }
}