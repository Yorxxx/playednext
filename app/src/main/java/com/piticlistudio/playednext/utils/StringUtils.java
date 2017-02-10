package com.piticlistudio.playednext.utils;


import android.support.annotation.Nullable;

import java.util.List;

/**
 * Utility methods for strings
 * Created by jorge.garcia on 12/01/2017.
 */

public class StringUtils {

    // TODO: 12/01/2017 test cases

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
     * @param values the array to stringify.
     * @return the formatted string.
     */
    @Nullable
    public static String stringify(List<String> values) {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<values.size();i++) {
            sb.append(values.get(i));
            if (i < values.size()-1)
                sb.append(", ");
        }
        if (values.size() > 0)
            return sb.toString();
        return null;
    }
}
