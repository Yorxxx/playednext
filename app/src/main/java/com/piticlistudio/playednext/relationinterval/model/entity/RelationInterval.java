package com.piticlistudio.playednext.relationinterval.model.entity;


import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.piticlistudio.playednext.R;
import com.piticlistudio.playednext.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Entity defining a relation status interval
 * Created by jorge.garcia on 30/01/2017.
 */
@AutoValue
public abstract class RelationInterval {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_HOUR = new SimpleDateFormat("h:mm a", Locale.getDefault());
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT_HOUR_24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
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
     * @param calendar       the current calendar
     * @param is24HourFormat boolean indicating if hours should be displayed in 24 hour format or not (ie 17:00 vs 5:00 PM)
     * @return the display date
     */
    @Nullable
    public String getDisplayDate(Context context, Calendar calendar, boolean is24HourFormat) {

        if (type() == RelationType.NONE)
            return null;

        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startAt());
//
//        Calendar end = Calendar.getInstance();
//        end.setTimeInMillis(getEndAt());

        if (endAt == 0) {
            if (calendar.get(Calendar.YEAR) == start.get(Calendar.YEAR)) {
                if (currentDay - start.get(Calendar.DAY_OF_YEAR) <= 7) {
                    int daysDiff = currentDay - start.get(Calendar.DAY_OF_YEAR);

                    String hourMessage;
                    if (daysDiff > 1) {
                        hourMessage = String.valueOf(daysDiff);
                    } else {
                        if (is24HourFormat) {
                            hourMessage = SIMPLE_DATE_FORMAT_HOUR_24.format(new Date(startAt()));
                        } else {
                            hourMessage = SIMPLE_DATE_FORMAT_HOUR.format(new Date(startAt()));
                        }
                    }
                    int pluralsRes = 0;
                    int stringRes = 0;
                    switch (type()) {
                        case DONE:
                            pluralsRes = R.plurals.interval_displaydate_completed;
                            stringRes = R.string.interval_displaydate_completed_zero;
                            break;
                        case PLAYING:
                            pluralsRes = R.plurals.interval_displaydate_playing_start;
                            stringRes = R.string.interval_displaydate_playing_start_zero;
                            break;
                        case PENDING:
                            pluralsRes = R.plurals.interval_displaydate_waiting_start;
                            stringRes = R.string.interval_displaydate_waiting_start_zero;
                            break;
                    }

                    return StringUtils.getQuantityStringZero(context.getResources(), pluralsRes, stringRes, daysDiff, hourMessage);
                }
            }
            String dateFormat = context.getString(R.string.interval_displaydate_long);
            SimpleDateFormat df = new SimpleDateFormat(dateFormat, getCurrentLocale(context));
            switch (type()) {
                case DONE:
                    return context.getString(R.string.interval_displaydate_completed_longago, df.format(new Date(startAt())));
                case PLAYING:
                    return context.getString(R.string.interval_displaydate_playing_start_longago, df.format(new Date(startAt())));
                case PENDING:
                    return context.getString(R.string.interval_displaydate_waiting_start_longago, df.format(new Date(startAt())));
            }
        }
        else {
            String dateFormat = context.getString(R.string.interval_displaydate_long);
            SimpleDateFormat df = new SimpleDateFormat(dateFormat, getCurrentLocale(context));
            long hoursDiffInMs = getEndAt() - startAt();
            int hours = (int)(hoursDiffInMs/ AlarmManager.INTERVAL_HOUR);
            int days = (int)(hoursDiffInMs/ AlarmManager.INTERVAL_DAY);
            String startDay = df.format(new Date(startAt()));
            String endDay = df.format(new Date(getEndAt()));
            if (days == 0) {
                switch (type()) {
                    case DONE:
                        return context.getString(R.string.interval_displaydate_completed_ended, startDay);
                    case PLAYING:
                        return context.getString(R.string.interval_displaydate_playing_ended_hours, startDay, endDay, hours);
                    case PENDING:
                        return context.getString(R.string.interval_displaydate_waiting_ended_hours, startDay, endDay, hours);
                }
            }
            else {
                switch (type()) {
                    case DONE:
                        return context.getString(R.string.interval_displaydate_completed_ended, startDay);
                    case PLAYING:
                        return context.getString(R.string.interval_displaydate_playing_ended_days, startDay, endDay, days);
                    case PENDING:
                        return context.getString(R.string.interval_displaydate_waiting_ended_days, startDay, endDay, days);
                }
            }
        }
        String dateFormat = context.getString(R.string.interval_displaydate_long);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, getCurrentLocale(context));
        return df.format(new Date(startAt()));
    }

    /**
     * Returns the DateFormat appropiated for this device to map dates into hours/minutes
     *
     * @param context application's context.
     * @return the appropiated format.
     */
    SimpleDateFormat getDateHourFormat(Context context) {
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            return SIMPLE_DATE_FORMAT_HOUR_24;
        } else {
            return SIMPLE_DATE_FORMAT_HOUR;
        }
    }

    private Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    public enum RelationType {
        NONE, PENDING, PLAYING, DONE
    }
}
