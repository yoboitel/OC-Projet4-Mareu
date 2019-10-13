package com.stoudyoz.mareu;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class BottomSheetDate extends BottomSheetDialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add rounded corners to the bottom sheet
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_filtre_date, container, false);

        //Initialisation Textviews
        final TextView tvPickerMin = v.findViewById(R.id.tvMinTime);
        final TextView tvPickerMax = v.findViewById(R.id.tvMaxTime);

        //Initialisation DatePicker
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        //Initialisation SharedPreferences
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("test", MODE_PRIVATE).edit();
        final SharedPreferences prefs = getActivity().getSharedPreferences("test", MODE_PRIVATE);

        //Récupère la valeur du min en cache si l'user l'a déja choisi
        String timeMinFromCache = prefs.getString("timeMin", "minimum");
        tvPickerMin.setText(timeMinFromCache);

        //Récupère la valeur du max en cache si l'user l'a déja choisi
        String timeMaxFromCache = prefs.getString("timeMax", "maximum");
        tvPickerMax.setText(timeMaxFromCache);

        //DatePicker Minimum
        final android.app.TimePickerDialog timePickerDialogMin = new android.app.TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                //Stock la valeur choisie en SharedPrefs
                editor.putString("timeMin", checkDigit(hourOfDay) + "h" + checkDigit(minutes));
                editor.apply();

                //Recupere la valeur Min en SharedPrefs
                String timeMinFromCache = prefs.getString("timeMin", "minimum");

                tvPickerMin.setText(timeMinFromCache);
            }
        }, currentHour, currentMinute, true);

        //DatePicker Maximum
        final android.app.TimePickerDialog timePickerDialogMax = new android.app.TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                //Stock la valeur choisie en SharedPrefs
                editor.putString("timeMax", checkDigit(hourOfDay) + "h" + checkDigit(minutes));
                editor.apply();

                //Recupere la valeur Max en SharedPrefs
                String timeMaxFromCache = prefs.getString("timeMax", "maximum");

                tvPickerMax.setText(timeMaxFromCache);
            }
        }, currentHour, currentMinute, true);

        //Affiche les DatePicker quand on click sur les textviews min et max
        tvPickerMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogMin.show();
            }
        });
        tvPickerMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogMax.show();
            }
        });

        //Gère le click sur le bouton filtrer
        Button buttonDateFilter = v.findViewById(R.id.buttonFiltrerDate);
        buttonDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifie que l'user bien choisi les deux valeurs pour la plage horaire
                if ((tvPickerMin.getText().equals("minimum")) || (tvPickerMax.getText().equals("maximum"))) {
                    Toast.makeText(getContext(), "Choisissez une plage horaire", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Appelle la fonction du MainActivity pour filter par date en passant les deux valeurs en paramètres
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.filterByDate(getActivity(),tvPickerMin.getText().toString(), tvPickerMax.getText().toString());
                    BottomSheetDate.this.dismiss();
                }
            }
        });

        //Gère le click sur le bouton reset
        Button resetDateFilter = v.findViewById(R.id.btnResetFromDateFilter);
        resetDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.removeFilter();
                BottomSheetDate.this.dismiss();

                editor.putString("timeMin", "minimum");
                editor.apply();

                editor.putString("timeMax", "maximum");
                editor.apply();
            }
        });

        return v;
    }

    //AJOUTE LE 0 AVANT L'HEURE SI BESOIN
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
