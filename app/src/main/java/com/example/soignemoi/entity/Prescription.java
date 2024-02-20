package com.example.soignemoi.entity;

import java.util.Date;

public class Prescription {
    public String id;
    public Sejour idSejour;
    public String medicament;
    public String posologie;
    public Date dateDebut;
    public Date dateFin;
    public Medecin idMedecin;
}
