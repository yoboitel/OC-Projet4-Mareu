package com.stoudyoz.mareu;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void checkIfOpenPopupIsWorking() {
        //Clique sur le fab
        onView(withId(R.id.fab)).perform(click());
        // Verifie que le edittext est bien affiché, donc que la popup est bien ouverte
        onView(ViewMatchers.withId(R.id.editTextSujet)).check(matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkIfAddReunionIsWorking() {
        //Clique sur le fab
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.button)).perform(click());
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(1)));
    }

    @Test
    public void checkIfDeletingReunionIsWorking() {
        //Clique sur le fab
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.button)).perform(click());
        // Clique sur le bouton supprimmer du premier item
        onView(ViewMatchers.withId(R.id.rcEvenements))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(0)));
    }
}