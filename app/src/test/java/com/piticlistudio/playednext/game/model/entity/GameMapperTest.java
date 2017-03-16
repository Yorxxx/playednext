package com.piticlistudio.playednext.game.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.collection.model.entity.Collection;
import com.piticlistudio.playednext.collection.model.entity.CollectionMapper;
import com.piticlistudio.playednext.collection.model.entity.datasource.ICollectionData;
import com.piticlistudio.playednext.collection.model.entity.datasource.RealmCollection;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.company.model.entity.CompanyMapper;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.game.model.BaseGameTest;
import com.piticlistudio.playednext.game.model.entity.datasource.IGDBGame;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelease.model.entity.GameReleaseMapper;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.genre.model.entity.GenreMapper;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.image.model.entity.ImageDataMapper;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.Platform;
import com.piticlistudio.playednext.platform.model.entity.PlatformMapper;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Mapper
 * Created by jorge.garcia on 10/02/2017.
 */
public class GameMapperTest extends BaseGameTest {

    @Mock
    CollectionMapper collectionMapper;
    @Mock
    ImageDataMapper imageMapper;
    @Mock
    CompanyMapper companyMapper;
    @Mock
    GenreMapper genreMapper;
    @Mock
    GameReleaseMapper releaseMapper;
    @Mock
    PlatformMapper platformMapper;

    @InjectMocks
    private GameMapper mapper;

