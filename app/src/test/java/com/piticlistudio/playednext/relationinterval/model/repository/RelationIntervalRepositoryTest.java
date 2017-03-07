package com.piticlistudio.playednext.relationinterval.model.repository;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.relationinterval.model.entity.RelationInterval;
import com.piticlistudio.playednext.relationinterval.model.repository.datasource.RealmRelationIntervalRepositoryImpl;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 07/03/2017.
 */
public class RelationIntervalRepositoryTest extends BaseTest {

    @Mock
    RealmRelationIntervalRepositoryImpl localImpl;

    @InjectMocks
    RelationIntervalRepository intervalRepository;

    @Test
    public void given_relationType_When_CreatesNewInterval_Then_ReturnsNewRelationInterval() throws Exception {

        when(localImpl.getAutoincrementId()).thenReturn(10);

        // Act
        RelationInterval result = intervalRepository.create(RelationInterval.RelationType.DONE);

        // Assert
        assertNotNull(result);
        assertEquals(RelationInterval.RelationType.DONE, result.type());
        assertEquals(10, result.id());
        assertTrue(result.startAt() > 0);
        verify(localImpl).getAutoincrementId();
    }
}