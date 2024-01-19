package eu.telecomnancy.test;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class FeedbackView {
	
    public GridPane loadPage( int userId, int adId, AppController appCtrl ) throws IOException {

    // Charger le fichier FXML pour la page de cr�ation/modification du compte utilisateur
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/feedback_form.fxml"));
    GridPane root = loader.load();

    // Récupérer le contrôleur
    FeedbackController controller = loader.getController();

    // Initialiser la page 
    controller.initPage( userId, adId, appCtrl );

    return root;

    }

}
