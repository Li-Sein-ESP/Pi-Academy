package org.example.pi_dev.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.pi_dev.Entities.Cours;
import org.example.pi_dev.Entities.SessionCours;
import org.example.pi_dev.service.ServiceCours;
import org.example.pi_dev.service.ServiceSessionCours;

import java.io.IOException;

public class ModifierSession {

    @FXML private Button afficherSessionsBtn;
    @FXML private Button homeButton;
    @FXML private Button afficherCoursBtn ;
    @FXML private Button ajouterSessionBtn ;
    @FXML private Button supprimerSessionBtn ;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private TextField nomField;
    @FXML private DatePicker dateDebutPicker;
    @FXML private DatePicker dateFinPicker;
    @FXML private ComboBox<Cours> coursComboBox;
    @FXML private TextArea descriptionCoursArea;

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
    @FXML public void handleAjouterSession() {naviguerVers("/Template/Cours/ajouterSession.fxml", ajouterSessionBtn);}
    @FXML public void handleSupprimerSession() {naviguerVers("/Template/Cours/supprimerSession.fxml", supprimerSessionBtn);}

    private final ServiceSessionCours serviceSession = new ServiceSessionCours();
    private final ServiceCours serviceCours = new ServiceCours();
    private Stage stage;
    private SessionCours sessionToModify;

    @FXML
    public void initialize() {
        // Configuration des ComboBox
        configureCoursComboBox();
        configureRechercheComboBox();
        // Chargement des cours disponibles
        loadCours();
        // Désactiver les champs initialement
        setFieldsEditable(false);
    }

    private void configureCoursComboBox() {
        // Configurer l'affichage des noms de cours seulement
        coursComboBox.setConverter(new StringConverter<Cours>() {
            @Override
            public String toString(Cours cours) {
                return cours == null ? "" : cours.getNom();
            }

            @Override
            public Cours fromString(String string) {
                return null;
            }
        });

        // Mise à jour automatique de la description quand le cours change
        coursComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                descriptionCoursArea.setText(newVal.getDescription());
            } else {
                descriptionCoursArea.clear();
            }
        });
    }

    private void configureRechercheComboBox() {
        critereComboBox.getItems().setAll("ID", "Nom");
        critereComboBox.getSelectionModel().selectFirst();
    }

    private void loadCours() {
        coursComboBox.getItems().setAll(serviceCours.afficher());
    }

    @FXML
    private void handleRechercher() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        if (valeur.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une valeur de recherche");
            return;
        }

        SessionCours session = serviceSession.rechercherSession(critere, valeur);

        if (session != null) {
            sessionToModify = session;
            populateFields(session);
            setFieldsEditable(true);
        } else {
            showAlert("Information", "Aucune session trouvée");
            setFieldsEditable(false);
        }
    }

    private void populateFields(SessionCours session) {
        nomField.setText(session.getNom_session());
        dateDebutPicker.setValue(session.getDateDebut());
        dateFinPicker.setValue(session.getDateFin());
        coursComboBox.setValue(session.getCours());
        // La description sera mise à jour automatiquement via le listener
    }

    private void setFieldsEditable(boolean editable) {
        nomField.setDisable(!editable);
        dateDebutPicker.setDisable(!editable);
        dateFinPicker.setDisable(!editable);
        coursComboBox.setDisable(!editable);
    }

    @FXML
    private void handleModifier() {
        try {
            // Validation des champs
            if (!validateFields()) return;

            // Mise à jour de la session
            updateSessionFromFields();

            serviceSession.modifier(sessionToModify);

            showAlert("Succès", "Session modifiée avec succès");
            stage.close();
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (nomField.getText().isEmpty() ||
                dateDebutPicker.getValue() == null ||
                dateFinPicker.getValue() == null ||
                coursComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs");
            return false;
        }

        if (dateFinPicker.getValue().isBefore(dateDebutPicker.getValue())) {
            showAlert("Erreur", "La date de fin doit être après la date de début");
            return false;
        }

        return true;
    }

    private void updateSessionFromFields() {
        sessionToModify.setNom_session(nomField.getText());
        sessionToModify.setDateDebut(dateDebutPicker.getValue());
        sessionToModify.setDateFin(dateFinPicker.getValue());
        sessionToModify.setCours(coursComboBox.getValue());
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
