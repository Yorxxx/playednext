package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.company.model.entity.datasource.RealmCompany;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.RealmGameRelease;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.genre.model.entity.datasource.RealmGenre;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.RealmPlatform;
import com.piticlistudio.playednext.releasedate.model.entity.datasource.RealmReleaseDate;

import org.junit.Test;

import java.util.List;

import io.realm.RealmList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class RealmGameTest {

    RealmGame data = GameFactory.provideRealmGame(60, "title");

    @Test
    public void getId() throws Exception {
        assertEquals(60, data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("title", data.getName());
    }

    @Test
    public void getSummary() throws Exception {
        data.setSummary("summary");
        assertEquals("summary", data.getSummary());
    }

    @Test
    public void getStoryline() throws Exception {
        data.setStoryline("storyline");
        assertEquals("storyline", data.getStoryline());
    }

    @Test
    public void getCollection() throws Exception {
        assertNotNull(data.getCollection());
        assertTrue(data.getCollection().isPresent());
        assertTrue(data.getCollection().get().data.isPresent());

        // Arrange
        data.setCollection(null);

        // Assert
        assertNotNull(data.getCollection());
        assertFalse(data.getCollection().isPresent());
    }

    @Test
    public void getCover() throws Exception {

        RealmImageData cover = new RealmImageData("id", "url", 200, 500);
        data.setCover(cover);

        // Act
        Optional<IImageData> result = data.getCover();
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(cover, data.getCover().get());

        // Arrange
        data.setCover(null);

        // Act
        result = data.getCover();

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());

    }

    @Test
    public void getScreenshots() throws Exception {
        RealmImageData screen1 = new RealmImageData("id1", "url", 200, 33);
        RealmImageData screen2 = new RealmImageData("id2", "url2", 200, 33);
        RealmImageData screen3 = new RealmImageData("id3", "url3", 200, 33);
        RealmList<RealmImageData> screens = new RealmList<>();
        screens.add(screen1);
        screens.add(screen2);
        screens.add(screen3);
        data.setScreenshots(screens);

        // Act
        List<IImageData> result = data.getScreenshots();

        // Assert
        assertNotNull(result);
        assertEquals(screens, result);

        // Arrange
        data.setScreenshots(null);

        // Act
        result = data.getScreenshots();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getDevelopers() throws Exception {

        RealmList<RealmCompany> developers = new RealmList<>();
        developers.add(new RealmCompany(1, "1"));
        developers.add(new RealmCompany(2, "2"));
        developers.add(new RealmCompany(3, "3"));
        data.setDevelopers(developers);

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getDevelopers();

        // Assert
        assertNotNull(result);
        assertEquals(developers.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertTrue(result.get(i).data.isPresent());
            assertEquals(result.get(i).getData(), developers.get(i));
            assertEquals(result.get(i).id, developers.get(i).getId());
        }

    }

    @Test
    public void getPublishers() throws Exception {

        RealmList<RealmCompany> publishers = new RealmList<>();
        publishers.add(new RealmCompany(1, "1"));
        publishers.add(new RealmCompany(2, "2"));
        publishers.add(new RealmCompany(3, "3"));
        data.setPublishers(publishers);

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getPublishers();

        // Assert
        assertNotNull(result);
        assertEquals(publishers.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertTrue(result.get(i).data.isPresent());
            assertEquals(result.get(i).getData(), publishers.get(i));
            assertEquals(result.get(i).id, publishers.get(i).getId());
        }
    }

    @Test
    public void getGenres() throws Exception {

        RealmList<RealmGenre> genres = new RealmList<>();
        genres.add(new RealmGenre(1, "1"));
        genres.add(new RealmGenre(2, "2"));
        genres.add(new RealmGenre(3, "3"));
        data.setGenres(genres);

        // Act
        List<NetworkEntityIdRelation<IGenreData>> result = data.getGenres();

        // Assert
        assertNotNull(result);
        assertEquals(genres.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertTrue(result.get(i).data.isPresent());
            assertEquals(genres.get(i), result.get(i).getData());
            assertEquals(genres.get(i).getId(), result.get(i).id);
        }
    }

    @Test
    public void getReleases() throws Exception {
        RealmList<RealmGameRelease> releases = new RealmList<>();
        for (int i = 0; i < 3; i++) {
            RealmPlatform platform = new RealmPlatform(i, "platform_"+i);
            RealmReleaseDate date = new RealmReleaseDate("date_"+i, (i+1)*1000);
            releases.add(new RealmGameRelease(platform, date));
        }
        data.setReleases(releases);

        // ACt
        List<IGameReleaseDateData> result = data.getReleases();

        // Assert
        assertNotNull(result);
        assertEquals(releases.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(releases.get(i), result.get(i));
        }

    }
}