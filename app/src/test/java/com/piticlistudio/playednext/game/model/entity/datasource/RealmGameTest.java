package com.piticlistudio.playednext.game.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.RealmImageData;

import org.junit.Test;

import java.util.List;

import io.realm.RealmList;

import static org.junit.Assert.*;

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
        assertEquals(data.collection.id, data.getCollection().get().id);
        assertTrue(data.getCollection().get().data.isPresent());
        assertEquals(data.collection.id, data.getCollection().get().data.get().getId());
        assertEquals(data.collection.getName(), data.getCollection().get().data.get().getName());

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
}