package com.piticlistudio.playednext.utils;


import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;

import java.util.List;

/**
 * Utility methods for strings
 * Created by jorge.garcia on 12/01/2017.
 */

public class StringUtils {

    /**
     * Safety string comparison with null checking.
     *
     * @param one the first string to compare.
     * @param two the second string to compare.
     * @return true if one is equal to two (ignoring cases). False otherwise.
     */
    public static boolean equalsIgnoreCase(String one, String two) {
        return !(one == null && two != null) && !(two == null && one != null) && (two == null || two.equalsIgnoreCase(one));
    }

    /**
     * Formats the array to show as standard string (ie without brackets).
     *
     * @param values the array to stringify.
     * @return the formatted string.
     */
    @Nullable
    public static String stringify(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1)
                sb.append(", ");
        }
        if (values.size() > 0)
            return sb.toString();
        return null;
    }

    /**
     * Convenience method to retrieve quantity strings.
     * Android uses CLDR plurals system, so it means that the value zero does not represent the number, but the keyword category.
     *
     * @param resources   the resources
     * @param pluralResId the plural resource id
     * @param zeroResId   the zero string resource id
     * @param quantity    the quantity
     * @param formatArgs  the arguments
     * @return the string quantiy.
     */
    public static String getQuantityStringZero(Resources resources, @PluralsRes int pluralResId, @StringRes int zeroResId, int quantity,
                                               Object... formatArgs) {
        if (quantity == 0) {
            return resources.getString(zeroResId, formatArgs);
        } else {
            return resources.getQuantityString(pluralResId, quantity, formatArgs);
        }
    }
}
