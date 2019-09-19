package com.stoudyoz.mareu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyEvenementAdapter extends RecyclerView.Adapter<MyEvenementAdapter.ViewHolder> {

    private Activity context;

    MyEvenementAdapter(List<Evenement> evenements, Activity context) {
        this.evenements = evenements;
        this.context = context;
    }

    private List<Evenement> evenements;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evenement_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Evenement evenement = evenements.get(position);

        holder.tvGras.setText("Réunion " + evenement.getLieu() + " - " + evenement.getTime() + " - " + evenement.getSujet());
        holder.tvParticipant.setText(evenement.getParticipants());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evenements.remove(evenement);
                MainActivity.recyclerView.setAdapter(new MyEvenementAdapter((MainActivity.evenements), context));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Participants");

                String TextParticipants = evenements.get(position).getParticipants();
                String textRemplace = TextParticipants.replace(",", "\n");
                String textRemplace2 = textRemplace.replace(" ", "");

                alertDialog.setMessage(textRemplace2);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return evenements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGras;
        TextView tvParticipant;
        ImageView ivDelete, ivCercle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvGras = itemView.findViewById(R.id.tvGras);
            tvParticipant = itemView.findViewById(R.id.tvParticipants);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivCercle = itemView.findViewById(R.id.ivCercle);

        }
    }

}
