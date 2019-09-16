package com.stoudyoz.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

        recyclerView = (RecyclerView) findViewById(R.id.rcEvenements);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_date) {
            Toast.makeText(this, "Trier par date", Toast.LENGTH_SHORT).show();
            sortByDate();
            return true;
        } else if (id == R.id.action_lieu) {
            Toast.makeText(this, "Trier par lieu", Toast.LENGTH_SHORT).show();
            sortByLieu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByDate() {
        Collections.sort(evenements, new Comparator<Evenement>() {
            @Override
            public int compare(Evenement o1, Evenement o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByLieu() {
        Collections.sort(evenements, new Comparator<Evenement>() {
            @Override
            public int compare(Evenement o1, Evenement o2) {
                return o1.getLieu().compareTo(o2.getLieu());
            }
        });
        adapter.notifyDataSetChanged();
    }

    public void showPopup(){

        nachoParticipants = (NachoTextView) dialog.findViewById(R.id.nachoParticipants);
        nachoParticipants.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoParticipants.enableEditChipOnTouch(false, true);

        editTextHeure = (EditText) dialog.findViewById(R.id.editTextHeure);
        editTextLieu = (EditText) dialog.findViewById(R.id.editTextLieu);
        editTextSujet = (EditText) dialog.findViewById(R.id.editTextSujet);
        ajouter = (Button) dialog.findViewById(R.id.button);

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

                    // Iterate over all of the chips in the NachoTextView
                    for (Chip chip : nachoParticipants.getAllChips()) {
                        // Do something with the text of each chip
                        CharSequence text = chip.getText();
                        allNachos.add(text.toString());
                        listString = String.join(", ", allNachos);
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //AJOUTE LE 0 AVANT L'HEURE SI BESOIN
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}
