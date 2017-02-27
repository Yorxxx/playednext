package com.piticlistudio.playednext.gamerelation.model.entity.datasource;

import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for RealmGameRelation
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelationTest {

    RealmGameRelation relation;
    RealmGame game = GameFactory.provideRealmGame(10, "title");

    @Before
    public void setUp() throws Exception {
        relation = new RealmGameRelation(10, game, 100, 200);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(10, relation.getId());
    }

    @Test
    public void getGame() throws Exception {
        assertNotNull(relation.getGame());
        assertEquals(game, relation.getGame());
    }

    @Test
    public void getCreatedAt() throws Exception {
        assertEquals(100, relation.getCreatedAt());
    }

    @Test
    public void getUpdatedAt() throws Exception {
        assertEquals(200, relation.getUpdatedAt());
    }

    @Test
    public void given_emptyStatusRelation_When_GetStatus_Then_EmptyList() throws Exception {
        assertNotNull(relation.getStatus());
        assertTrue(relation.getStatus().isEmpty());
    }

    @Test
    public void given_multipleStatusRelation_When_GetStatus_Then_ReturnsNewList() throws Exception {

        relation.getStatuses().add(new RealmRelationInterval());
        relation.getStatuses().add(new RealmRelationInterval());
        relation.getStatuses().add(new RealmRelationInterval());
        relation.getStatuses().add(new RealmRelationInterval());

        assertNotNull(relation.getStatus());
        assertEquals(4, relation.getStatus().size());
    }
}