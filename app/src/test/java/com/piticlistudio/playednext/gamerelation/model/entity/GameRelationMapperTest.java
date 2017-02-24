package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test cases for GameRelationMapper
 * Created by jorge.garcia on 24/02/2017.
 */
public class GameRelationMapperTest extends BaseTest {

    @Mock
    GameMapper gameMapper;

    @InjectMocks
    GameRelationMapper mapper;

    @Test
    public void given_NullEntity_When_TransformsIntoGameRelation_Then_ReturnsAbsent() throws Exception {

        RealmGameRelation data = null;

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_relationWithoutGame_When_TransformsIntoGameRelation_Then_ReturnsAbsent() throws Exception {

        RealmGameRelation data = new RealmGameRelation(10, null, 100, 100);

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_relationWithoutId_When_MapsIntoGameRelation_Then_ReturnsAbsent() throws Exception {

        RealmGameRelation data = new RealmGameRelation(0, GameFactory.provideRealmGame(10, "title"), 100, 100);

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_gameMapperFails_When_TriesToMapAGameRelation_Then_ReturnsAbsent() throws Exception {

        RealmGame game = GameFactory.provideRealmGame(10, "title");
        RealmGameRelation data = new RealmGameRelation(10, game, 100, 100);

        when(gameMapper.transform(game)).thenReturn(Optional.absent());

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_mappableRelationAndGame_When_TriesToMap_Then_ReturnsMappedRelation() throws Exception {

        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        Game game = GameFactory.provide(10, "title");
        RealmGameRelation data = new RealmGameRelation(10, realmGame, 100, 100);

        when(gameMapper.transform(realmGame)).thenReturn(Optional.of(game));

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(game, result.get().game());
        assertEquals(10, result.get().id());
        assertEquals(100, result.get().createdAt());
        assertEquals(100, result.get().getUpdatedAt());
    }
}