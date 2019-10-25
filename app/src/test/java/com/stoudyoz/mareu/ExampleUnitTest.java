package com.stoudyoz.mareu;

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
    public void checkIfFiltreParLieuIsWorking() {
        //On crée la string de la salle qui sera choisie pour le filtre
        String salle = "Mario";
        //On crée la liste qui accueillera les réunions filtrées
        List<Evenement> ListeFiltreeParLieu = new ArrayList<>();
        //On parcours la liste de réunions et on ajoute les réunions avec la salle "Mario" dans "ListeFiltreeParLieu"
        for (Evenement evenement : evenements) {
            if (evenement.getLieu().equals(salle))
                ListeFiltreeParLieu.add(evenement);
        }
        //On vérifie qu'une seule réunion à bien été trouvée
        assertEquals(1, ListeFiltreeParLieu.size());
        //On vérifie que la seule réunion trouvée est bien celle en position 1 de la liste "evenements"
        assertTrue(ListeFiltreeParLieu.contains(evenements.get(1)));
    }

    @Test
    public void checkIfFiltreParDateIsWorking() {
        //On crée les strings des heures min et max qui nous servirons pour le filtre
        String heureMin = "14h30";
        String heureMax = "17h25";
        //On crée la liste qui acceuillera les réunions filtrées
        List<Evenement> ListeFiltreeParDate = new ArrayList<>();
        //On transforme la string en Date pour pouvoir comparer
        DateFormat dateFormat = new SimpleDateFormat("hh'h'mm");
        //On crée les variables Date;
        Date minDate, maxDate, dateEvenement;

        try {
            //On transforme en Date les Strings des heures choisies pour le filtre
            minDate = dateFormat.parse(heureMin);
            maxDate = dateFormat.parse(heureMax);

            //On boucle dans la liste de réunions
            for (Evenement evenement : evenements) {
                //On transforme la String de la date de la réunion parcourue en Date
                dateEvenement = dateFormat.parse(evenement.getTime());
                //On garde uniquement les réunions inclusent dans la plage horaire
                if (dateEvenement.equals(minDate) || dateEvenement.equals(maxDate) || (dateEvenement.after(minDate) && dateEvenement.before(maxDate))) {
                    ListeFiltreeParDate.add(evenement);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //On vérifie qu'une seule réunion à bien été trouvée
        assertEquals(1, ListeFiltreeParDate.size());
        //On vérifie que la seule réunion trouvée est bien celle en position 2 de la liste "evenements"
        assertTrue(ListeFiltreeParDate.contains(evenements.get(2)));

    }
}

