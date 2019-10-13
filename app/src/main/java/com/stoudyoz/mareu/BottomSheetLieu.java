package com.stoudyoz.mareu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetLieu extends BottomSheetDialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add rounded corners to the bottom sheet
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottomsheet_filtre_lieu, container, false);

        //Filtre par salle Peach
        Button buttonPeach = v.findViewById(R.id.buttonSallePeach);
        buttonPeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.filterByLieu("Peach");
                BottomSheetLieu.this.dismiss();
            }
        });

        //Filtre par salle Mario
        Button buttonMario = v.findViewById(R.id.buttonSalleMario);
        buttonMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.filterByLieu("Mario");
                BottomSheetLieu.this.dismiss();
            }
        });

        //Filtre par salle Luigi
        Button buttonLuigi = v.findViewById(R.id.buttonSalleLuigi);
        buttonLuigi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.filterByLieu("Luigi");
                BottomSheetLieu.this.dismiss();
            }
        });

        //Reset le filtre par salle
        Button buttonReset = v.findViewById(R.id.buttonRemoveFilter);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.removeFilter();
                BottomSheetLieu.this.dismiss();
            }
        });
        
        return v;
    }
}
