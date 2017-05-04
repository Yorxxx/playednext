package com.piticlistudio.playednext.relationinterval.model.entity;


import android.app.AlarmManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.R;

import java.util.concurrent.TimeUnit;

/**
 * Entity defining a relation status interval
 * Created by jorge.garcia on 30/01/2017.
 */
@AutoValue
public abstract class RelationInterval {

    private long endAt;

    public static RelationInterval create(int id, RelationType type, long createdAt) {
        return new AutoValue_RelationInterval(id, type, createdAt);
    }

    public abstract int id();

    public abstract RelationType type();

    public abstract long startAt();

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    /**
     * Returns the display message date of the interval.
     * This includes the type of the interval plus the start or end of it (depending if it has ended or not)
     *
     * @param context        application's context. Needed for accessing resources
     */
    @Nullable
    public String getDisplayDate(Context context) {

        int stringRes = 0;
        switch (type()) {
            case DONE:
                stringRes = R.string.interval_displaydate_completed;
                break;
            case PLAYING:
                stringRes = R.string.interval_displaydate_playing;
                break;
            case PENDING:
                stringRes = R.string.interval_displaydate_waiting;
                break;
            default:
                return null;
        }
        long diff = System.currentTimeMillis() -startAt();
        if (diff <= AlarmManager.INTERVAL_HOUR*24) {
            String value = DateUtils.getRelativeTimeSpanString(startAt(), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_NO_YEAR).toString();

            return context.getString(stringRes, value);
        }
        else if (diff <= AlarmManager.INTERVAL_DAY*7) {
            String value = DateUtils.getRelativeDateTimeString(context,
                    startAt(),
                    DateUtils.HOUR_IN_MILLIS,
                    DateUtils.WEEK_IN_MILLIS,
                    DateUtils.FORMAT_NO_YEAR).toString();
            return context.getString(stringRes, value);
        }
        else {
            String value = DateUtils.getRelativeTimeSpanString(context, startAt(), true).toString();
            return context.getString(stringRes, value);
        }
    }

    /**
     * Returns the number of hours the interval has.
     * If interval has not ended, will use up to current time
     * @return the number of hours
     */
    public double getHours() {
        long max = endAt;
        if (max == 0)
            max = System.currentTimeMillis();
        long value = max-startAt();
        return value/ TimeUnit.HOURS.toMillis(1);
    }

    public enum RelationType {
        NONE, PENDING, PLAYING, DONE
    }
}
