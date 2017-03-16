package com.piticlistudio.playednext.game.model.repository;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.TestSchedulerRule;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.repository.ICollectionRepository;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.company.model.repository.ICompanyRepository;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.game.model.repository.datasource.IGamedataRepository;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.genre.model.repository.IGenreRepository;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.platform.model.repository.IPlatformRepository;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDate;
import com.piticlistudio.playednext.releasedate.model.entity.ReleaseDateMapper;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.realm.RealmList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Test cases for Game
 * Created by jorge.garcia on 10/02/2017.
 */
public class GameRepositoryTest extends BaseGameTest {

    @Rule
    public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();
    @Mock
    IGamedataRepository dataRepository;
    @Mock
    ICollectionRepository collectionRepository;
    @Mock
    ICompanyRepository companyRepository;
    @Mock
    IGenreRepository genreRepository;
    @Mock
    IPlatformRepository platformRepository;
    @Mock
    ReleaseDateMapper dateMapper;
    @Mock
    GameMapper mapper;
    @Mock
    RealmGameMapper realmGameMapper;

    @InjectMocks
    private GameRepository repository;

    private RealmGame localData = GameFactory.provideRealmGame(50, "title");

    @Before
    public void setUp() throws Exception {
        assertNotNull(repository);
    }

    @Test
    public void Given_RealmMapError_When_Save_Then_EmitsError() throws Exception {

        Game data = GameFactory.provide(50, "name");
        when(realmGameMapper.transform(data)).thenReturn(Optional.absent());

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verifyZeroInteractions(dataRepository);
    }

    @Test
    public void Given_SaveError_When_Save_Then_EmitsError() throws Exception {

        Game data = GameFactory.provide(50, "name");
        RealmGame expectedData = GameFactory.provideRealmGame(50, "name");
        when(realmGameMapper.transform(data)).thenReturn(Optional.of(expectedData));

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(dataRepository).save(expectedData);

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verify(dataRepository).save(expectedData);
        verify(realmGameMapper).transform(data);
    }

    @Test
    public void Given_MapError_When_Save_Then_EmitsError() throws Exception {

        Game data = GameFactory.provide(50, "name");
        RealmGame expectedData = GameFactory.provideRealmGame(50, "name");
        when(realmGameMapper.transform(data)).thenReturn(Optional.of(expectedData));
        when(mapper.transform(expectedData)).thenReturn(Optional.absent());

        doAnswer(invocation -> Observable.just(invocation.getArguments()[0])).when(dataRepository).save(any());

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verify(dataRepository).save(expectedData);
        verify(realmGameMapper).transform(data);
        verify(mapper).transform(expectedData);
    }

    @Test
    public void Given_Success_When_Save_Then_EmitsData() throws Exception {

        Game data = GameFactory.provide(50, "name");
        RealmGame expectedData = GameFactory.provideRealmGame(50, "name");
        when(realmGameMapper.transform(data)).thenReturn(Optional.of(expectedData));
        when(mapper.transform(expectedData)).thenReturn(Optional.of(data));

        doAnswer(invocation -> Observable.just(invocation.getArguments()[0])).when(dataRepository).save(any());

        // Act
        TestObserver<Game> result = repository.save(data).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertValue(data)
                .assertComplete()
                .assertValueCount(1)
                .assertNoErrors();
        verify(dataRepository).save(expectedData);
        verify(realmGameMapper).transform(data);
        verify(mapper).transform(expectedData);
    }

    @Test
    public void Given_RepositoryError_When_Load_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(dataRepository).load(10);

