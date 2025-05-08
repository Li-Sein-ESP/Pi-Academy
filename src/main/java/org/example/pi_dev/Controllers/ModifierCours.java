package org.example.pi_dev.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.pi_dev.Entities.CategoriesCours;
import org.example.pi_dev.Entities.Cours;
import org.example.pi_dev.service.ServiceCours;

import java.io.IOException;
import java.util.List;

public class ModifierCours {

    @FXML private Button modifierButton;
    @FXML private VBox ap;
    @FXML private Button homeButton;
    @FXML private Button afficherSessionsBtn;
    @FXML private Button afficherCoursBtn;
    @FXML private Button supprimerCoursBtn;
    @FXML private Button ajouterCoursBtn;

    @FXML private ComboBox<String> critereComboBox;
    @FXML private TextField valeurRechercheField;
    @FXML private Button btnRechercher;
    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private Spinner<Integer> dureeSpinner;
    @FXML private ComboBox<String> categorieComboBox;

    private final ServiceCours serviceCours = new ServiceCours();
    private Cours coursActuel;

    @FXML private void initialize() {
        critereComboBox.getItems().addAll("ID", "Nom");
        critereComboBox.setOnAction(event -> {
            valeurRechercheField.setVisible(true);
            btnRechercher.setVisible(true);
        });

        dureeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1));

        // Remplir le ComboBox des catégories
        List<CategoriesCours> categories = serviceCours.getAllCategories();
        for (CategoriesCours cat : categories) {
            categorieComboBox.getItems().add(cat.getNom());
        }
    }
    @FXML private void rechercherCours() {
        String critere = critereComboBox.getValue();
        String valeur = valeurRechercheField.getText();

        // Appel à la méthode rechercherCours pour obtenir le cours
        coursActuel = serviceCours.rechercherCours(critere, valeur);

        if (coursActuel != null) {
            // Pré-remplir les champs avec les données du cours trouvé
            nomField.setText(coursActuel.getNom());
            descriptionField.setText(coursActuel.getDescription());
            prixField.setText(String.valueOf(coursActuel.getPrix()));
            dureeSpinner.getValueFactory().setValue(coursActuel.getDuree());
            categorieComboBox.setValue(coursActuel.getCategoriesCours().getNom());
        } else {
            // Message d'erreur si aucun cours trouvé
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cours non trouvé.");
            alert.show();
        }
    }
    @FXML private void modifierCours() {
        if (coursActuel == null) {
            new Alert(Alert.AlertType.WARNING, "Veuillez d'abord rechercher un cours.").show();
            return;
        }

        // Mettre à jour les données du cours avec les nouvelles valeurs
        coursActuel.setNom(nomField.getText());
        coursActuel.setDescription(descriptionField.getText());
        coursActuel.setPrix(Float.parseFloat(prixField.getText()));
        coursActuel.setDuree(dureeSpinner.getValue());

        // Associer l'objet CategoriesCours
        String nomCategorie = categorieComboBox.getValue();
        for (CategoriesCours cat : serviceCours.getAllCategories()) {
            if (cat.getNom().equals(nomCategorie)) {
                coursActuel.setCategoriesCours(cat);
                break;
            }
        }

        // Modifier le cours via le service
        serviceCours.modifier(coursActuel);
        new Alert(Alert.AlertType.INFORMATION, "Cours modifié avec succès.").show();
    }


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
    @FXML private void handleSupprimerCours() {naviguerVers("/Template/Cours/supprimerCours.fxml", supprimerCoursBtn);}
    @FXML public void handleAjouterCours() {naviguerVers("/Template/Cours/ajouterCours.fxml", ajouterCoursBtn);}



}
