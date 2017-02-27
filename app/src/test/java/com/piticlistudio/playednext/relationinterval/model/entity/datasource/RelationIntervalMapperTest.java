package com.piticlistudio.playednext.relationinterval.model.entity.datasource;

import com.fernandocejas.arrow.optional.Optional;
import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for RelationIntervalMapper
 * Created by jorge.garcia on 27/02/2017.
 */
public class RelationIntervalMapperTest extends BaseTest {

    @InjectMocks
    RelationIntervalMapper mapper;

    @Test
    public void given_nullIntervalStatus_When_TransformToInterval_Then_ReturnsAbsent() throws Exception {

        RealmRelationInterval data = null;
        Optional<RelationInterval> result = mapper.transform(data);

        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_EmptyIntervalStatus_When_Transform_Then_ReturnsAbsent() throws Exception {

        RealmRelationInterval data = new RealmRelationInterval();

        // Act
        Optional<RelationInterval> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_MissingIdInterval_When_Transform_Then_ReturnsAbsent() throws Exception {
        RealmRelationInterval data = new RealmRelationInterval();
        data.setStartedAt(5000);
        data.setType(RelationInterval.RelationType.DONE.ordinal());

        // Act
        Optional<RelationInterval> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_MissingStartDate_When_Transform_Then_ReturnsAbsent() throws Exception {

        RealmRelationInterval data = new RealmRelationInterval();
        data.setId(50);
        data.setType(RelationInterval.RelationType.DONE.ordinal());

        // Act
        Optional<RelationInterval> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    @Test
    public void given_ValidInterval_When_Tranform_Then_ReturnsRealmRelationValue() throws Exception {
        RealmRelationInterval data = new RealmRelationInterval();
        data.setStartedAt(5000);
        data.setId(4);
        data.setEndedAt(40000);
        data.setType(RelationInterval.RelationType.DONE.ordinal());

        // Act
        Optional<RelationInterval> result = mapper.transform(data);

        // Assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(5000, result.get().startAt());
        assertEquals(4, result.get().id());
        assertEquals(40000, result.get().getEndAt());
        assertEquals(RelationInterval.RelationType.DONE, result.get().type());
    }
}