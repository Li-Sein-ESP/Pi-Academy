package ui;

import entities.Paiement;
import enums.MethodePaiement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.PaiementService;
import utils.DatabaseConfig;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaiementViewController {

    @FXML private Label dateValueLabel;
    @FXML private TableView<Paiement> paiementsTable;
    @FXML private TableColumn<Paiement, Integer> idColumn;
    @FXML private TableColumn<Paiement, Double> montantColumn;
    @FXML private TableColumn<Paiement, LocalDate> dateColumn;
    @FXML private TableColumn<Paiement, String> methodeColumn;
    @FXML private TableColumn<Paiement, Integer> idEtudiantColumn;

    @FXML private TextField montantField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> methodeComboBox;
    @FXML private TextField idEtudiantField;
    @FXML private TextField idField;
    @FXML private Button addButton;
    @FXML private Button refreshButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private PaiementService paiementService;

    // ✅ Callback to notify main view to refresh after add/update
    private Runnable onSaveCallback;

    public void setPaiementService(PaiementService service) {
        this.paiementService = service;
        loadPaiements();
    }

    // ✅ Allow setting callback from parent view
    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    public void initialize() {
        // Set the current date in the DatePicker and disable editing
        if (datePicker != null) {
            datePicker.setValue(LocalDate.now());  // Set today's date
            datePicker.setDisable(true);           // Disable DatePicker
            datePicker.setEditable(false);         // Make it non-editable
        }

        if (dateValueLabel != null) {
            dateValueLabel.setText(LocalDate.now().toString());  // Display today's date
        }

        if (paiementsTable != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            methodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMethode()));
            idEtudiantColumn.setCellValueFactory(new PropertyValueFactory<>("idEtudiant"));
        }

        if (methodeComboBox != null) {
            methodeComboBox.getItems().addAll(MethodePaiement.getAllLibelles());
        }

        if (paiementService == null) {
            try {
                paiementService = new PaiementService(DatabaseConfig.getConnection());
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de se connecter à la base de données: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onRefreshClicked() {
        if (dateValueLabel != null) {
            dateValueLabel.setText(LocalDate.now().toString());
        }
        loadPaiements();
    }

    @FXML
    private void onAddClicked() {
        if (montantField == null || datePicker == null || methodeComboBox == null || idEtudiantField == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/paiement-form.fxml"));
                Parent formView = loader.load();
                PaiementViewController formController = loader.getController();
                formController.setPaiementService(paiementService);
                formController.setOnSaveCallback(this::loadPaiements); // ✅ callback setup
                Stage stage = new Stage();
                stage.setScene(new Scene(formView));
                stage.show();
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de charger la vue du formulaire: " + e.getMessage());
            }
            return;
        }

        try {
            double montant = Double.parseDouble(montantField.getText());
            LocalDate date = datePicker.getValue();
            if (date == null) {
                date = LocalDate.now();  // If date is not selected, use today's date
            }
            String methode = methodeComboBox.getValue();
            int idEtudiant = Integer.parseInt(idEtudiantField.getText());

            boolean success;
            if (idField == null || idField.getText().isEmpty()) {
                Paiement paiement = new Paiement();
                paiement.setMontant(montant);
                paiement.setDate(date);
                paiement.setMethode(methode);
                paiement.setIdEtudiant(idEtudiant);
                success = paiementService.ajouterPaiement(paiement);
                if (success) {
                    showAlert("Succès", "Paiement ajouté avec succès!");
                }
            } else {
                Paiement paiement = new Paiement();
                paiement.setId(Integer.parseInt(idField.getText()));
                paiement.setMontant(montant);
                paiement.setDate(date);
                paiement.setMethode(methode);
                paiement.setIdEtudiant(idEtudiant);
                success = paiementService.mettreAJourPaiement(paiement);
                if (success) {
                    showAlert("Succès", "Paiement modifié avec succès!");
                }
            }

            if (success) {
                clearForm();
                Stage stage = (Stage) addButton.getScene().getWindow();
                stage.close();

                if (onSaveCallback != null) {
                    onSaveCallback.run(); // ✅ refresh after save
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour le montant et l'ID de l'étudiant.");
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de l'opération: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdateClicked() {
        Paiement selectedPaiement = paiementsTable.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null) {
            showAlert("Erreur", "Veuillez sélectionner un paiement à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/paiement-form.fxml"));
            Parent formView = loader.load();
            PaiementViewController formController = loader.getController();
            formController.setPaiementService(paiementService);
            formController.prefillForm(selectedPaiement);
            formController.setOnSaveCallback(this::loadPaiements); // ✅ callback setup
            Stage stage = new Stage();
            stage.setScene(new Scene(formView));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la vue du formulaire: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteClicked() {
        Paiement selectedPaiement = paiementsTable.getSelectionModel().getSelectedItem();
        if (selectedPaiement == null) {
            showAlert("Erreur", "Veuillez sélectionner un paiement à supprimer.");
            return;
        }

        try {
            boolean deleted = paiementService.supprimerPaiement(selectedPaiement.getId());
            if (deleted) {
                showAlert("Succès", "Paiement supprimé avec succès!");
                loadPaiements();
            } else {
                showAlert("Erreur", "Échec de la suppression du paiement.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }

    private void prefillForm(Paiement paiement) {
        if (montantField != null) montantField.setText(String.valueOf(paiement.getMontant()));
        if (datePicker != null) datePicker.setValue(paiement.getDate());
        if (methodeComboBox != null) methodeComboBox.setValue(paiement.getMethode());
        if (idEtudiantField != null) idEtudiantField.setText(String.valueOf(paiement.getIdEtudiant()));
        if (idField != null) idField.setText(String.valueOf(paiement.getId()));
    }

    private void loadPaiements() {
        if (paiementsTable != null && paiementService != null) {
            try {
                List<Paiement> paiements = paiementService.getAllPaiements();
                paiementsTable.setItems(FXCollections.observableArrayList(paiements));
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors du chargement des paiements: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        if (montantField != null) montantField.clear();
        if (datePicker != null) datePicker.setValue(LocalDate.now());  // Reset to today
        if (methodeComboBox != null) methodeComboBox.setValue(null);
        if (idEtudiantField != null) idEtudiantField.clear();
        if (idField != null) idField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
