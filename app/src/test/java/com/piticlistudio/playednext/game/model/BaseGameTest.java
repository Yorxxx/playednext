package com.piticlistudio.playednext.game.model;


import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.company.model.entity.Company;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.datasource.IGameDatasource;
import com.piticlistudio.playednext.genre.model.entity.Genre;
import com.piticlistudio.playednext.image.model.entity.ImageData;
import com.piticlistudio.playednext.utils.StringUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BaseGameTest extends BaseTest {

    protected static boolean equalsGame(Game data, IGameDatasource remoteData) {
        if (data.id() != remoteData.getId())
            return false;
        if (!data.title().equals(remoteData.getName()))
            return false;
        if (!StringUtils.equalsIgnoreCase(data.summary, remoteData.getSummary()))
            return false;
        if (!StringUtils.equalsIgnoreCase(data.storyline, remoteData.getStoryline()))
            return false;
       /* assertEquals(data.hypes, remoteData.getHypes());
        assertEquals(data.rating, remoteData.getRating(), 0);*/

        assertNotNull(data.cover);
        assertEquals(data.cover.isPresent(), remoteData.getCover().isPresent());
        if (data.cover.isPresent()) {
            assertEquals(data.cover.get().id(), remoteData.getCover().get().getId());
            assertEquals(data.cover.get().thumbUrl(), remoteData.getCover().get().getUrl());
            assertEquals(data.cover.get().fullWidth(), remoteData.getCover().get().getWidth());
            assertEquals(data.cover.get().fullHeight(), remoteData.getCover().get().getHeight());
        }

        assertNotNull(data.collection);
        if (remoteData.getCollection().isPresent() && remoteData.getCollection().get().data.isPresent()) {
            assertTrue(data.collection.isPresent());
        }
        else {
            assertFalse(data.collection.isPresent());
        }
        if (data.collection.isPresent()) {
            assertTrue(remoteData.getCollection().get().data.isPresent());
            assertEquals(data.collection.get().id(), remoteData.getCollection().get().id);
            assertEquals(data.collection.get().id(), remoteData.getCollection().get().data.get().getId());
            assertEquals(data.collection.get().name(), remoteData.getCollection().get().data.get().getName());
        }

        if (data.developers == null)
            assertTrue(remoteData.getDevelopers().isEmpty());
        else {
            assertEquals(data.developers.size(), remoteData.getDevelopers().size());
            for (int i = 0; i < data.developers.size(); i++) {
                Company datacompany = data.developers.get(i);
                assertTrue(remoteData.getDevelopers().get(i).data.isPresent());
                assertEquals(datacompany.id(), remoteData.getDevelopers().get(i).id);
                assertEquals(datacompany.id(), remoteData.getDevelopers().get(i).data.get().getId());
                assertEquals(datacompany.name(), remoteData.getDevelopers().get(i).data.get().getName());
            }
        }

       if (data.publishers == null)
            assertTrue(remoteData.getPublishers().isEmpty());
        else {
            assertEquals(data.publishers.size(), remoteData.getPublishers().size());
            for (int i = 0; i < data.publishers.size(); i++) {
                Company datacompany = data.publishers.get(i);
                assertTrue(remoteData.getPublishers().get(i).data.isPresent());
                assertEquals(datacompany.id(), remoteData.getPublishers().get(i).id);
                assertEquals(datacompany.id(), remoteData.getPublishers().get(i).data.get().getId());
                assertEquals(datacompany.name(), remoteData.getPublishers().get(i).data.get().getName());
            }
        }

        if (data.genres == null)
            assertTrue(remoteData.getGenres().isEmpty());
        else {
            assertEquals(data.genres.size(), remoteData.getGenres().size());
            for (int i = 0; i < data.genres.size(); i++) {
                Genre datagenre = data.genres.get(i);
                assertTrue(remoteData.getGenres().get(i).data.isPresent());
                assertEquals(datagenre.id(), remoteData.getGenres().get(i).id);
                assertEquals(datagenre.id(), remoteData.getGenres().get(i).data.get().getId());
                assertEquals(datagenre.name(), remoteData.getGenres().get(i).data.get().getName());
            }
        }

        if (data.screenshots == null)
            assertTrue(remoteData.getScreenshots().isEmpty());
        else {
            assertEquals(data.screenshots.size(), remoteData.getScreenshots().size());
            for (int i = 0; i < data.screenshots.size(); i++) {
                ImageData imageData = data.screenshots.get(i);
                assertEquals(imageData.thumbUrl(), remoteData.getScreenshots().get(i).getUrl());
                assertEquals(imageData.id(), remoteData.getScreenshots().get(i).getId());
                assertEquals(imageData.fullHeight(), remoteData.getScreenshots().get(i).getHeight());
                assertEquals(imageData.fullWidth(), remoteData.getScreenshots().get(i).getWidth());
            }
        }

        /*if (data.platforms == null)
            assertTrue(remoteData.getPlatforms().isEmpty());
        else {
            assertEquals(data.platforms.size(), remoteData.getPlatforms().size());
            for (int i = 0; i < data.platforms.size(); i++) {
                Platform platform = data.platforms.get(i);
                assertTrue(remoteData.getPlatforms().get(i).data.isPresent());
                assertEquals(platform.id(), remoteData.getPlatforms().get(i).id);
                assertEquals(platform.id(), remoteData.getPlatforms().get(i).data.get().getId());
                assertEquals(platform.name(), remoteData.getPlatforms().get(i).data.get().getName());
            }
        }

        // TODO
//        if (data.releases == null)
//            assertTrue(remoteData.getReleases().isEmpty());
//        else {
//            assertEquals(data.releases.size(), remoteData.getReleases().size());
//            for (int i = 0; i < data.releases.size(); i++) {
//                GameRelease release = data.releases.get(i);
//                assertEquals(release.platform().id(), remoteData.getReleases().get(i).getPlatform().data.get().getId());
//                assertEquals(release.releaseDate().date(), remoteData.getReleases().get(i).getReleaseDate().getDate());
//                assertEquals(release.releaseDate().region(), remoteData.getReleases().get(i).getReleaseDate().getRegion());
//            }
//        }*/

        return true;
    }
}
