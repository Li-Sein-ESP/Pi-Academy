package entities;

import java.time.LocalDate;

public class Paiement {
    private int id;
    private double montant;
    private LocalDate date;
    private String methode;
    private int idEtudiant;

    // Constructors
    public Paiement() {
    }

    public Paiement(int id, double montant, LocalDate date, String methode, int idEtudiant) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.methode = methode;
        this.idEtudiant = idEtudiant;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "id=" + id +
                ", montant=" + montant +
                ", date=" + date +
                ", methode='" + methode + '\'' +
                ", idEtudiant=" + idEtudiant +
                '}';
    }
}
