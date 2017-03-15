package com.piticlistudio.playednext;

import android.graphics.Rect;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class CustomMatchers {

    public static Matcher<View> withHint(final String expectedHint) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                String hint = ((EditText) view).getHint().toString();

                return expectedHint.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static Matcher<View> withAlpha(final float expectedAlpha) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                float alpha = view.getAlpha();

                return alpha == expectedAlpha;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    /**
     * A Custom matcher that check the visibility of the view
     * Checks the Visibility state and the alpha state
     *
     * @return a matcher
     */
    public static Matcher<View> isNotVisible() {
        return isVisibleToUser(false);
    }

    /**
     * Custom matcher that checks the effective visibilify of the view.
     * To be applicable for truly visible should
     * <ul>
     * <li>View's visible rect should be within bounds</li>
     * <li>View's alpha should be 1 or View's visibility should be VISIBLE</li>
     * </ul>
     * If any of the requirements is not true, the view is considered as not visible
     *
     * @param visible boolean indicating the expected view visibility
     * @return the matcher
     */
    public static Matcher<View> isVisibleToUser(boolean visible) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                float alpha = item.getAlpha();
                int visibility = item.getVisibility();
                if (visible) {
                    return item.getGlobalVisibleRect(new Rect()) && (alpha == 1 || visibility == View.VISIBLE);
                }
                return alpha == 0 || visibility == View.INVISIBLE || visibility == View.GONE;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    /**
     * A Custom matcher to check the iconified status of the searchview
     *
     * @param iconified true or false based for the assertion
     * @return a matcher
     */
    public static Matcher<View> isSearchIconified(boolean iconified) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof SearchView))
                    return false;
                SearchView searchView = (SearchView) view;
                return searchView.isIconified() == iconified;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with search iconified: " + iconified);
            }
        };
    }

    public static Matcher<View> withError(CharSequence error) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText editText = (EditText) view;
                return error.equals(editText.getError());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with error: " + error);
            }
        };
    }
}
