package com.piticlistudio.playednext.boost.model.repository;

import android.app.AlarmManager;

import com.piticlistudio.playednext.boost.model.entity.BoostItem;
import com.piticlistudio.playednext.boost.model.entity.BoostTypes;
import com.piticlistudio.playednext.boost.model.entity.IBoostable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Repository for boosts
 * Created by jorge.garcia on 08/03/2017.
 */
public class BoostCalculatorRepository implements IBoostCalculatorRepository {

    /**
     * Loads the boost items applicable for the specified item
     *
     * @param item the item to calculate its boost
     * @return an Observable that emits the list of boostItems
     */
    @Override
    public Observable<List<BoostItem>> load(IBoostable item) {
        return Observable.just(item)
                .map(new Function<IBoostable, List<BoostItem>>() {
                    @Override
                    public List<BoostItem> apply(@NonNull IBoostable iBoostable) throws Exception {
                        List<BoostItem> result = new ArrayList<>();
                        if (!item.isBoostEnabled()) {
                            return result;
                        }
                        if (isReleasedWithin15DaysWindowFrame(item))
                            result.add(BoostItem.create(BoostTypes.RECENTLY_RELEASED.value, BoostTypes.RECENTLY_RELEASED.id));
                        if (isTenYearsLaunchCelebration(item))
                            result.add(BoostItem.create(BoostTypes.TEN_YEARS_CELEBRATION.value, BoostTypes.TEN_YEARS_CELEBRATION.id));
                        if (item.getWaitingStartedAt() > 0)
                            result.add(BoostItem.create(getNumberOfDaysWaiting(item), BoostTypes.WAITING_TIME.id));
                        long completedCount = item.getCompletedCount();
                        if (completedCount > 0) {
                            result.add(BoostItem.create(completedCount*BoostTypes.COMPLETED_COUNT.value, BoostTypes.COMPLETED_COUNT.id));
                        }
                        return result;
                    }
                });
    }

    /**
     * Returns if the item has been released less that 15 days ago
     *
     * @param item the item to check
     * @return true if was released recently. False otherwise
     */
    boolean isReleasedWithin15DaysWindowFrame(IBoostable item) {
        long interval = System.currentTimeMillis() - item.getLastRelease();
        return interval < 15 * AlarmManager.INTERVAL_DAY && interval > 0;
    }

    /**
     * Returns if the item has been released ten years ago (with a month of window frame)
     *
     * @param item the item to check
     * @return true or false
     */
    boolean isTenYearsLaunchCelebration(IBoostable item) {
        long interval = System.currentTimeMillis() - item.getFirstRelease();
        long min = 10 * (365 * AlarmManager.INTERVAL_DAY);
        long max = min + 30 * AlarmManager.INTERVAL_DAY;
        return interval >= min && interval < max;
    }

    /**
     * Returns the number of days the item is waiting
     *
     * @param item the item to check
     * @return the number of days in the todo list
     */
    long getNumberOfDaysWaiting(IBoostable item) {
        long interval = System.currentTimeMillis() - item.getWaitingStartedAt();
        return interval / AlarmManager.INTERVAL_DAY;
    }
}
