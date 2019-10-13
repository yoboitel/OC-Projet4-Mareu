package com.stoudyoz.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Initialisation
    private Dialog dialog;
    private Spinner editTextLieu;
    private EditText editTextHeure, editTextSujet;
    private Button ajouter;
    NachoTextView nachoParticipants;
    static RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    static List<Evenement> evenements;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;

        //Initialisation de la popup pour ajouter une réunion
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);

        recyclerView = findViewById(R.id.rcEvenements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        evenements = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Montre la dialog pour ajouter une réunion
                showPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate le menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_date) {
            //Si l'user choisit de filtrer par date
            if (!(evenements.isEmpty())) {
                BottomSheetDate bottomSheetdate = new BottomSheetDate();
                bottomSheetdate.show(getSupportFragmentManager(), "BottomSheet");
            }
            return true;
        } else if (id == R.id.action_lieu) {
            //Si l'user choisit de filtrer par lieu
            if (!(evenements.isEmpty())) {
                BottomSheetLieu bottomSheetlieu = new BottomSheetLieu();
                bottomSheetlieu.show(getSupportFragmentManager(), "BottomSheet");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Gere le filtre de réunion par lieu
    public void filterByLieu(String salle) {

            //On crée une liste dans laquelle on ajoute seulement les réunions correspondant au filtre appliqué
            List<Evenement> ListeFiltreeParLieu = new ArrayList<>();
            for (Evenement evenement : evenements) {
                if (evenement.getLieu().equals(salle))
                    ListeFiltreeParLieu.add(evenement);
            }
            adapter = new MyEvenementAdapter(ListeFiltreeParLieu, context);
            recyclerView.setAdapter(adapter);
        }

    //Gere le filtre de réunion par date
    public void filterByDate(Context contextfromfrag, String minTimeFromPicker, String maxTimeFromPicker) {

        //On crée une liste dans laquelle on ajoute seulement les réunions inclusent dans la plage horaire du filtre
        List<Evenement> ListeFiltreeParDate = new ArrayList<>();

        //On transforme la string en Date pour pouvoir comparer
        DateFormat dateFormat = new SimpleDateFormat("hh'h'mm");

        Date minDate, maxDate, dateEvenement;

        try{
        minDate = dateFormat.parse(minTimeFromPicker);
        maxDate = dateFormat.parse(maxTimeFromPicker);

        //Si l'heure max et inférieur à l'heure min
        if (maxDate.before(minDate)){
            Toast.makeText(contextfromfrag, "L'heure maximum doit être supérieur au minimum", Toast.LENGTH_SHORT).show();
        } else {

        //On boucle dans la liste de réunions
        for (Evenement evenement : evenements) {
            //On transforme la date en string de la réunion en Date
            dateEvenement = dateFormat.parse(evenement.getTime());
            //On garde celles inclusent dans la plage horaire
            if (dateEvenement.equals(minDate) || dateEvenement.equals(maxDate) || (dateEvenement.after(minDate) && dateEvenement.before(maxDate))) {
                ListeFiltreeParDate.add(evenement);
            }
        }

        adapter = new MyEvenementAdapter(ListeFiltreeParDate, context);
        recyclerView.setAdapter(adapter);
        }
    }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Reset le filtre de réunion
    public void removeFilter(){
        adapter = new MyEvenementAdapter(evenements, context);
        recyclerView.setAdapter(adapter);
    }

    //Gere la popup pour ajouter une réunion
    public void showPopup() {

        //Configuration des chips
        nachoParticipants = dialog.findViewById(R.id.nachoParticipants);
        nachoParticipants.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoParticipants.enableEditChipOnTouch(false, true);

        //Initialisation des widgets du dialog
        editTextHeure = dialog.findViewById(R.id.editTextHeure);
        editTextLieu = dialog.findViewById(R.id.editTextLieu);
        editTextSujet = dialog.findViewById(R.id.editTextSujet);
        ajouter = dialog.findViewById(R.id.buttonAdd);

        //Gère le click pour selectionner l'heure d'une réunion
        editTextHeure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        editTextHeure.setText(checkDigit(hourOfDay) + "h" + checkDigit(minutes));
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        //Gère le click pour enregistrer la réunion
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Vérifie que toute les informations d'une réunion sont remplies
                if (TextUtils.isEmpty(editTextHeure.getText()) || TextUtils.isEmpty(editTextSujet.getText()) || TextUtils.isEmpty(nachoParticipants.getText()))
                    Toast.makeText(MainActivity.this, "Veuillez remplir touts les champs", Toast.LENGTH_SHORT).show();
                else {

                    List<String> allNachos = new ArrayList<>();
                    String listString = "";
                    //On ajoute toutes les chips dans une liste
                    for (Chip chip : nachoParticipants.getAllChips()) {
                        CharSequence text = chip.getText();
                        allNachos.add(text.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            listString = String.join(", ", allNachos);
                        }
                    }
                    //Si l'user ajoute un participants sans appuyer sur espace pour le transformer en chip, on l'ajoute quand même
                    if (nachoParticipants.getAllChips().isEmpty() && (!(nachoParticipants.getText().toString().isEmpty()))){
                        listString = nachoParticipants.getText().toString();
                    }

                    //On crée l'objet de la réunion avec les informations
                    Evenement nouvelEvenement = new Evenement(editTextHeure.getText().toString(), editTextLieu.getSelectedItem().toString(), editTextSujet.getText().toString(), listString);

                    evenements.add(nouvelEvenement);
                    adapter = new MyEvenementAdapter(evenements, context);
                    recyclerView.setAdapter(adapter);
                    editTextHeure.setText("");
                    editTextSujet.setText("");
                    nachoParticipants.setText("");
                    dialog.dismiss();
                }
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //Ajoute un 0 devant l'heure du DatePicker si necéssaire
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}