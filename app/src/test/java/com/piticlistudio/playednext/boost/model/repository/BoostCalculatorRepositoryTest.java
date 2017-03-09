package com.piticlistudio.playednext.boost.model.repository;

import android.app.AlarmManager;

import com.piticlistudio.playednext.BaseTest;
import com.piticlistudio.playednext.boost.model.entity.BoostItem;
import com.piticlistudio.playednext.boost.model.entity.BoostTypes;
import com.piticlistudio.playednext.boost.model.entity.BoostTypesTest;
import com.piticlistudio.playednext.boost.model.entity.IBoostable;

import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases
 * Created by jorge.garcia on 08/03/2017.
 */
public class BoostCalculatorRepositoryTest extends BaseTest {

    @InjectMocks
    BoostCalculatorRepository repository;

    @Test
    public void given_nonBoostableItem_when_load_Then_ReturnsEmptyList() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(false);

        // Act
        TestObserver<List<BoostItem>> result = repository.load(item).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(check(data -> {
                    assertTrue(data.isEmpty());
                }));
    }

    @Test
    public void given_itemReleased30DaysAgo_When_isReleasedWithin15DaysWindowFrame_Then_ReturnsFalse() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.getLastRelease()).thenReturn(System.currentTimeMillis() - 30*AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isReleasedWithin15DaysWindowFrame(item);

        // Assert
        assertFalse(result);
    }

    @Test
    public void given_itemReleased10DaysAgo_When_isReleasedWithin15DaysWindowFrame_Then_ReturnsTrue() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.getLastRelease()).thenReturn(System.currentTimeMillis() - 10*AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isReleasedWithin15DaysWindowFrame(item);

        // Assert
        assertTrue(result);
    }

    @Test
    public void given_itemNotYetReleased_When_isReleasedWithin15DaysWindowFrame_Then_ReturnsFalse() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.getLastRelease()).thenReturn(System.currentTimeMillis() + 10*AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isReleasedWithin15DaysWindowFrame(item);

        // Assert
        assertFalse(result);
    }

    @Test
    public void given_boostItemNotReleasedRecently_When_Load_Then_ReturnsEmptyList() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        when(item.getLastRelease()).thenReturn(1000L);

        // Act
        TestObserver<List<BoostItem>> result = repository.load(item).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(check(data -> {
                    assertTrue(data.isEmpty());
                }));
    }

    @Test
    public void given_boostItemReleasedADayAgo_When_Load_Then_ReturnsListWithBoost() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        when(item.getLastRelease()).thenReturn(System.currentTimeMillis() - AlarmManager.INTERVAL_DAY);

        // Act
        TestObserver<List<BoostItem>> result = repository.load(item).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(check(data -> {
                    assertFalse(data.isEmpty());
                    BoostItem boost = data.get(0);
                    assertEquals(BoostTypes.RECENTLY_RELEASED.id, boost.type());
                    assertEquals(BoostTypes.RECENTLY_RELEASED.value, boost.value());
                }));
    }

    @Test
    public void given_itemToBeReleased_When_isTenYearsLaunchCelebration_Then_ReturnsFalse() throws Exception {
        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        when(item.getFirstRelease()).thenReturn(System.currentTimeMillis() + AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isTenYearsLaunchCelebration(item);

        // Assert
        assertFalse(result);

    }

    @Test
    public void given_itemReleasedTenYearAndTwoMonthsAgo_When_isTenYearsLaunchCelebration_Then_ReturnsFalse() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        long year = 365*AlarmManager.INTERVAL_DAY;
        when(item.getFirstRelease()).thenReturn(System.currentTimeMillis() - 10*year-60*AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isTenYearsLaunchCelebration(item);

        // Assert
        assertFalse(result);
    }

    @Test
    public void given_itemReleasedTenYearsAgo_When_isTenYearsLaunchCelebration_Then_ReturnsTrue() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        long year = 365*AlarmManager.INTERVAL_DAY;
        when(item.getFirstRelease()).thenReturn(System.currentTimeMillis() - 10*year-20*AlarmManager.INTERVAL_DAY);

        // Act
        boolean result = repository.isTenYearsLaunchCelebration(item);

        // Assert
        assertTrue(result);
    }

    @Test
    public void given_tenYearCelebration_When_Load_Then_ReturnsListWithBoost() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        long year = 365*AlarmManager.INTERVAL_DAY;
        when(item.getFirstRelease()).thenReturn(System.currentTimeMillis() - 10*year-AlarmManager.INTERVAL_DAY);

        // Act
        TestObserver<List<BoostItem>> result = repository.load(item).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(check(data -> {
                    assertFalse(data.isEmpty());
                    BoostItem boost = data.get(0);
                    assertEquals(BoostTypes.TEN_YEARS_CELEBRATION.id, boost.type());
                    assertEquals(BoostTypes.TEN_YEARS_CELEBRATION.value, boost.value());
                }));
    }

    @Test
    public void given_waitingTenDaysAgo_When_getNumberOfDaysWaiting_Then_ReturnsTen() throws Exception {
        IBoostable item = mock(IBoostable.class);
        when(item.getWaitingStartedAt()).thenReturn(System.currentTimeMillis() - 10*AlarmManager.INTERVAL_DAY);

        // Act
        long result = repository.getNumberOfDaysWaiting(item);

        // Assert
        assertEquals(10L, result);
    }

    @Test
    public void given_waitingItem_When_Load_Then_ReturnsListWithBoostItem() throws Exception {

        IBoostable item = mock(IBoostable.class);
        when(item.isBoostEnabled()).thenReturn(true);
        when(item.getWaitingStartedAt()).thenReturn(System.currentTimeMillis() - 100*AlarmManager.INTERVAL_DAY);

        // Act
        TestObserver<List<BoostItem>> result = repository.load(item).test();
        result.awaitTerminalEvent();

        // Assert
        result.assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .assertValue(check(data -> {
                    assertFalse(data.isEmpty());
                    BoostItem boost = data.get(0);
                    assertEquals(BoostTypes.WAITING_TIME.id, boost.type());
                    assertEquals(100, boost.value());
                }));
    }
}

