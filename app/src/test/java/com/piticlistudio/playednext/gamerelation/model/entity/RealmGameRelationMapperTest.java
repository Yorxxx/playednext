package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.gamerelease.model.entity.RealmGameReleaseMapper;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Test cases for RealmGameRelationMapper
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelationMapperTest extends BaseTest {

    @Mock
    RealmGameMapper gameMapper;

    @InjectMocks
    RealmGameRelationMapper mapper;

    @Test
    public void given_NullEntity_When_TriesToMap_Then_ReturnsAbsent() throws Exception {

        GameRelation data = null;

        Optional<RealmGameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_RealmGameMapError_When_TriesToMap_Then_ReturnAbsent() throws Exception {

        Game game = GameFactory.provide(10, "title");
        GameRelation data = GameRelation.create(game, 1000);

        when(gameMapper.transform(game)).thenReturn(Optional.absent());

        Optional<RealmGameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_MappableEntity_When_TriesToMap_Then_ReturnsRealmGameRelation() throws Exception {

        Game game = GameFactory.provide(10, "title");
        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        GameRelation data = GameRelation.create(game, 1000);

        when(gameMapper.transform(game)).thenReturn(Optional.of(realmGame));

        Optional<RealmGameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(realmGame, result.get().getGame());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.createdAt(), result.get().getCreatedAt());
        assertEquals(data.getUpdatedAt(), result.get().getUpdatedAt());
    }
}