package com.stoudyoz.mareu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private List<Evenement> evenements = new ArrayList<>(Arrays.asList(
            new Evenement("12h15", "Peach", "A", "alexandre@lamzone.com"),
            new Evenement("08h35", "Mario", "B", "julie@lamzone.com"),
            new Evenement("16h50", "Luigi", "C", "marc@lamzone.com, fred@lamzone.com")
    ));

    private Evenement nouvelleReunion = new Evenement("14h30", "Mario", "A", "thomas@lamzone.com");

    private Context context;

    @Test
    public void checkIfAddReunionIsWorking() {
        //On verifie que la liste ne contient pas la réunion
        assertFalse(evenements.contains(nouvelleReunion));
        //On ajoute la réunion a la liste
        evenements.add(nouvelleReunion);
        //On verifie que la liste contient la réunion
        assertTrue(evenements.contains(nouvelleReunion));
    }

    @Test
    public void checkIfDeletingReunionIsWorking() {
        //On ajoute une réunion dans liste en reutilisant le test au dessus
        checkIfAddReunionIsWorking();
        //On retire cette réunion de la liste
        evenements.remove(nouvelleReunion);
        //On verifie que la liste ne contient plus la réunion
        assertFalse(evenements.contains(nouvelleReunion));
    }

    @Test
    public void checkIfFilterByLieuIsWorkingNew(){
        List<Evenement> listeFiltreeFromMethod;

        MainActivity mainActivity = new MainActivity();
        listeFiltreeFromMethod = mainActivity.filtreByLieu("Mario", evenements);

        assertEquals(1, listeFiltreeFromMethod.size());
        assertTrue(listeFiltreeFromMethod.contains(evenements.get(1)));
    }

    @Test
    public void checkIfFilterByDateIsWorkingNew(){
        List<Evenement> listeFiltreeFromMethod;

        MainActivity mainActivity = new MainActivity();
        listeFiltreeFromMethod = mainActivity.filterByDate(context, "14h30", "17h25", evenements);

        assertEquals(1, listeFiltreeFromMethod.size());
        assertTrue(listeFiltreeFromMethod.contains(evenements.get(2)));
    }
}

