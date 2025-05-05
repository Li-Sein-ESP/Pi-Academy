package controllers;

import entities.Paiement;
import services.PaiementService;
import utils.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

public class PaiementController {
    private final PaiementService paiementService;
    private final Connection connection;

    public PaiementController() {
        try {
            // Get database connection
            this.connection = DatabaseConfig.getConnection();
            // Initialize PaiementService with the connection
            this.paiementService = new PaiementService(connection);
        } catch (SQLException e) {
            showAlert("Erreur de connexion", "Erreur lors de la connexion à la base de données: " + e.getMessage());
            throw new RuntimeException("Impossible d'initialiser PaiementController: " + e.getMessage(), e);
        }
    }

    public List<Paiement> getAllPaiements() {
        try {
            return paiementService.getAllPaiements();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération des paiements: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Paiement getPaiementById(int id) {
        try {
            return paiementService.getPaiementById(id);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération du paiement: " + e.getMessage());
            return null;
        }
    }

    public boolean ajouterPaiement(double montant, LocalDate date, String methode, int idEtudiant) {
        try {
            if (!validatePaiementData(montant, date, methode, idEtudiant)) {
                return false;
            }

            Paiement paiement = new Paiement();
            paiement.setMontant(montant);
            paiement.setDate(date);
            paiement.setMethode(methode);
            paiement.setIdEtudiant(idEtudiant);

            return paiementService.ajouterPaiement(paiement);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'ajout du paiement: " + e.getMessage());
            return false;
        }
    }

    public boolean mettreAJourPaiement(int id, double montant, LocalDate date, String methode, int idEtudiant) {
        try {
            if (!validatePaiementData(montant, date, methode, idEtudiant)) {
                return false;
            }

            Paiement paiement = new Paiement();
            paiement.setId(id);
            paiement.setMontant(montant);
            paiement.setDate(date);
            paiement.setMethode(methode);
            paiement.setIdEtudiant(idEtudiant);

            return paiementService.mettreAJourPaiement(paiement);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la mise à jour du paiement: " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerPaiement(int id) {
        try {
            return paiementService.supprimerPaiement(id);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression du paiement: " + e.getMessage());
            return false;
        }
    }

    private boolean validatePaiementData(double montant, LocalDate date, String methode, int idEtudiant) {
        if (montant <= 0) {
            showAlert("Erreur", "Le montant doit être supérieur à zéro!");
            return false;
        }
        if (date == null) {
            showAlert("Erreur", "La date de paiement ne peut pas être vide!");
            return false;
        }
        if (methode == null || methode.trim().isEmpty()) {
            showAlert("Erreur", "La méthode de paiement ne peut pas être vide!");
            return false;
        }
        if (idEtudiant <= 0) {
            showAlert("Erreur", "L'ID de l'étudiant doit être un nombre positif!");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Close the database connection when done
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}