package com.piticlistudio.playednext.relationinterval.model.entity;

import android.app.AlarmManager;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for RelationInterval
 * Created by jorge.garcia on 01/03/2017.
 */
public class RelationIntervalAndroidTest extends BaseAndroidTest {

    Resources res = InstrumentationRegistry.getTargetContext().getResources();

    @Test
    public void given_noneStatusType_when_getDisplayDate_Then_ReturnsNull() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.NONE, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());

        // Assert
        assertNull(result);
    }

    @Test
    public void given_completedToday_when_getDisplayDate_Then_ReturnsRelativeCompletionTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed, "1 hour ago");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedMoreThan24hoursago_when_getDisplayDate_Then_ReturnsAbsoluteCompletionTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed, "Yesterday");

        // Assert
        assertNotNull(result);
        assertTrue(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
    }

    @Test
    public void given_completedMoreThanAWeekAgo_when_getDisplayDate_Then_ReturnsAbsoluteCompletionTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_DAY * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed, "");

        // Assert
        assertNotNull(result);
        assertFalse(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
        assertFalse(result.contains("ago"));
    }

    @Test
    public void given_playingToday_when_getDisplayDate_Then_ReturnsRelativePlayingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing, "1 hour ago");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingMoreThan24hoursago_when_getDisplayDate_Then_ReturnsAbsolutePlayingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing, "Yesterday");

        // Assert
        assertNotNull(result);
        assertTrue(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
    }

    @Test
    public void given_playingMoreThanAWeekAgo_when_getDisplayDate_Then_ReturnsAbsolutePlayingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_DAY * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing, "");

        // Assert
        assertNotNull(result);
        assertFalse(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
        assertFalse(result.contains("ago"));
    }

    @Test
    public void given_waitingToday_when_getDisplayDate_Then_ReturnsRelativeWaitingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting, "1 hour ago");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingMoreThan24hoursago_when_getDisplayDate_Then_ReturnsAbsoluteWaitingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_HOUR * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting, "Yesterday");

        // Assert
        assertNotNull(result);
        assertTrue(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
    }

    @Test
    public void given_waitingMoreThanAWeekAgo_when_getDisplayDate_Then_ReturnsAbsoluteWaitingTime() throws Exception {

        long currentTime = System.currentTimeMillis() - AlarmManager.INTERVAL_DAY * 30;
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext());
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting, "");

        // Assert
        assertNotNull(result);
        assertFalse(result.matches("^(" + expected + ").*[0-9].*(AM|PM)"));
        assertFalse(result.contains("ago"));
    }
}