package org.example.pi_dev.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {
    @FXML
    private Button gestionCoursButton;

    @FXML
    private void handleGestionCours() {
        try {
            // Chemin absolu depuis le classpath
            String fxmlPath = "/Template/Cours/gestionCoursAccueil.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);

            if (fxmlUrl == null) {
                throw new IOException("Fichier FXML introuvable. Vérifiez que le fichier existe à: "
                        + "src/main/resources/Template/Cours/gestionCoursAccueil.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // Vérification que la scène existe
            if (gestionCoursButton.getScene() == null) {
                throw new IllegalStateException("La scène actuelle est null");
            }

            Stage stage = (Stage) gestionCoursButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Cours");

        } catch (IOException e) {
            showErrorAlert("Erreur de chargement",
                    "Impossible de charger l'interface des cours",
                    e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showErrorAlert("Erreur inattendue",
                    "Une erreur est survenue",
                    e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private StackPane tableContainer;

    @FXML
    private void showCoursTable() {
        setTableViewVisibility(true);
    }

    @FXML
    private void showSessionsTable() {
        setTableViewVisibility(false);
    }

    private void setTableViewVisibility(boolean showCours) {
        for (Node node : tableContainer.getChildren()) {
            if (node instanceof TableView) {
                node.setVisible(showCours == (node.getId().equals("coursTableView")));
            }
        }  }
}