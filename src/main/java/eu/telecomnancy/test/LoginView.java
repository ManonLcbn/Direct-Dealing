package eu.telecomnancy.test;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class LoginView {

    public GridPane loadPage() throws IOException {

    // Charger le fichier FXML pour la page de création/modification du compte utilisateur
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_form.fxml"));
    GridPane root = loader.load();

    // Récupérer le contrôleur
    LoginController controller = loader.getController();

    // Initialiser la page 
    controller.initPage();

    return root;

    }

}
