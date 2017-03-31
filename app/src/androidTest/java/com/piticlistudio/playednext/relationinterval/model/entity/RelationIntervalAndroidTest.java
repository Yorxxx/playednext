package com.piticlistudio.playednext.relationinterval.model.entity;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;

import com.piticlistudio.playednext.BaseAndroidTest;
import com.piticlistudio.playednext.R;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for RelationInterval
 * TODO: Hour checking will fail based on device timezone
 * Created by jorge.garcia on 01/03/2017.
 */
public class RelationIntervalAndroidTest extends BaseAndroidTest {

    Resources res = InstrumentationRegistry.getTargetContext().getResources();

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_HOUR = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_HOUR_24 = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Test
    public void given_noneStatusType_when_getDisplayDate_Then_ReturnsNull() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.NONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);

        // Assert
        assertNull(result);
    }

    @Test
    public void given_completedTodayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed_zero, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedTodayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed_zero, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_startedPlayingTodayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsPlayingToday() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing_start_zero, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_startedPlayingTodayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsPlayingToday() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing_start_zero, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_startedWaitingTodayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsWaitingToday() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting_start_zero, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_startedWaitingTodayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsWaitingToday() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 60);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting_start_zero, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedYesterdayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_completed, 1, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedYesterdayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_completed, 1, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingYesterdayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsPlayingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_playing_start, 1, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingYesterdayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsPlayingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_playing_start, 1, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingYesterdayIntervalIn12hourFormat_when_getDisplayDate_Then_ReturnsWaitingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_waiting_start, 1, SIMPLE_DATE_FORMAT_HOUR.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingYesterdayIntervalIn24hourFormat_when_getDisplayDate_Then_ReturnsWaitingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 61);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, true);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_waiting_start, 1, SIMPLE_DATE_FORMAT_HOUR_24.format(currentTime));

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedLessThan7Days_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 65);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_completed, 5, 5);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingLessThan7Days_when_getDisplayDate_Then_ReturnsPlayingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 65);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_playing_start, 5, 5);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingLessThan7Days_when_getDisplayDate_Then_ReturnsWaitingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 65);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = res.getQuantityString(R.plurals.interval_displaydate_waiting_start, 5, 5);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_completedLongAgo_when_getDisplayDate_Then_ReturnsCompletionTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 85);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed_longago, "03/01/17");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingLongAgo_when_getDisplayDate_Then_ReturnsPlayingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 85);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing_start_longago, "03/01/17");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingLongAgo_when_getDisplayDate_Then_ReturnsWaitingTime() throws Exception {

        long currentTime = 1488387610545L; // 03/01/2017 17:00PM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.DAY_OF_YEAR, 85);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting_start_longago, "03/01/17");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_endedCompleted_when_getDisplayDate_Then_ReturnsEndedCompletionInHours() throws Exception {

        Calendar calendar = Calendar.getInstance();
        long startAt = 1488387610545L; // 03/01/2017 17:00PM
        long endAt = 1488446764545L; // 03/02/2017 9:26AM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.DONE, startAt);
        interval.setEndAt(endAt);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_completed_ended, "03/01/17");

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingCompletedLessThanADay_when_getDisplayDate_Then_ReturnsPlaytimeInHours() throws Exception {

        Calendar calendar = Calendar.getInstance();
        long startAt = 1488387610545L; // 03/01/2017 17:00PM
        long endAt = 1488446764545L; // 03/02/2017 9:26AM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, startAt);
        interval.setEndAt(endAt);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing_ended_hours,
                "03/01/17",
                "03/02/17", 16);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_playingCompletedMoreThanADay_when_getDisplayDate_Then_ReturnsPlaytimeInDays() throws Exception {

        Calendar calendar = Calendar.getInstance();
        long startAt = 1488387610545L; // 03/01/2017 17:00PM
        long endAt = 1489104000545L; // 03/10/2017 12:00AM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PLAYING, startAt);
        interval.setEndAt(endAt);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_playing_ended_days,
                "03/01/17",
                "03/10/17", 8);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingIntervalLowerThanADay_when_getDisplayDate_Then_ReturnsWaitTimeInHours() throws Exception {

        Calendar calendar = Calendar.getInstance();
        long startAt = 1488387610545L; // 03/01/2017 17:00PM
        long endAt = 1488446764545L; // 03/02/2017 9:26AM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, startAt);
        interval.setEndAt(endAt);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting_ended_hours,
                "03/01/17",
                "03/02/17", 16);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void given_waitingIntervalGreaterThanADay_when_getDisplayDate_Then_ReturnsWaitTimeInDays() throws Exception {

        Calendar calendar = Calendar.getInstance();
        long startAt = 1488387610545L; // 03/01/2017 17:00PM
        long endAt = 1489104000545L; // 03/10/2017 9:26AM
        RelationInterval interval = RelationInterval.create(10, RelationInterval.RelationType.PENDING, startAt);
        interval.setEndAt(endAt);

        // Act
        String result = interval.getDisplayDate(InstrumentationRegistry.getTargetContext(), calendar, false);
        String expected = InstrumentationRegistry.getTargetContext().getString(R.string.interval_displaydate_waiting_ended_days, "03/01/17",
                "03/10/17", 8);

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }
}