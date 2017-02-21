package com.piticlistudio.playednext.utils;

import android.graphics.Color;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases
 * Created by jorge.garcia on 21/02/2017.
 */
public class UIUtilsTest {

    @Test
    public void getTextColorForBackground_whiteBg() throws Exception {

        // Test with white background
        int color = UIUtils.getTextColorForBackground(Color.WHITE);

        assertFalse(color == Color.WHITE);
        assertEquals(Color.BLACK, color);
    }

    @Test
    public void getTextColorForBackground_blackBg() throws Exception {

        // Test with white background
        int color = UIUtils.getTextColorForBackground(Color.BLACK);

        assertFalse(color == Color.BLACK);
        assertEquals(Color.WHITE, color);
    }

    @Test
    public void getTextColorForBackground_lightColor() throws Exception {

        int color = Color.parseColor("#e5e5e5");

        int result = UIUtils.getTextColorForBackground(color);

        int red = Color.red(result);
        int green = Color.green(result);
        int blue = Color.blue(result);

        assertTrue(red < 255/2);
        assertTrue(green < 255/2);
        assertTrue(blue < 255/2);
    }

    @Test
    public void getTextColorForBackground_darkColor() throws Exception {

        int color = Color.parseColor("#003153");

        int result = UIUtils.getTextColorForBackground(color);

        int red = Color.red(result);
        int green = Color.green(result);
        int blue = Color.blue(result);

        assertTrue(red > 255/2);
        assertTrue(green > 255/2);
        assertTrue(blue > 255/2);
    }
}