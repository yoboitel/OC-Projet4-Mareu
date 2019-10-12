package com.stoudyoz.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);

        recyclerView = findViewById(R.id.rcEvenements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        evenements = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open Dialog
                showPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_date) {
            //If user pick date filter
            if (!(evenements.isEmpty())) {
                BottomSheetDate bottomSheetdate = new BottomSheetDate();
                bottomSheetdate.show(getSupportFragmentManager(), "BottomSheet");
            }

            return true;
        } else if (id == R.id.action_lieu) {
            //If user pick lieu filter
            if (!(evenements.isEmpty())) {
                BottomSheetLieu bottomSheetlieu = new BottomSheetLieu();
                bottomSheetlieu.show(getSupportFragmentManager(), "BottomSheet");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterByLieu(String salle) {

            //On crée une liste avec seulement les réunions correspondant au filtre appliqué
            List<Evenement> ListeFiltreeParLieu = new ArrayList<>();
            for (Evenement evenement : evenements) {
                if (evenement.getLieu().equals(salle))
                    ListeFiltreeParLieu.add(evenement);
            }
            adapter = new MyEvenementAdapter(ListeFiltreeParLieu, context);
            recyclerView.setAdapter(adapter);
        }

    public void filterByDate(String minTimeFromPicker, String maxTimeFromPicker) {

        //On crée une liste avec seulement les réunions correspondant au filtre appliqué
        List<Evenement> ListeFiltreeParDate = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("hh'h'mm");

        Date minDate, maxDate, dateEvenement;

        try{
        minDate = dateFormat.parse(minTimeFromPicker);
        maxDate = dateFormat.parse(maxTimeFromPicker);

        Log.d("avancement", "minFormat " + minDate + " et maxFormat" + maxDate);

        for (Evenement evenement : evenements) {


            dateEvenement = dateFormat.parse(evenement.getTime());


            if (dateEvenement.equals(minDate) || dateEvenement.equals(maxDate) || (dateEvenement.after(minDate) && dateEvenement.before(maxDate))) {
                ListeFiltreeParDate.add(evenement);
            }

        }
    }catch (ParseException e) {
            e.printStackTrace();
            Log.d("avancement", "fock");
        }

        adapter = new MyEvenementAdapter(ListeFiltreeParDate, context);
        recyclerView.setAdapter(adapter);
    }

    public void removeFilter(){
        adapter = new MyEvenementAdapter(evenements, context);
        recyclerView.setAdapter(adapter);
    }

    //GERE LA POPUP
    public void showPopup() {

        nachoParticipants = dialog.findViewById(R.id.nachoParticipants);
        nachoParticipants.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoParticipants.enableEditChipOnTouch(false, true);

        editTextHeure = dialog.findViewById(R.id.editTextHeure);
        editTextLieu = dialog.findViewById(R.id.editTextLieu);
        editTextSujet = dialog.findViewById(R.id.editTextSujet);
        ajouter = dialog.findViewById(R.id.button);
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

        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextHeure.getText()) || TextUtils.isEmpty(editTextSujet.getText()) || TextUtils.isEmpty(nachoParticipants.getText()))
                    Toast.makeText(MainActivity.this, "Veuillez remplir touts les champs", Toast.LENGTH_SHORT).show();
                else {
                    List<String> allNachos = new ArrayList<>();
                    String listString = "";
                    //AJOUTE TOUT LES CHIPS ENSEMBLE
                    for (Chip chip : nachoParticipants.getAllChips()) {
                        CharSequence text = chip.getText();
                        allNachos.add(text.toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            listString = String.join(", ", allNachos);
                        }
                    }
                    //If user forget to add a space to transform his text in chip, it's still added in the reunion informations
                    if (nachoParticipants.getAllChips().isEmpty() && (!(nachoParticipants.getText().toString().isEmpty()))){
                        listString = nachoParticipants.getText().toString();
                    }

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

    //AJOUTE LE 0 AVANT L'HEURE SI BESOIN
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}