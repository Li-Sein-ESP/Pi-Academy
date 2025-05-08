package org.example.pi_dev.Controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.pi_dev.Entities.Cours;
import org.example.pi_dev.Entities.SessionCours;
import org.example.pi_dev.service.ServiceCours;
import org.example.pi_dev.service.ServiceSessionCours;

import java.io.IOException;
import java.util.List;


public class GestionCoursController {

    // Références FXML
    @FXML private Button homeButton;
    @FXML private StackPane tableContainer;

    // Boutons d'affichage
    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;
    @FXML private Button refreshBtn;

    // Containers de boutons
    @FXML private HBox coursButtonsContainer;
    @FXML private HBox sessionsButtonsContainer;
    @FXML private HBox metierButtonsContainer;

    @FXML private Button ajouterCoursBtn;
    @FXML private Button modifierCoursBtn;
    @FXML private Button supprimerCoursBtn;
    @FXML private Button ajouterSessionBtn;
    @FXML private Button modifierSessionBtn;
    @FXML private Button supprimerSessionBtn;
    @FXML private Button metierAvancee1Btn;
    @FXML private Button metierAvancee2Btn;



    // Tables
    @FXML private TableView<Cours> tableCours;
    @FXML private TableView<SessionCours> tableSessionCours;

    // Colonnes Cours
    @FXML private TableColumn<Cours, Integer> idCoursCol;
    @FXML private TableColumn<Cours, String> nomCoursCol, descriptionCol, categorieCol;
    @FXML private TableColumn<Cours, Integer> dureeCol;
    @FXML private TableColumn<Cours, Float> prixCol;

    // Colonnes Session
    @FXML private TableColumn<SessionCours, Integer> idSessionCol;
    @FXML private TableColumn<SessionCours, String> nomSessionCol, coursSessionCol, descriptionCoursCol;
    @FXML private TableColumn<SessionCours, String> dateDebutCol, dateFinCol;

    // Services
    private final ServiceCours serviceCours = new ServiceCours();
    private final ServiceSessionCours serviceSession = new ServiceSessionCours();


    private void showCoursView() {
        coursButtonsContainer.setVisible(true);
        sessionsButtonsContainer.setVisible(false);
        metierButtonsContainer.setVisible(true);
    }

   /* private void showSessionsView() {
        coursButtonsContainer.setVisible(false);
        sessionsButtonsContainer.setVisible(true);
        metierButtonsContainer.setVisible(true);
    }*/

    @FXML
    private void afficherTableCours() {
        coursButtonsContainer.setVisible(true);
        coursButtonsContainer.setManaged(true); // Important pour qu’il occupe l’espace

        sessionsButtonsContainer.setVisible(false);
        sessionsButtonsContainer.setManaged(false);
        tableCours.setVisible(true);
        tableCours.setManaged(true);
        tableSessionCours.setVisible(false);
        tableSessionCours.setManaged(false);

        loadCours();
    }

    @FXML
    private void afficherTableSessions() {
        coursButtonsContainer.setVisible(false);
        coursButtonsContainer.setManaged(false);

        sessionsButtonsContainer.setVisible(true);
        sessionsButtonsContainer.setManaged(true);
        tableSessionCours.setVisible(true);
        tableSessionCours.setManaged(true);
        tableCours.setVisible(false);
        tableCours.setManaged(false);

        loadSessions();}

    public void initialize() {
        configureCoursColumns();
        configureSessionColumns();
        showCoursView();
    }

    private void configureCoursColumns() {
        idCoursCol.setCellValueFactory(new PropertyValueFactory<>("idCours"));
        nomCoursCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        categorieCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoriesCours() != null ?
                        cellData.getValue().getCategoriesCours().getNom() : "N/A"));
    }

    private void configureSessionColumns() {
        // Correspondance exacte avec vos fx:id
        idSessionCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        nomSessionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNom_session()));

        dateDebutCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateDebut().toString()));

        dateFinCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateFin().toString()));

        coursSessionCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCours().getNom()));

        descriptionCoursCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCours().getDescription()));
    }

    // Chargement des données
    private void loadCours() {
        List<Cours> coursList = serviceCours.afficher();
        System.out.println("Nombre de cours chargés : " + coursList.size()); // Debug
        tableCours.getItems().setAll(coursList);
        for (Cours c : coursList) {
            System.out.println("Cours: " + c.getIdCours() + ", " + c.getNom());
        }

    }

    private void loadSessions() {
        List<SessionCours> sessionsList = serviceSession.afficher();
        tableSessionCours.getItems().setAll(sessionsList);
    }

    @FXML
    private void handleRefresh() {
        if (tableCours.isVisible()) {
            loadCours();
        } else {
            loadSessions();
        }
    }

    // Navigation
    @FXML
    private void retourHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Template/template.fxml"));
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible de charger l'interface d'accueil", e);
        }
    }

    // Méthodes CRUD (à compléter)

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterCours() {
        try {
            // Charge avec le chemin ABSOLU depuis la racine des ressources
            Parent root = FXMLLoader.load(getClass().getResource("/Template/Cours/ajouterCours.fxml"));

            // Debug: affiche le chemin réel utilisé
            System.out.println("Chemin réel: " + getClass().getResource("/Template/Cours/ajouterCours.fxml"));

            Stage stage = (Stage) ajouterCoursBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showError("Erreur de navigation", "Impossible de charger la page d'ajout de cours", e);
            e.printStackTrace();
        }
    }
    @FXML
    private void handleModifierCours() { naviguerVers("/Template/Cours/modifierCours.fxml", modifierCoursBtn); }
    @FXML
    private void handleSupprimerCours() {naviguerVers("/Template/Cours/supprimerCours.fxml", supprimerCoursBtn); }
    @FXML
    private void handleAjouterSession() {
        try {
            // Charge la même page gestionCoursAccueil.fxml (ou une autre si nécessaire)
            Parent root = FXMLLoader.load(getClass().getResource("/Template/Cours/ajouterSession.fxml"));
            Stage stage = (Stage) ajouterSessionBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur de navigation, Impossible de retourner à l'accueil"+ e);
        }
    }
    @FXML
    private void handleModifierSession() {naviguerVers("/Template/Cours/modifierSession.fxml", modifierSessionBtn); }
    @FXML
    private void handleSupprimerSession() { naviguerVers("/Template/Cours/supprimerSession.fxml", supprimerSessionBtn); }
    @FXML
    private void handleMetierAvancee1() { /* ... */ }
    @FXML
    private void handleMetierAvancee2() { /* ... */ }


    // Utilitaires
    private void showError(String title, String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message + "\nErreur: " + e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
}