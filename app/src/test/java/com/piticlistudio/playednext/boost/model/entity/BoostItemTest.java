package com.piticlistudio.playednext.boost.model.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for BoostItems
 * Created by jorge.garcia on 08/03/2017.
 */
public class BoostItemTest {

    @Test
    public void given_any_When_Create_Then_InstantiatesANewBoost() throws Exception {

        // Act
        BoostItem item = BoostItem.create(1000, 10);

        // Assert
        assertNotNull(item);
        assertEquals(1000, item.value());
        assertEquals(10, item.type());
    }
}