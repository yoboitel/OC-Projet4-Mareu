package com.stoudyoz.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Dialog dialog;
    private EditText editTextHeure, editTextLieu, editTextSujet;
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
            sortByDate();
            return true;
        } else if (id == R.id.action_lieu) {
            sortByLieu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByDate() {

        if (!(evenements.isEmpty())) {

            Collections.sort(evenements, new Comparator<Evenement>() {
                @Override
                public int compare(Evenement o1, Evenement o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    private void sortByLieu() {

        if (!(evenements.isEmpty())) {

            Collections.sort(evenements, new Comparator<Evenement>() {
                @Override
                public int compare(Evenement o1, Evenement o2) {
                    return o1.getLieu().compareTo(o2.getLieu());
                }
            });
            adapter.notifyDataSetChanged();

        }

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
                }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });
        
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextHeure.getText()) || TextUtils.isEmpty(editTextLieu.getText()) || TextUtils.isEmpty(editTextSujet.getText()) || TextUtils.isEmpty(nachoParticipants.getText()))
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
                    Evenement nouvelEvenement = new Evenement(editTextHeure.getText().toString(), editTextLieu.getText().toString(), editTextSujet.getText().toString(), listString);

                    evenements.add(nouvelEvenement);
                    adapter = new MyEvenementAdapter(evenements, context);
                    recyclerView.setAdapter(adapter);
                    editTextHeure.setText("");
                    editTextLieu.setText("");
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
