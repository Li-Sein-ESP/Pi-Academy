package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main-view.fxml"));
        primaryStage.setTitle("Système de Gestion des Paiements");
        primaryStage.setScene(new Scene(root, 1400, 900));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}