    @Test
    public void given_gameWithoutCollection_When_Transforms_Then_ReturnsGameWithoutCollection() throws Exception {

        IGDBGame data = IGDBGame.create(10, "title", "slug", "url", 100, 200);
        data.collection = 0;

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().collection);
        assertFalse(result.get().collection.isPresent());
    }

    @Test
    public void given_notPresentCollectionData_When_Transforms_Then_ReturnsGameWithoutCollection() throws Exception {

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        NetworkEntityIdRelation<ICollectionData> collection = new NetworkEntityIdRelation<>(10, Optional.absent());
        when(data.getCollection()).thenReturn(Optional.of(collection));
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().collection);
        assertFalse(result.get().collection.isPresent());
    }

    @Test
    public void Given_CollectionMappingError_When_Transforms_Then_ReturnsGameWithoutCollection() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        RealmCollection collection = new RealmCollection();
        data.setCollection(collection);

        when(collectionMapper.transform(collection)).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().collection);
        assertFalse(result.get().collection.isPresent());
    }

    @Test
    public void given_notPresentCover_When_Transforms_Then_ReturnsGameWithoutCover() throws Exception {

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().cover);
        assertFalse(result.get().cover.isPresent());
    }

    @Test
    public void Given_CoverMapError_When_Transforms_Then_ReturnsGameWithoutCover() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        data.setCover(new RealmImageData());
        when(imageMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().cover);
        assertFalse(result.get().cover.isPresent());
    }

    @Test
    public void Given_EmptyScreenshots_When_Transforms_Then_ReturnsGameWithoutScreens() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        RealmList<RealmImageData> screens = new RealmList<>();
        data.setScreenshots(screens);

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().screenshots);
        assertTrue(result.get().screenshots.isEmpty());
    }

    @Test
    public void given_screenshotsMapError_When_Transforms_Then_ReturnsGameWithoutScreen() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        RealmList<RealmImageData> screens = new RealmList<>();
        screens.add(new RealmImageData());
        screens.add(new RealmImageData());
        data.setScreenshots(screens);
        when(imageMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().screenshots);
        assertTrue(result.get().screenshots.isEmpty());
        verify(imageMapper, times(2)).transform(any());
    }

    @Test
    public void Given_EmptyDevelopers_When_Transforms_Then_ReturnsGameWithoutDevelopers() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        RealmList<RealmCompany> developers = new RealmList<>();
        data.setDevelopers(developers);

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().developers);
        assertTrue(result.get().developers.isEmpty());
    }

    @Test
    public void given_developerMapError_When_Transforms_Then_ReturnsGameWithoutDeveloper() throws Exception {

        RealmGame data = new RealmGame();
        data.setName("name");
        data.setId(10);
        RealmList<RealmCompany> developers = new RealmList<>();
        developers.add(new RealmCompany());
        developers.add(new RealmCompany());
        developers.add(new RealmCompany());
        data.setDevelopers(developers);

        when(companyMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().developers);
        assertTrue(result.get().developers.isEmpty());
        verify(companyMapper, times(3)).transform(any());
    }

    @Test
    public void Given_NotPresentDevelopers_WheN_Transforms_Then_ReturnsGameWithoutDevelopers() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(10, Optional.absent()));
        companies.add(new NetworkEntityIdRelation<>(11, Optional.absent()));
        companies.add(new NetworkEntityIdRelation<>(12, Optional.absent()));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(companies);
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().developers);
        assertTrue(result.get().developers.isEmpty());
        verifyZeroInteractions(companyMapper);
    }

    @Test
    public void Given_EmptyPublishers_When_Transforms_Then_ReturnsGameWithoutPublishers() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(companies);
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().publishers);
        assertTrue(result.get().publishers.isEmpty());
        verifyZeroInteractions(companyMapper);
    }

    @Test
    public void Given_PublisherMapError_When_Transforms_Then_ReturnsGameWithoutPublishers() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(11, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmCompany())));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(companies);
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        when(companyMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().publishers);
        assertTrue(result.get().publishers.isEmpty());
        verify(companyMapper, times(3)).transform(any());
    }

    @Test
    public void given_notPresentPublisher_When_Transforms_Then_ReturnsGameWithoutPublishers() throws Exception {

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(10, Optional.absent()));
        companies.add(new NetworkEntityIdRelation<>(11, Optional.absent()));
        companies.add(new NetworkEntityIdRelation<>(12, Optional.absent()));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(companies);
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().publishers);
        assertTrue(result.get().publishers.isEmpty());
        verifyZeroInteractions(companyMapper);
    }

    @Test
    public void Given_EmptyGenres_When_Transforms_Then_ReturnsGameWithoutGenres() throws Exception {

        List<NetworkEntityIdRelation<IGenreData>> genres = new ArrayList<>();

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(genres);
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().genres);
        assertTrue(result.get().genres.isEmpty());
        verifyZeroInteractions(genreMapper);
    }

    @Test
    public void given_notPresentGenre_When_Transforms_Then_ReturnsGameWithoutGenres() throws Exception {

        List<NetworkEntityIdRelation<IGenreData>> genres = new ArrayList<>();
        genres.add(new NetworkEntityIdRelation<>(10, Optional.absent()));
        genres.add(new NetworkEntityIdRelation<>(12, Optional.absent()));
        genres.add(new NetworkEntityIdRelation<>(14, Optional.absent()));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(genres);
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().genres);
        assertTrue(result.get().genres.isEmpty());
        verifyZeroInteractions(genreMapper);
    }

    @Test
    public void Given_GenreMapError_When_Transforms_Then_ReturnsGameWithoutGenres() throws Exception {

        List<NetworkEntityIdRelation<IGenreData>> genres = new ArrayList<>();
        genres.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmGenre())));
        genres.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmGenre())));
        genres.add(new NetworkEntityIdRelation<>(14, Optional.of(new RealmGenre())));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(genres);
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(new ArrayList<>());

        when(genreMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().genres);
        assertTrue(result.get().genres.isEmpty());
        verify(genreMapper, times(3)).transform(any());
    }

    @Test
    public void Given_emptyPlatforms_When_Transforms_Then_ReturnsGameWithoutPlatforms() throws Exception {

        List<NetworkEntityIdRelation<IPlatformData>> platforms = new ArrayList<>();
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(platforms);

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().platforms);
        assertTrue(result.get().platforms.isEmpty());
        verifyZeroInteractions(platformMapper);
    }

    @Test
    public void Given_notPresentPlatform_When_Transforms_Then_ReturnsGameWithoutPlatforms() throws Exception {

        List<NetworkEntityIdRelation<IPlatformData>> platforms = new ArrayList<>();
        platforms.add(new NetworkEntityIdRelation<>(10, Optional.absent()));
        platforms.add(new NetworkEntityIdRelation<>(12, Optional.absent()));
        platforms.add(new NetworkEntityIdRelation<>(14, Optional.absent()));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(platforms);

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().platforms);
        assertTrue(result.get().platforms.isEmpty());
        verifyZeroInteractions(platformMapper);
    }

    @Test
    public void given_platformMapError_When_Transforms_Then_ReturnsGameWithoutPlatform() throws Exception {

        List<NetworkEntityIdRelation<IPlatformData>> platforms = new ArrayList<>();
        platforms.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(14, Optional.of(new RealmPlatform())));

        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.absent());
        when(data.getCover()).thenReturn(Optional.absent());
        when(data.getScreenshots()).thenReturn(new ArrayList<>());
        when(data.getDevelopers()).thenReturn(new ArrayList<>());
        when(data.getPublishers()).thenReturn(new ArrayList<>());
        when(data.getGenres()).thenReturn(new ArrayList<>());
        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(platforms);

        when(platformMapper.transform(any())).thenReturn(Optional.absent());

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertNotNull(result.get().platforms);
        assertTrue(result.get().platforms.isEmpty());
        verify(platformMapper, times(3)).transform(any());
    }

    @Test
    public void given_nullData_When_Transforms_Then_ReturnsAbsent() throws Exception {

        IGameDatasource data = null;

        Optional<Game> result = mapper.transform(data);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void given_dataWithoutName_When_Transforms_Then_ReturnsAbsent() throws Exception {

        RealmGame data = new RealmGame();
        data.setId(50);
        data.setName(null);

        Optional<Game> result = mapper.transform(data);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    public void given_validData_When_Transform_Then_ReturnsGameEntity() throws Exception {

        List<NetworkEntityIdRelation<IPlatformData>> platforms = new ArrayList<>();
        platforms.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(12, Optional.of(new RealmPlatform())));
        platforms.add(new NetworkEntityIdRelation<>(14, Optional.of(new RealmPlatform())));
        IGameDatasource data = mock(IGameDatasource.class);
        when(data.getName()).thenReturn("name");
        when(data.getId()).thenReturn(10);
        when(data.getCollection()).thenReturn(Optional.of(new NetworkEntityIdRelation<>(2, Optional.of(new RealmCollection()))));
        when(data.getCover()).thenReturn(Optional.of(new RealmImageData()));

        List<IImageData> screenshots = new ArrayList<>();
        screenshots.add(new RealmImageData());
        when(data.getScreenshots()).thenReturn(screenshots);

        List<NetworkEntityIdRelation<ICompanyData>> companies = new ArrayList<>();
        companies.add(new NetworkEntityIdRelation<>(3, Optional.of(new RealmCompany())));
        companies.add(new NetworkEntityIdRelation<>(4, Optional.of(new RealmCompany())));
        when(data.getDevelopers()).thenReturn(companies);
        when(data.getPublishers()).thenReturn(companies);

        List<NetworkEntityIdRelation<IGenreData>> genres = new ArrayList<>();
        genres.add(new NetworkEntityIdRelation<>(10, Optional.of(new RealmGenre())));
        when(data.getGenres()).thenReturn(genres);

        when(data.getReleases()).thenReturn(new ArrayList<>());
        when(data.getPlatforms()).thenReturn(platforms);

        Platform expectedPlatform = Platform.create(1, "platform");
        Collection expectedCollection = Collection.create(2, "collection");
        ImageData expectedImage = ImageData.create("id", 2, 3, "cover");
        Company expectedCompany = Company.create(5, "company");
        Genre expectedGenre = Genre.create(6, "genre");
        when(platformMapper.transform(any())).thenReturn(Optional.of(expectedPlatform));
        when(collectionMapper.transform(any())).thenReturn(Optional.of(expectedCollection));
        when(imageMapper.transform(any())).thenReturn(Optional.of(expectedImage));
        when(companyMapper.transform(any())).thenReturn(Optional.of(expectedCompany));
        when(genreMapper.transform(any())).thenReturn(Optional.of(expectedGenre));

        // Act
        Optional<Game> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertTrue(result.get().collection.isPresent());
        assertEquals(expectedCollection, result.get().collection.get());
        assertNotNull(result.get().platforms);
        assertEquals(platforms.size(), result.get().platforms.size());
        for (Platform platform1 : result.get().platforms) {
            assertEquals(expectedPlatform, platform1);
        }
        assertNotNull(result.get().cover);
        assertTrue(result.get().cover.isPresent());
        assertEquals(expectedImage, result.get().cover.get());
        assertEquals(screenshots.size(), result.get().screenshots.size());
        for (ImageData screenshot : result.get().screenshots) {
            assertEquals(expectedImage, screenshot);
        }
        assertEquals(companies.size(), result.get().developers.size());
        for (Company developer : result.get().developers) {
            assertEquals(expectedCompany, developer);
        }
        assertEquals(companies.size(), result.get().publishers.size());
        for (Company publisher : result.get().publishers) {
            assertEquals(expectedCompany, publisher);
        }
        assertEquals(genres.size(), result.get().genres.size());
        for (Genre genre : result.get().genres) {
            assertEquals(expectedGenre, genre);
        }
    }
}