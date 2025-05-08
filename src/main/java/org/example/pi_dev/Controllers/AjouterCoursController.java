package org.example.pi_dev.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.pi_dev.Entities.CategoriesCours;
import org.example.pi_dev.Entities.Cours;
import org.example.pi_dev.service.ServiceCours;

import java.io.IOException;
import java.util.List;

public class AjouterCoursController {

    @FXML
    private Spinner<Integer> dureeSpinner;
    @FXML private Button homeButton;
    @FXML private Button afficherCoursBtn;
    @FXML private Button afficherSessionsBtn;
    @FXML private Button ajouterSessionBtn;
    @FXML private Button modifierSessionBtn;
    @FXML private Button supprimerSessionBtn;
    @FXML private Button ajouterCoursBtn;
    @FXML private Button modifierCoursBtn;
    @FXML private Button supprimerCoursBtn;
    @FXML private Button metierAvancee1Btn;
    @FXML private Button metierAvancee2Btn;
    @FXML private Button ajouterButton;
    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private HBox coursButtonsContainer;
    @FXML private HBox sessionsButtonsContainer;
    @FXML private HBox metierButtonsContainer;

    private ServiceCours coursService = new ServiceCours();

    @FXML
    private void initialize() {
        // Récupérer les catégories depuis la base de données
        List<CategoriesCours> categories = coursService.getAllCategories();
        for (CategoriesCours categorie : categories) {
            categorieComboBox.getItems().add(categorie.getNom());
        }

        // Initialisation du Spinner pour la durée
        dureeSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(20, 100, 1));
    }

    @FXML
    private void ajouterCours() {
        String nom = nomField.getText();
        String description = descriptionField.getText();
        String prixTexte = prixField.getText();
        String categorie = categorieComboBox.getValue();
        Integer duree = dureeSpinner.getValue();

        if (nom.isEmpty() || description.isEmpty() || prixTexte.isEmpty() || categorie == null || duree == null) {
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }

        try {
            double prix = Double.parseDouble(prixTexte);

            System.out.println("Cours ajouté :");
            System.out.println("Nom: " + nom);
            System.out.println("Description: " + description);
            System.out.println("Prix: " + prix);
            System.out.println("Durée: " + duree + " heures");
            System.out.println("Catégorie: " + categorie);

            // Trouver l'id de la catégorie
            List<CategoriesCours> categories = coursService.getAllCategories();
            int idCategorie = -1;
            for (CategoriesCours cat : categories) {
                if (cat.getNom().equals(categorie)) {
                    idCategorie = cat.getId_categories();
                    break;
                }
            }

            if (idCategorie != -1) {
                // Créer l'objet Cours
                Cours cours = new Cours();
                cours.setNom(nom);
                cours.setDescription(description);
                cours.setDuree(duree);
                cours.setPrix((float) prix);

                // Ajouter dans la base
                coursService.ajouter(cours, idCategorie);

                System.out.println("Cours ajouté en base de données.");
            } else {
                System.out.println("Catégorie introuvable !");
            }

        } catch (NumberFormatException e) {
            System.out.println("Le prix doit être un nombre valide.");
        }
    }

    // Méthodes de navigation existantes
    @FXML
    private void afficherTableCours() {
        naviguerVers("/Template/Cours/gestionCoursAccueil.fxml", afficherCoursBtn);
    }

    @FXML
    private void afficherTableSessions() {
        naviguerVers("/Template/Cours/gestionCoursAccueil.fxml", afficherSessionsBtn);
    }

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
            System.out.println("Erreur Erreur de navigation: " + e.getMessage());
        }
    }

    // Méthodes non implémentées (conservées pour compatibilité)
    @FXML private void handleAjouterSession() {}
    @FXML private void handleModifierSession() {naviguerVers("/Template/Cours/modifierSession.fxml", modifierSessionBtn);}
    @FXML private void handleSupprimerSession() {naviguerVers("/Template/Cours/supprimerSession.fxml", supprimerSessionBtn);}
    @FXML private void handleAjouterCours() {}
    @FXML private void handleModifierCours() {naviguerVers("/Template/Cours/modifierCours.fxml", modifierCoursBtn);}
    @FXML private void handleSupprimerCours() {naviguerVers("/Template/Cours/supprimerCours.fxml", supprimerCoursBtn);}
    @FXML private void handleMetierAvancee1() {}
    @FXML private void handleMetierAvancee2() {}
}