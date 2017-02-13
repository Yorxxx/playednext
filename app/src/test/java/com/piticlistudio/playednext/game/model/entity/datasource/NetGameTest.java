package com.piticlistudio.playednext.game.model.entity.datasource;

import com.piticlistudio.playednext.company.model.entity.datasource.ICompanyData;
import com.piticlistudio.playednext.image.model.entity.datasource.IImageData;
import com.piticlistudio.playednext.image.model.entity.datasource.NetImageData;
import com.piticlistudio.playednext.mvp.model.entity.NetworkEntityIdRelation;

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
public class NetGameTest {

    private NetGame data = NetGame.create(10, "name", "slug", "url", 1000, 2500);


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
        final NetImageData cover = NetImageData.create("url", 200, 300, "id");

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
        final NetImageData screen1 = NetImageData.create("url", 200, 300, "id");
        final NetImageData screen2 = NetImageData.create("url2", 200, 300, "id2");
        final NetImageData screen3 = NetImageData.create("url3", 200, 300, "id3");
        List<NetImageData> screens = new ArrayList<>();
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
            assertEquals((int) developers.get(i), (int) result.get(i).id);
            assertNotNull(result.get(i).data);
            assertFalse(result.get(i).data.isPresent());
        }

    }
}