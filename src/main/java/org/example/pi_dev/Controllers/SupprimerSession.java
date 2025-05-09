package org.example.pi_dev.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.pi_dev.Entities.SessionCours;
import org.example.pi_dev.service.ServiceSessionCours;

import java.io.IOException;

public class SupprimerSession {

    @FXML
    private Button homeButton;
    @FXML private Button afficherCoursBtn ;
    @FXML private Button afficherSessionsBtn ;
    @FXML private Button modifierSessionBtn ;
    @FXML private Button AjouterSessionBtn ;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private Button btnRechercher;
    @FXML private TableView<SessionCours> sessionTable;
    @FXML private TableColumn<SessionCours, Integer> idCol;
    @FXML private TableColumn<SessionCours, String> nomCol;
    @FXML private TableColumn<SessionCours, String> dateDebutCol;
    @FXML private TableColumn<SessionCours, String> dateFinCol;
    @FXML private TableColumn<SessionCours, String> coursCol;

    private final ServiceSessionCours serviceSession = new ServiceSessionCours();
    private final ObservableList<SessionCours> sessionList = FXCollections.observableArrayList();

    private void naviguerVers(String fxmlPath, Button bouton) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }
    @FXML private void retourHome() {naviguerVers("/Template/template.fxml", homeButton);}
    @FXML private void afficherTableSessions() {naviguerVers("/Template/Cours/gestionCoursAccueil.fxml" , afficherSessionsBtn);}
    @FXML private void afficherTableCours() {naviguerVers("/Template/Cours/gestionCoursAccueil.fxml", afficherCoursBtn);}
    @FXML public void handleModifierSession(){naviguerVers("/Template/Cours/modifierSession.fxml", modifierSessionBtn);}
    @FXML public void handleAjouterSession() {naviguerVers("/Template/Cours/ajouterSession.fxml", AjouterSessionBtn);}

    @FXML private void initialize() {
        critereComboBox.getItems().setAll("ID", "Nom");

        critereComboBox.setOnAction(event -> {
            valeurRechercheField.setVisible(true);
            btnRechercher.setVisible(true);
        });

        // Initialisation des colonnes avec SimpleXxxProperty
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom_session()));
        dateDebutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateDebut().toString()));
        dateFinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateFin().toString()));
        coursCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCours().getNom()));

        sessionTable.setItems(sessionList);
    }
    @FXML private void rechercherSession() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        SessionCours session = serviceSession.rechercherSession(critere, valeur);

        if (session != null) {
            sessionList.clear();
            sessionList.add(session);
        } else {
            showAlert("Erreur", "Session non trouvée");
        }
    }
    @FXML private void supprimerSession() {
        SessionCours selectionne = sessionTable.getSelectionModel().getSelectedItem();

        if (selectionne == null) {
            showAlert("Avertissement", "Veuillez sélectionner une session à supprimer");
            return;
        }

        serviceSession.supprimer("ID", String.valueOf(selectionne.getId()));
        sessionList.remove(selectionne);
        showAlert("Succès", "Session supprimée avec succès");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
