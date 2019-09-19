package com.stoudyoz.mareu;

public class Evenement {

    private String time;
    private String lieu;
    private String sujet;
    private String participants;

    public Evenement(String time, String lieu, String sujet, String participants) {
        this.time = time;
        this.lieu = lieu;
        this.sujet = sujet;
        this.participants = participants;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }
}
