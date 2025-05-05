package org.example.pi_dev;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import services.PaiementService;
import ui.PaiementViewController;
import utils.DatabaseConfig;

import java.io.IOException;
import java.sql.SQLException;

public class TemplateController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button gestionPaiementsButton;

    @FXML
    private void handleGestionPaiements() {
        try {
            // Load the payment list view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/paiement-list.fxml"));
            Parent paymentListView = loader.load();

            // Get the controller for the payment list view
            PaiementViewController paiementViewController = loader.getController();

            // Initialize PaiementService and set it in PaiementViewController
            PaiementService paiementService = new PaiementService(DatabaseConfig.getConnection());
            paiementViewController.setPaiementService(paiementService);

            // Set the payment list view in the center of the BorderPane
            mainPane.setCenter(paymentListView);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la vue des paiements: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}