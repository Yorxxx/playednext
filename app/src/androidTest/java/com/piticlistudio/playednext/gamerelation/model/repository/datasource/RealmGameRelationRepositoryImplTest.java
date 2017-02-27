package com.piticlistudio.playednext.gamerelation.model.repository.datasource;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.IGameRelationDatasource;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;

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
 * Test cases for RealmGameRelationRepository implementation
 * Created by jorge.garcia on 27/02/2017.
 */
public class RealmGameRelationRepositoryImplTest extends BaseAndroidTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private RealmGameRelationRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        File tempFolder = testFolder.newFolder("realmdata");
        RealmConfiguration config = new RealmConfiguration.Builder(tempFolder).build();
        Realm.setDefaultConfiguration(config);
        repository = new RealmGameRelationRepositoryImpl();
    }

    @Test
    public void Given_EmptyDatabase_When_LoadingRelation_Then_EmitsError() throws Exception {

        // Act
        TestObserver<IGameRelationDatasource> result = repository.load(10).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class);
        result.assertNoValues();
        result.assertNotComplete();
    }

    @Test
    public void Given_StoredEntity_When_RequestingRelation_Then_EmitsEntity() throws Exception {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmGameRelation data = realm.createObject(RealmGameRelation.class);
        RealmGame game = realm.createObject(RealmGame.class);
        game.setId(10);
        game.setName("title");
        data.setId(game.getId());
        data.setUpdatedAt(1000);
        data.setCreatedAt(1000);
        data.setGame(game);
        realm.commitTransaction();

        // Act
        TestObserver<IGameRelationDatasource> result = repository.load(data.getId()).test();

        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors();
        result.assertComplete();
        result.assertValueCount(1);
        result.assertValue(check(value -> {
            assertEquals(data.getId(), value.getId());
            assertEquals(data.getUpdatedAt(), value.getUpdatedAt());
            assertEquals(data.getCreatedAt(), value.getCreatedAt());
            assertEquals(data.getGame().getId(), value.getGame().getId());
            assertEquals(data.getGame().getName(), value.getGame().getName());
        }));
    }

    @Test
    public void Given_GameRelationEntity_When_SavesOnDatabase_Then_PersistsData() throws Exception {

        Realm realm = Realm.getDefaultInstance();
        RealmGameRelation data = new RealmGameRelation();
        RealmGame game = GameFactory.provideRealmGame(10, "title");
        data.setId(game.getId());
        data.setUpdatedAt(1000);
        data.setCreatedAt(1000);
        data.setGame(game);
        assertEquals(0, realm.where(RealmGameRelation.class).count());
        realm.close();

        // Act
        TestObserver<IGameRelationDatasource> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValue(data);
        realm = Realm.getDefaultInstance();
        assertEquals(1, realm.where(RealmGameRelation.class).count());
        realm.close();

    }
}