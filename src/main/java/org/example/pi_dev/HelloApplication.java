package org.example.pi_dev;  // Notez le changement de package

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Chemin simplifié car template.fxml est à la racine de resources
            Parent root = FXMLLoader.load(getClass().getResource("/Template/template.fxml"));

            primaryStage.setTitle("Page d'accueil");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("ERREUR: Impossible de charger template.fxml");
            System.err.println("Vérifiez que:");
            System.err.println("1. Le fichier template.fxml est bien dans src/main/resources/");
            System.err.println("2. Le dossier resources est marqué comme 'Resources Root'");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        // Debug: Vérifie où Java cherche les ressources
        System.out.println("Chemin de template.fxml: " +
                HelloApplication.class.getResource("/Template/template.fxml"));

        launch(args);
    }
}