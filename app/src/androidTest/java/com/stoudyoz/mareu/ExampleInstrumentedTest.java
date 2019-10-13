package com.stoudyoz.mareu;

import android.widget.TimePicker;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
        //Rempli l'editText du sujet
        onView(withId(R.id.editTextSujet)).perform(typeText("A"));
        //Choisi une date
        onView(withId(R.id.editTextHeure)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12,15));
        onView(withId(android.R.id.button1)).perform(click());
        //Rempli l'editText des participants
        onView(withId(R.id.nachoParticipants)).perform(typeText("alexandre@lamzone.com "));
        //Clique sur le bouton d'ajout
        onView(withId(R.id.buttonAdd)).perform(click());
        //Vérifie que le recyclerview des réunions à bien une réunion
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(1)));
    }

    @Test
    public void checkIfDeletingReunionIsWorking() {
        //Ajoute une réunion en utilisant le test d'au dessus
        checkIfAddReunionIsWorking();
        //Clique sur le bouton supprimmer du premier item
        onView(withId(R.id.ivDelete)).perform(click());
        //Vérifie que le recyclerview des réunions n'en contient aucune
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(0)));
    }

    @Test
    public void checkIfFiltreParLieuIsWorking() throws InterruptedException {
        //On ajoute une réunion
        checkIfAddReunionIsWorking();
        //Click sur les settings de la toolbar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        //Click sur le menu pour filtrer par lieu
        onView(withText("Filtrer par lieu")).perform(click());
        //Attends 0.5s que la bottomsheet soit bien visible pour cliquer
        Thread.sleep(500);
        //Filtre par salle "Mario"
        onView(withId(R.id.buttonSalleMario)).perform(click());
        //Vérifie que le recyclerview des réunions n'en contient plus car la réunion présente avait la salle Peach
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(0)));
        //Click sur les settings de la toolbar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        //Click sur le menu pour filtrer par lieu
        onView(withText("Filtrer par lieu")).perform(click());
        //Attends 0.5s que la bottomsheet soit bien visible pour cliquer
        Thread.sleep(500);
        //Click sur le bouton Reset
        onView(withId(R.id.buttonRemoveFilter)).perform(click());
        //Vérifie que le recyclerview des réunions contient à nouveau la réunion
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(1)));
    }

    @Test
    public void checkIfFiltreParDateIsWorking() throws InterruptedException {
        //On ajoute une réunion
        checkIfAddReunionIsWorking();
        //Click sur les settings de la toolbar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        //Click sur le menu pour filtrer par lieu
        onView(withText("Filtrer par date")).perform(click());
        //Attends 0.5s que la bottomsheet soit bien visible pour cliquer
        Thread.sleep(500);
        //Choisi une heure minimum
        onView(withId(R.id.tvMinTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14,30));
        onView(withId(android.R.id.button1)).perform(click());
        //Choisi une heure maximum
        onView(withId(R.id.tvMaxTime)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(18,00));
        onView(withId(android.R.id.button1)).perform(click());
        //Click sur le bouton Filtrer
        onView(withId(R.id.buttonFiltrerDate)).perform(click());
        //Vérifie que le recyclerview des réunions n'en contient plus car la réunion présente n'est pas dans la plage horaire séléctionnée
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(0)));
        //Click sur les settings de la toolbar
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        //Click sur le menu pour filtrer par date
        onView(withText("Filtrer par date")).perform(click());
        //Attends 0.5s que la bottomsheet soit bien visible pour cliquer
        Thread.sleep(500);
        //Click sur le bouton Reset
        onView(withId(R.id.btnResetFromDateFilter)).perform(click());
        //Vérifie que le recyclerview des réunions contient à nouveau la réunion
        onView(ViewMatchers.withId(R.id.rcEvenements)).check(matches(hasChildCount(1)));
    }

}