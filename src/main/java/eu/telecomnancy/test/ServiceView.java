package eu.telecomnancy.test;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class ServiceView implements GenericView {

	public GridPane loadPage(int userId, int adId, AppController appController ) throws IOException {
    
    // Charger le fichier FXML pour la page de principale de l'application
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/service_form.fxml"));
    GridPane root = loader.load();


    // Récupérer le contrôleur
    ServiceController controller = loader.getController();

    // Initialiser la page 
    controller.initPage();

    return root;
    }
}
