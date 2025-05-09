package org.example.pi_dev.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pi_dev.Entities.Cours;
import org.example.pi_dev.Entities.SessionCours;
import org.example.pi_dev.service.ServiceCours;
import org.example.pi_dev.service.ServiceSessionCours;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AjouterSessionCoursController {

     @FXML private Button modifierSessionBtn;
     @FXML private Button supprimerSessionsBtn;
    // Éléments du formulaire
    @FXML private TextField nomSessionField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<String> coursComboBox; // Changé pour String si vous voulez juste afficher les noms
    @FXML private TextArea descriptionCoursArea;

    // Barre de navigation
    @FXML private Button homeButton;
    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;

    // Services
    private final ServiceSessionCours sessionService = new ServiceSessionCours();
    private final ServiceCours coursService = new ServiceCours();

    @FXML
    private void initialize() {
        // Charger les cours dans la ComboBox
        List<Cours> coursList = coursService.afficher();
        for (Cours cours : coursList) {
            coursComboBox.getItems().add(cours.getNom());
        }

        // Écouteur pour afficher la description
        coursComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Cours selected = coursList.stream()
                        .filter(c -> c.getNom().equals(newVal))
                        .findFirst()
                        .orElse(null);
                if (selected != null) {
                    descriptionCoursArea.setText(selected.getDescription());
                }
            }
        });
    }

    @FXML
    private void handleAjouterSession() {
        try {
            // Validation
            String nomSession = nomSessionField.getText();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String nomCours = coursComboBox.getValue();

            if (nomSession.isEmpty() || dateDebut == null || dateFin == null || nomCours == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur", "La date de fin doit être après la date de début");
                return;
            }

            // Trouver l'ID du cours
            Cours cours = coursService.afficher().stream()
                    .filter(c -> c.getNom().equals(nomCours))
                    .findFirst()
                    .orElse(null);

            if (cours != null) {
                SessionCours session = new SessionCours(0, nomSession, dateDebut, dateFin, cours);
                sessionService.ajouter(session, cours.getIdCours());
                showAlert("Succès", "Session ajoutée avec succès");
                clearForm();
            } else {
                showAlert("Erreur", "Cours introuvable");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private void clearForm() {
        nomSessionField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        coursComboBox.setValue(null);
        descriptionCoursArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes de navigation (conservées inchangées)
    @FXML
    private void afficherTableCours() {
        naviguerVers("/Template/Cours/gestionCoursAccueil.fxml", afficherCoursBtn);
    }

    @FXML
    private void afficherTableSessions() {naviguerVers("/Template/Cours/gestionCoursAccueil.fxml", afficherSessionsBtn);}

    @FXML
    private void retourHome() {
        naviguerVers("/Template/template.fxml", homeButton);
    }

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur de navigation: " + e.getMessage());
        }
    }

    public void modifierSession() {
        naviguerVers("/Template/Cours/modifierSession.fxml", modifierSessionBtn);}

    public void supprimerSessions() {
        naviguerVers("/Template/Cours/supprimerSession.fxml", supprimerSessionsBtn);}
}