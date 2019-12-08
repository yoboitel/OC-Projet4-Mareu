package com.stoudyoz.mareu;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    private View v;
    private TextView tvPickerMin, tvPickerMax;
    private int currentHour, currentMinute;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add rounded corners to the bottom sheet
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.bottomsheet_filtre_date, container, false);

        //Initialisation Textviews
        tvPickerMin = v.findViewById(R.id.tvMinTime);
        tvPickerMax = v.findViewById(R.id.tvMaxTime);

        //Initialisation DatePicker
        Calendar calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);

        //Initialisation SharedPreferences
        editor = getActivity().getSharedPreferences("test", MODE_PRIVATE).edit();
        prefs = getActivity().getSharedPreferences("test", MODE_PRIVATE);

        //Récupère la valeur du min en cache si l'user l'a déja choisi
        String timeMinFromCache = prefs.getString("timeMin", "minimum");
        tvPickerMin.setText(timeMinFromCache);

        //Récupère la valeur du max en cache si l'user l'a déja choisi
        String timeMaxFromCache = prefs.getString("timeMax", "maximum");
        tvPickerMax.setText(timeMaxFromCache);

        //Gère le TimePicker pour l'heure min
        onTimePickerMinChoosed();
        //Gère le TimePicker pour l'heure max
        onTimePickerMaxChoosed();

        //Gère le click sur le bouton filtrer
        onClickButtonFiltrer();
        //Gère le click sur le bouton reset
        onClickButtonReset();

        return v;
    }

    private void onTimePickerMaxChoosed() {
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


        tvPickerMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogMax.show();
            }
        });
    }

    private void onTimePickerMinChoosed() {
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

        //Affiche les DatePicker quand on click sur les textviews min et max
        tvPickerMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialogMin.show();
            }
        });
    }

    private void onClickButtonFiltrer() {
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
                    mainActivity.filtreParDatePlusReload(getActivity(),tvPickerMin.getText().toString(), tvPickerMax.getText().toString());
                    BottomSheetDate.this.dismiss();
                }
            }
        });
    }

    private void onClickButtonReset() {
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
    }

    //AJOUTE LE 0 AVANT L'HEURE SI BESOIN
    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}
