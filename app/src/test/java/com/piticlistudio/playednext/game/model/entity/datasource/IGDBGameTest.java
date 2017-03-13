package com.piticlistudio.playednext.game.model.entity.datasource;

import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGDBGameRelease;
import com.piticlistudio.playednext.gamerelease.model.entity.datasource.IGameReleaseDateData;
import com.piticlistudio.playednext.genre.model.entity.datasource.IGenreData;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.IGDBImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;
import com.piticlistudio.playednext.platform.model.entity.datasource.IPlatformData;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 10/02/2017.
 */
public class IGDBGameTest {

    private IGDBGame data = IGDBGame.create(10, "name", "slug", "url", 1000, 2500);


    @Test
    public void getId() throws Exception {
        assertEquals(data.id(), data.getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals(data.name(), data.getName());
    }

    @Test
    public void getSummary() throws Exception {
        data.summary = "summary";
        assertEquals(data.summary, data.getSummary());
    }

    @Test
    public void getStoryline() throws Exception {
        data.storyline = "storyline";
        assertEquals(data.storyline, data.getStoryline());
    }

    @Test
    public void getCollection() throws Exception {
        data.collection = 10;
        assertNotNull(data.getCollection());
        assertTrue(data.getCollection().isPresent());
        assertEquals(data.collection, data.getCollection().get().id);
        assertFalse(data.getCollection().get().data.isPresent());

        // Arrange
        data.collection = -1;

        // Assert
        assertNotNull(data.getCollection());
        assertFalse(data.getCollection().isPresent());
    }

    @Test
    public void getCover() throws Exception {
        final IGDBImageData cover = IGDBImageData.create("url", 200, 300, "id");

        data.cover = cover;
        assertNotNull(data.getCover());
        assertTrue(data.getCover().isPresent());
        assertEquals(cover, data.getCover().get());

        data.cover = null;

        // Assert
        assertNotNull(data.getCover());
        assertFalse(data.getCover().isPresent());

    }

    @Test
    public void getScreenshots() throws Exception {
        final IGDBImageData screen1 = IGDBImageData.create("url", 200, 300, "id");
        final IGDBImageData screen2 = IGDBImageData.create("url2", 200, 300, "id2");
        final IGDBImageData screen3 = IGDBImageData.create("url3", 200, 300, "id3");
        List<IGDBImageData> screens = new ArrayList<>();
        screens.add(screen1);
        screens.add(screen2);
        screens.add(screen3);
        data.screenshots = screens;

        // Act
        List<IImageData> result = data.getScreenshots();

        // Assert
        assertNotNull(result);
        assertEquals(screens, result);

        // Arrange
        data.screenshots = null;

        result = data.getScreenshots();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void given_nullDevelopers_When_getDevelopers_Then_ReturnsEmptyList() throws Exception {

        data.developers = null;

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getDevelopers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getDevelopers() throws Exception {
        List<Integer> developers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            developers.add(i);
        }
        data.developers = developers;

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getDevelopers();

        // Assert
        assertNotNull(result);
        assertEquals(developers.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals((int) developers.get(i), result.get(i).id);
            assertNotNull(result.get(i).data);
            assertFalse(result.get(i).data.isPresent());
        }
    }

    @Test
    public void given_nullPublishers_When_GetPublishers_Then_ReturnsEmptyList() throws Exception {
        data.publishers = null;

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getPublishers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getPublishers() throws Exception {
        List<Integer> publishers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            publishers.add(i);
        }
        data.publishers = publishers;

        // Act
        List<NetworkEntityIdRelation<ICompanyData>> result = data.getPublishers();

        // Assert
        assertNotNull(result);
        assertEquals(publishers.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals((int) publishers.get(i), result.get(i).id);
            assertNotNull(result.get(i).data);
            assertFalse(result.get(i).data.isPresent());
        }
    }

    @Test
    public void given_nullGenres_When_GetGenres_Then_ReturnsEmptyList() throws Exception {

        data.genres = null;

        // Act
        List<NetworkEntityIdRelation<IGenreData>> result = data.getGenres();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getGenres() throws Exception {
        List<Integer> genres = new ArrayList<>();
        for (int i = 15; i < 20; i++) {
            genres.add(i);
        }
        data.genres = genres;

        // Act
        List<NetworkEntityIdRelation<IGenreData>> result = data.getGenres();

        // Assert
        assertNotNull(result);
        assertEquals(genres.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals((int) genres.get(i), result.get(i).id);
            assertNotNull(result.get(i).data);
            assertFalse(result.get(i).data.isPresent());
        }

    }

    @Test
    public void given_nullReleaseDates_When_getReleases_Then_ReturnsEmptyList() throws Exception {

        data.release_dates = null;

        // Act
        List<IGameReleaseDateData> result = data.getReleases();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void given_nullReleaseDates_When_getPlatforms_Then_ReturnsEmptyList() throws Exception {

        data.release_dates = null;

        // Act
        List<NetworkEntityIdRelation<IPlatformData>> result = data.getPlatforms();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getReleaseDates() throws Exception {
        List<IGDBGameRelease> dates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dates.add(IGDBGameRelease.create(i, (i+1)*1000, "human_"+i));
        }
        data.release_dates = dates;

        assertEquals(3, data.release_dates.size());

        // Act
        List<IGameReleaseDateData> result = data.getReleases();

        // Assert
        assertNotNull(result);
        assertEquals(dates.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(dates.get(i), result.get(i));
        }
    }

    @Test
    public void given_duplicatedReleaseDatePlatforms_When_getPlatforms_Then_RemovesDuplicatedEntities() throws Exception {

        List<IGDBGameRelease> dates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dates.add(IGDBGameRelease.create(i, (i+1)*1000, "human_"+i));
        }
        dates.add(IGDBGameRelease.create(0, 3000, "human3000"));
        dates.add(IGDBGameRelease.create(2, 3000, "human3000"));
        data.release_dates = dates;

        // Act
        List<NetworkEntityIdRelation<IPlatformData>> result = data.getPlatforms();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        List<Integer> checkedPlatformIds = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            assertFalse(result.get(i).data.isPresent());
            assertEquals(dates.get(i).getPlatform().id, result.get(i).id);
            assertFalse(checkedPlatformIds.contains(result.get(i).id));
            checkedPlatformIds.add(result.get(i).id);
        }
    }

    @Test
    public void getPlatforms() throws Exception {

        List<IGDBGameRelease> dates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dates.add(IGDBGameRelease.create(i, (i+1)*1000, "human_"+i));
        }
        data.release_dates = dates;

        assertEquals(3, data.release_dates.size());

        // Act
        List<NetworkEntityIdRelation<IPlatformData>> result = data.getPlatforms();

        // Assert
        assertNotNull(result);
        assertEquals(dates.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertFalse(result.get(i).data.isPresent());
            assertEquals(dates.get(i).getPlatform().id, result.get(i).id);
        }
    }
}