        // Act
        TestObserver<Game> result = repository.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verify(dataRepository).load(10);
        verifyZeroInteractions(mapper);
    }

    @Test
    public void Given_MapError_When_Load_Then_EmitsError() throws Exception {

        RealmGame data = GameFactory.provideRealmGame(10, "title");
        doReturn(Observable.just(data)).when(dataRepository).load(10);

        when(mapper.transform(data)).thenReturn(Optional.absent());

        // Act
        TestObserver<Game> result = repository.load(10).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertError(Throwable.class)
                .assertNoValues()
                .assertNotComplete();
        verify(dataRepository).load(10);
        verify(mapper).transform(data);
    }

    @Test
    public void Given_AggregatedData_When_Load_Then_EmitsMultipleTimesWithAggregatedData() throws Exception {

        final int gameId = 50;

        Game expected = GameFactory.provide(gameId, "title");
        IGameDatasource source = GameFactory.provideNetGame(gameId, "title");
        assertTrue(source.getCollection().isPresent());
        assertFalse(source.getCollection().get().data.isPresent());
        assertEquals(3, source.getDevelopers().size());
        assertEquals(3, source.getPublishers().size());
        assertEquals(3, source.getGenres().size());

        Collection collection = Collection.create(10, "name");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.just(collection).delay(5, TimeUnit.SECONDS));
        when(dataRepository.load(source.getId())).thenReturn(Observable.just(source).delay(2, TimeUnit.SECONDS));

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Company.create(id, "company_" + id)).delay(10, TimeUnit.SECONDS);
        }).when(companyRepository).load(anyInt());

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Genre.create(id, "genre_" + id)).delay(15, TimeUnit.SECONDS);
        }).when(genreRepository).load(anyInt());

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id)).delay(20, TimeUnit.SECONDS);
        }).when(platformRepository).load(anyInt());

        doReturn(Optional.of(ReleaseDate.create(10, "date"))).when(dateMapper).transform(any());

        doAnswer(invocation -> {
            IGameDatasource source1 = (IGameDatasource) invocation.getArguments()[0];
            Game game = Game.create(gameId, "title");
            if (source1.getCollection().isPresent() && source1.getCollection().get().data.isPresent()) {
                ICollectionData data = source1.getCollection().get().data.get();
                game.collection = Optional.of(Collection.create(data.getId(), data.getName()));
            }
            List<Company> developers = new ArrayList<>();
            for (NetworkEntityIdRelation<ICompanyData> company : source1.getDevelopers()) {
                if (company.data.isPresent()) {
                    ICompanyData data = company.getData();
                    developers.add(Company.create(data.getId(), data.getName()));
                }
            }
            game.developers = developers;

            List<Company> publishers = new ArrayList<>();
            for (NetworkEntityIdRelation<ICompanyData> company : source1.getDevelopers()) {
                if (company.data.isPresent()) {
                    ICompanyData data = company.getData();
                    publishers.add(Company.create(data.getId(), data.getName()));
                }
            }
            game.publishers = publishers;

            List<Genre> genres = new ArrayList<>();
            for (NetworkEntityIdRelation<IGenreData> genre : source1.getGenres()) {
                if (genre.data.isPresent()) {
                    IGenreData data = genre.getData();
                    genres.add(Genre.create(data.getId(), data.getName()));
                }
            }
            game.genres = genres;

            List<Platform> platforms = new ArrayList<>();
            for (NetworkEntityIdRelation<IPlatformData> platform : source1.getPlatforms()) {
                if (platform.data.isPresent()) {
                    IPlatformData data = platform.getData();
                    platforms.add(Platform.create(data.getId(), data.getName()));
                }
            }
            game.platforms = platforms;
            return Optional.of(game);
        }).when(mapper).transform(source);

        // Act
        TestObserver<Game> result = repository.load(gameId).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(3, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(1)
                .assertValue(check(game -> assertFalse(game.collection.isPresent())));

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(2)
                .assertValueAt(1, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                }));


        verify(collectionRepository).load(source.getCollection().get().id);

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Check if developers and publishers have been loaded
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(4)
                .assertValueAt(3, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                }));
        verify(companyRepository, times(6)).load(anyInt());


        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Check if genres have been loaded
        result.assertNoErrors()
                .assertNotComplete()
                .assertValueCount(5)
                .assertValueAt(4, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                    assertEquals(source.getGenres().size(), game.genres.size());
                }));
        verify(genreRepository, times(3)).load(anyInt());

        // Advance time
        testSchedulerRule.getTestScheduler().advanceTimeBy(6, TimeUnit.SECONDS);

        // Check if genres have been loaded
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(7)
                .assertValueAt(6, check(game -> {
                    assertTrue(game.collection.isPresent());
                    assertEquals(collection, game.collection.get());
                    assertEquals(source.getDevelopers().size(), game.developers.size());
                    assertEquals(source.getPublishers().size(), game.publishers.size());
                    assertEquals(source.getGenres().size(), game.genres.size());
                    assertEquals(source.getPlatforms().size(), game.platforms.size());
                    assertEquals(source.getReleases().size(), game.releases.size());
                }));
        verify(platformRepository, times(source.getPlatforms().size() + source.getReleases().size())).load(anyInt());
    }

    @Test
    public void Given_SearchError_When_Search_Then_EmitsError() throws Exception {

        Throwable error = new Exception("bla");
        doReturn(Observable.error(error)).when(dataRepository).search(anyString(), anyInt(), anyInt());

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertError(error)
                .assertNoValues()
                .assertNotComplete();
    }

    @Test
    public void Given_MapError_When_Search_Then_FiltersOutInvalidMaps() throws Exception {

        List<IGameDatasource> data = new ArrayList<>();
        IGameDatasource valid1 = GameFactory.provideNetGame(1, "title1");
        data.add(valid1);
        IGameDatasource valid2 = GameFactory.provideNetGame(2, "title2");
        data.add(valid2);
        RealmGame invalid = new RealmGame();
        data.add(invalid);
        data.add(invalid);
        data.add(invalid);
        data.add(invalid);
        Game expected1 = GameFactory.provide(1, "title1");
        Game expected2 = GameFactory.provide(2, "title2");
        when(dataRepository.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));
        when(mapper.transform(invalid)).thenReturn(Optional.absent());
        when(mapper.transform(valid1)).thenReturn(Optional.of(expected1));
        when(mapper.transform(valid2)).thenReturn(Optional.of(expected2));

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(games -> {
                    assertEquals(2, games.size());
                    assertTrue(games.contains(expected1));
                    assertTrue(games.contains(expected2));
                }));
    }

    @Test
    public void Given_Success_When_Search_Then_EmitsListWithAllData() throws Exception {
        List<IGameDatasource> data = new ArrayList<>();
        IGameDatasource valid1 = GameFactory.provideNetGame(1, "title1");
        data.add(valid1);
        IGameDatasource valid2 = GameFactory.provideNetGame(2, "title2");
        data.add(valid2);
        IGameDatasource valid3 = GameFactory.provideNetGame(3, "title3");
        data.add(valid3);
        Game expected1 = GameFactory.provide(1, "title1");
        Game expected2 = GameFactory.provide(2, "title2");
        Game expected3 = GameFactory.provide(3, "title3");
        when(dataRepository.search(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(data).delay(2, TimeUnit.SECONDS));
        when(mapper.transform(valid1)).thenReturn(Optional.of(expected1));
        when(mapper.transform(valid2)).thenReturn(Optional.of(expected2));
        when(mapper.transform(valid3)).thenReturn(Optional.of(expected3));

        // Act
        TestObserver<List<Game>> result = repository.search("1", 0, 10).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(check(games -> {
                    assertEquals(3, games.size());
                    assertTrue(games.contains(expected1));
                    assertTrue(games.contains(expected2));
                    assertTrue(games.contains(expected3));
                }));

    }

    @Test
    public void Given_noCollection_When_loadCollection_Then_EmitsNothing() throws Exception {

        IGDBGame source = GameFactory.provideNetGame(10, "title");
        source.collection = -1;
        Game to = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(collectionRepository);
    }

    @Test
    public void Given_CollectionAlreadyLoaded_When_loadCollection_Then_EmitsNothing() throws Exception {

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        assertTrue(source.getCollection().isPresent());
        assertTrue(source.getCollection().get().data.isPresent());

        Game to = Game.create(10, "title");
        Collection collection = Collection.create(11, "collection");
        to.collection = Optional.of(collection);

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(collectionRepository);
    }

    @Test
    public void Given_CollectionAvailableToLoad_When_loadCollection_Then_EmitsCollection() throws Exception {

        IGDBGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getCollection().isPresent());
        assertFalse(source.getCollection().get().data.isPresent());

        Game to = Game.create(10, "title");

        Collection collection = Collection.create(10, "name");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.just(collection).delay(1, TimeUnit.SECONDS));

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertTrue(to.collection.isPresent());
        assertEquals(collection, to.collection.get());
        verify(collectionRepository).load(source.getCollection().get().id);
    }

    @Test
    public void Given_LoadError_When_LoadCollection_Then_EmitsError() throws Exception {

        IGDBGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getCollection().isPresent());
        assertFalse(source.getCollection().get().data.isPresent());

        Game to = Game.create(10, "title");

        Throwable error = new Exception("bla");
        when(collectionRepository.load(anyInt())).thenReturn(Observable.error(error));

        // Act
        TestObserver<Game> result = repository.loadCollection(source, to).test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(5, TimeUnit.SECONDS);

        // Assert
        result.assertError(error)
                .assertNoValues()
                .assertNotComplete();
    }

    @Test
    public void Given_emptyCompanies_When_LoadMissingCompanies_Then_EmitsNothing() throws Exception {

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(new ArrayList<>(), new ArrayList<>()).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void Given_LoadedCompanies_When_LoadMissingCompanies_Then_EmitsNothing() throws Exception {

        List<Company> loaded = new ArrayList<>();
        Company dev1 = Company.create(1, "name1");
        Company dev2 = Company.create(2, "name2");
        loaded.add(dev1);
        loaded.add(dev2);

        List<NetworkEntityIdRelation<ICompanyData>> request = new ArrayList<>();
        request.add(new NetworkEntityIdRelation<>(1, Optional.of(new RealmCompany(1, "name1"))));
        request.add(new NetworkEntityIdRelation<>(2, Optional.of(new RealmCompany(2, "name2"))));

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(request, loaded).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(companyRepository);
    }

    @Test
    public void Given_NotLoadedCompanies_When_LoadMissingCompanies_Then_LoadsMissing() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Company.create(id, "company_" + id));
        }).when(companyRepository).load(anyInt());

        List<Company> loaded = new ArrayList<>();
        Company dev1 = Company.create(1, "name1");
        Company dev2 = Company.create(2, "name2");
        loaded.add(dev1);
        loaded.add(dev2);

        List<NetworkEntityIdRelation<ICompanyData>> request = new ArrayList<>();
        request.add(new NetworkEntityIdRelation<>(1, Optional.of(new RealmCompany(1, "name1"))));
        request.add(new NetworkEntityIdRelation<>(2, Optional.of(new RealmCompany(2, "name2"))));
        request.add(new NetworkEntityIdRelation<>(3, Optional.absent()));
        request.add(new NetworkEntityIdRelation<>(4, Optional.absent()));

        // Act
        TestObserver<List<Company>> result = repository.loadMissingCompanies(request, loaded).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertComplete()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(check(data -> {
                    assertEquals(2, data.size());
                    assertEquals(3, data.get(0).id());
                    assertEquals(4, data.get(1).id());
                }));

        verify(companyRepository, times(2)).load(anyInt());
    }

    @Test
    public void Given_NoGenres_When_LoadGenres_Then_EmitsNothing() throws Exception {

        IGDBGame from = GameFactory.provideNetGame(10, "title");
        from.genres.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadGenres(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(genreRepository);
    }

    @Test
    public void Given_GenresLoaded_When_LoadGenres_Then_EmitsNothing() throws Exception {
        Game to = Game.create(10, "title");
        List<Genre> genres = new ArrayList<>();
        Genre genre1 = Genre.create(1, "name1");
        Genre genre2 = Genre.create(2, "name2");
        genres.add(genre1);
        genres.add(genre2);
        to.genres = genres;

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmGenre> realmGenres = new RealmList<>();
        realmGenres.add(new RealmGenre(1, "name1"));
        realmGenres.add(new RealmGenre(2, "name2"));
        source.setGenres(realmGenres);

        // Act
        TestObserver<Game> result = repository.loadGenres(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(genreRepository);
    }

    @Test
    public void Given_NotLoadedGenres_When_loadGenre_Then_RequestsGenre() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Genre.create(id, "genre_" + id));
        }).when(genreRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.genres = new ArrayList<>();

        IGDBGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getGenres().size() > 0);

        // Act
        TestObserver<Game> result = repository.loadGenres(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.genres.size(), source.getGenres().size());
        verify(genreRepository, times(source.getGenres().size())).load(anyInt());
    }

    @Test
    public void Given_NoPlatforms_When_loadPlatforms_Then_EmitsNothing() throws Exception {

        IGDBGame from = GameFactory.provideNetGame(10, "title");
        from.release_dates.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadPlatforms(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void Given_PlatformsLoaded_When_loadPlatforms_Then_EmitsNothing() throws Exception {
        Game to = Game.create(10, "title");
        List<Platform> platforms = new ArrayList<>();
        Platform platform1 = Platform.create(1, "name1");
        Platform platform2 = Platform.create(2, "name2");
        platforms.add(platform1);
        platforms.add(platform2);
        to.platforms = platforms;

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmPlatform> realmPlatforms = new RealmList<>();
        realmPlatforms.add(new RealmPlatform(1, "name1"));
        realmPlatforms.add(new RealmPlatform(2, "name2"));
        source.setPlatforms(realmPlatforms);

        // Act
        TestObserver<Game> result = repository.loadPlatforms(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void Given_LoadablePlatforms_When_loadPlatforms_Then_RequestsPlatform() throws Exception {

        Game to = Game.create(10, "title");
        to.platforms = new ArrayList<>();

        IGDBGame source = GameFactory.provideNetGame(10, "title");
        assertTrue(source.getPlatforms().size() > 0);

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id));
        }).when(platformRepository).load(anyInt());

        // Act
        TestObserver<Game> result = repository.loadPlatforms(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.platforms.size(), source.getPlatforms().size());
        verify(platformRepository, times(source.getPlatforms().size())).load(anyInt());
    }

    @Test
    public void Given_noReleases_When_loadReleases_Then_EmitsNothing() throws Exception {

        IGDBGame from = GameFactory.provideNetGame(10, "title");
        from.release_dates.clear();
        Game dest = Game.create(10, "title");

        // Act
        TestObserver<Game> result = repository.loadReleases(from, dest).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertNoValues();
        verifyZeroInteractions(platformRepository);
    }

    @Test
    public void Given_LoadableReleases_When_loadReleases_Then_requestsReleases() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id));
        }).when(platformRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.releases = new ArrayList<>();

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmGameRelease> realmReleases = new RealmList<>();
        RealmPlatform realmPlatform1 = new RealmPlatform(1, "name1");
        RealmReleaseDate realmDate1 = new RealmReleaseDate("human-date", 1000);
        RealmPlatform realmPlatform2 = new RealmPlatform(2, "name2");
        RealmReleaseDate realmDate2 = new RealmReleaseDate("human-date", 1000);
        realmReleases.add(new RealmGameRelease(realmPlatform1, realmDate1));
        realmReleases.add(new RealmGameRelease(realmPlatform2, realmDate2));
        source.setReleases(realmReleases);
        when(dateMapper.transform(realmDate1)).thenReturn(Optional.of(ReleaseDate.create(1000, "human-date")));
        when(dateMapper.transform(realmDate2)).thenReturn(Optional.of(ReleaseDate.create(1000, "human-date")));


        // Act
        TestObserver<Game> result = repository.loadReleases(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(to.releases.size(), source.getReleases().size());
        verify(platformRepository, times(source.getReleases().size())).load(anyInt());
    }

    @Test
    public void given_releaseMapError_When_loadRelease_Then_RemovesInvalidReleaseFromList() throws Exception {

        doAnswer(invocation -> {
            int id = (int) invocation.getArguments()[0];
            return Observable.just(Platform.create(id, "platform_" + id));
        }).when(platformRepository).load(anyInt());

        Game to = Game.create(10, "title");
        to.releases = new ArrayList<>();

        RealmGame source = GameFactory.provideRealmGame(10, "title");
        RealmList<RealmGameRelease> realmReleases = new RealmList<>();
        RealmPlatform realmPlatform1 = new RealmPlatform(1, "name1");
        RealmReleaseDate realmDate1 = new RealmReleaseDate(null, 0); // Invalid release date
        RealmPlatform realmPlatform2 = new RealmPlatform(2, "name2");
        RealmReleaseDate realmDate2 = new RealmReleaseDate("human-date", 1000);
        realmReleases.add(new RealmGameRelease(realmPlatform1, realmDate1));
        realmReleases.add(new RealmGameRelease(realmPlatform2, realmDate2));
        source.setReleases(realmReleases);
        when(dateMapper.transform(realmDate1)).thenReturn(Optional.absent());
        when(dateMapper.transform(realmDate2)).thenReturn(Optional.of(ReleaseDate.create(1000, "human-date")));

        // Act
        TestObserver<Game> result = repository.loadReleases(source, to).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(to);

        assertEquals(1, to.releases.size());
        verify(platformRepository, times(2)).load(anyInt());
        assertEquals(1, to.releases.size());
        assertEquals(2, to.releases.get(0).platform().id());
        assertEquals("human-date", to.releases.get(0).releaseDate().humanDate());
    }
}