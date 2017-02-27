package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.GameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RelationIntervalMapper;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test cases for GameRelationMapper
 * Created by jorge.garcia on 24/02/2017.
 */
public class GameRelationMapperTest extends BaseTest {

    @Mock
    GameMapper gameMapper;

    @Mock
    RelationIntervalMapper intervalMapper;

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
    public void given_intervalMapperFails_When_Transform_Then_ReturnsRelation() throws Exception {
        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        Game game = GameFactory.provide(10, "title");
        RealmGameRelation data = new RealmGameRelation(10, realmGame, 100, 100);
        data.getStatuses().add(new RealmRelationInterval());
        data.getStatuses().add(new RealmRelationInterval());
        data.getStatuses().add(new RealmRelationInterval());
        when(gameMapper.transform(realmGame)).thenReturn(Optional.of(game));
        when(intervalMapper.transform(any())).thenReturn(Optional.absent());

        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(game, result.get().game());
        assertEquals(10, result.get().id());
        assertEquals(100, result.get().createdAt());
        assertEquals(100, result.get().getUpdatedAt());
        assertTrue(result.get().getStatuses().isEmpty());
    }

    @Test
    public void given_intervalMapperFails_When_Transform_Then_RemovesUnmappableIntervals() throws Exception {

        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        Game game = GameFactory.provide(10, "title");
        RealmGameRelation data = new RealmGameRelation(10, realmGame, 100, 100);
        RealmRelationInterval interval1 = new RealmRelationInterval(1, 1, 1000, 2000);
        RelationInterval intervalResult1 = RelationInterval.create(1, RelationInterval.RelationType.values()[1], 1000);
        intervalResult1.setEndAt(2000);
        RealmRelationInterval interval2 = new RealmRelationInterval(2, 2, 2000, 3000);
        RelationInterval intervalResult2 = RelationInterval.create(21, RelationInterval.RelationType.values()[2], 2000);
        intervalResult2.setEndAt(3000);
        RealmRelationInterval interval3 = new RealmRelationInterval(3, 3, 3000, 4000);
        RelationInterval intervalResult3 = RelationInterval.create(3, RelationInterval.RelationType.values()[3], 3000);
        intervalResult3.setEndAt(4000);
        data.getStatuses().add(interval1);
        data.getStatuses().add(interval2);
        data.getStatuses().add(interval3);
        when(intervalMapper.transform(interval1)).thenReturn(Optional.of(intervalResult1));
        when(intervalMapper.transform(interval2)).thenReturn(Optional.absent());
        when(intervalMapper.transform(interval3)).thenReturn(Optional.of(intervalResult3));
        when(gameMapper.transform(realmGame)).thenReturn(Optional.of(game));


        Optional<GameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(game, result.get().game());
        assertEquals(10, result.get().id());
        assertEquals(100, result.get().createdAt());
        assertEquals(100, result.get().getUpdatedAt());
        assertEquals(2, result.get().getStatuses().size());
        assertTrue(result.get().getStatuses().contains(intervalResult1));
        assertFalse(result.get().getStatuses().contains(intervalResult2));
        assertTrue(result.get().getStatuses().contains(intervalResult3));
    }
}