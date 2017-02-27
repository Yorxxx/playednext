package com.piticlistudio.playednext.relationinterval.model.entity;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.relationinterval.model.entity.datasource.RealmRelationInterval;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;

/**
 * Test cases
 * Created by jorge.garcia on 27/02/2017.
 */
public class RealmRelationIntervalMapperTest extends BaseTest {

    @InjectMocks
    RealmRelationIntervalMapper mapper;

    @Test
    public void given_NullEntity_When_Transform_Then_ReturnsAbsent() throws Exception {

        Optional<RealmRelationInterval> result = mapper.transform(null);

        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_NotFinishedInterval_When_Transform_Then_ReturnsRealmRelationInterval() throws Exception {

        RelationInterval interval = RelationInterval.create(40, RelationInterval.RelationType.DONE, 5000);

        // Act
        Optional<RealmRelationInterval> result = mapper.transform(interval);

        // Assert
        assertNotNull(result);
        assertEquals(40, result.get().getId());
        assertEquals(RelationInterval.RelationType.DONE, result.get().getType());
        assertEquals(5000, result.get().getStartedAt());
        assertEquals(0, result.get().getEndedAt());
    }

    @Test
    public void given_FinishedInterval_When_Transform_Then_ReturnsRealmEntity() throws Exception {

        RelationInterval interval = RelationInterval.create(40, RelationInterval.RelationType.DONE, 5000);
        interval.setEndAt(6000);

        // Act
        Optional<RealmRelationInterval> result = mapper.transform(interval);

        // Assert
        assertNotNull(result);
        assertEquals(40, result.get().getId());
        assertEquals(RelationInterval.RelationType.DONE, result.get().getType());
        assertEquals(5000, result.get().getStartedAt());
        assertEquals(6000, result.get().getEndedAt());
    }
}