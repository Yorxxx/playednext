package com.piticlistudio.playednext.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test cases for StringUtils
 * Created by jorge on 28/04/17.
 */
public class StringUtilsTest {


    @Test
    public void given_nullValues_When_equalsIgnoreCase_Then_ReturnsTrue() throws Exception {
        assertTrue(StringUtils.equalsIgnoreCase(null, null));
    }

    @Test
    public void given_firstElementIsNull_When_equalsIgnoreCase_Then_ReturnsFalse() throws Exception {
        assertFalse(StringUtils.equalsIgnoreCase(null, "foo"));
    }

    @Test
    public void given_secondElementIsNull_When_equalsIgnoreCase_Then_ReturnsFalse() throws Exception {
        assertFalse(StringUtils.equalsIgnoreCase("foo", null));
    }

    @Test
    public void given_elementsAreEqual_When_equalsIgnoreCase_Then_ReturnsTrue() throws Exception {
        assertTrue(StringUtils.equalsIgnoreCase("foo", "foo"));
    }

    @Test
    public void given_elementsAreDifferent_When_equalsIgnoreCase_Then_ReturnsFalse() throws Exception {
        assertFalse(StringUtils.equalsIgnoreCase("foo", "foo bar"));
    }

    @Test
    public void given_elementsDifferOnUppercase_When_equalsIgnoreCase_Then_ReturnsTrue() throws Exception {
        assertTrue(StringUtils.equalsIgnoreCase("foo", "FOO"));
    }

    @Test
    public void given_emptyList_When_stringify_Then_ReturnsNull() throws Exception {
        assertNull(StringUtils.stringify(new ArrayList<>()));
    }

    @Test
    public void given_stringList_When_stringify_Then_ReturnsStringElementsConcatenated() throws Exception {
        List<String> data = new ArrayList<>();
        data.add("Hello");
        data.add("World");
        data.add("!");

        // Assert
        assertEquals("Hello, World, !", StringUtils.stringify(data));
    }
}