package com.piticlistudio.playednext.gamerelation.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.GameFactory;
import com.piticlistudio.playednext.game.model.entity.Game;
import com.piticlistudio.playednext.game.model.entity.RealmGameMapper;
import com.piticlistudio.playednext.game.model.entity.datasource.RealmGame;
import com.piticlistudio.playednext.gamerelation.model.entity.datasource.RealmGameRelation;
import com.piticlistudio.playednext.gamerelease.model.entity.RealmGameReleaseMapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RealmRelationIntervalMapper;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Test cases for RealmGameRelationMapper
 * Created by jorge.garcia on 24/02/2017.
 */
public class RealmGameRelationMapperTest extends BaseTest {

    @Mock
    RealmGameMapper gameMapper;

    @Mock
    RealmRelationIntervalMapper intervalMapper;

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

    @Test
    public void given_intervalMapperFails_When_Transform_Then_ReturnsRelation() throws Exception {
        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        Game game = GameFactory.provide(10, "title");
        GameRelation data = GameRelation.create(game, 1000);
        data.getStatuses().add(RelationInterval.create(10, RelationInterval.RelationType.DONE, 1000));
        data.getStatuses().add(RelationInterval.create(11, RelationInterval.RelationType.PLAYING, 2000));
        when(gameMapper.transform(game)).thenReturn(Optional.of(realmGame));
        when(intervalMapper.transform(any())).thenReturn(Optional.absent());

        Optional<RealmGameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(realmGame, result.get().getGame());
        assertEquals(data.id(), result.get().getId());
        assertEquals(data.createdAt(), result.get().getCreatedAt());
        assertEquals(data.getUpdatedAt(), result.get().getUpdatedAt());
        assertTrue(result.get().getStatuses().isEmpty());
    }

    @Test
    public void given_intervalMapperFails_When_Transform_Then_RemovesUnmappableIntervals() throws Exception {

        RealmGame realmGame = GameFactory.provideRealmGame(10, "title");
        Game game = GameFactory.provide(10, "title");
        GameRelation data = GameRelation.create(game, 1000);
        RealmRelationInterval intervalResult1 = new RealmRelationInterval(1, 1, 1000, 2000);
        RelationInterval interval1 = RelationInterval.create(1, RelationInterval.RelationType.values()[1], 1000);
        interval1.setEndAt(2000);
        RealmRelationInterval intervalResult2 = new RealmRelationInterval(2, 2, 2000, 3000);
        RelationInterval interval2 = RelationInterval.create(21, RelationInterval.RelationType.values()[2], 2000);
        interval2.setEndAt(3000);
        RealmRelationInterval intervalResult3 = new RealmRelationInterval(3, 3, 3000, 4000);
        RelationInterval interval3 = RelationInterval.create(3, RelationInterval.RelationType.values()[3], 3000);
        interval3.setEndAt(4000);
        data.getStatuses().add(interval1);
        data.getStatuses().add(interval2);
        data.getStatuses().add(interval3);
        when(intervalMapper.transform(interval1)).thenReturn(Optional.of(intervalResult1));
        when(intervalMapper.transform(interval2)).thenReturn(Optional.absent());
        when(intervalMapper.transform(interval3)).thenReturn(Optional.of(intervalResult3));
        when(gameMapper.transform(game)).thenReturn(Optional.of(realmGame));


        Optional<RealmGameRelation> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(realmGame, result.get().getGame());
        assertEquals(10, result.get().getId());
        assertEquals(1000, result.get().getCreatedAt());
        assertEquals(1000, result.get().getUpdatedAt());
        assertEquals(2, result.get().getStatuses().size());
        assertTrue(result.get().getStatuses().contains(intervalResult1));
        assertFalse(result.get().getStatuses().contains(intervalResult2));
        assertTrue(result.get().getStatuses().contains(intervalResult3));
    }
}