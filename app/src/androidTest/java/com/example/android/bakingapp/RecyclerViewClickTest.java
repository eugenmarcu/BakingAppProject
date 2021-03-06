package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecyclerViewClickTest {

    private final String RECIPE = "Cheesecake";
    private final int CHEESECAKE_POSITION = 3;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void clickOnRecyclerView() {
        onView(allOf(ViewMatchers.withId(R.id.rv_list), hasFocus())).perform(RecyclerViewActions.scrollToPosition(CHEESECAKE_POSITION));
        onView(withText(RECIPE)).perform(click());

        onView(withId(R.id.recipe_name)).check(matches(withText(RECIPE)));
    }
}
