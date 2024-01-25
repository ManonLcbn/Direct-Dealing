package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class MessageView implements GenericView {

    public GridPane loadPage( int userId, int adId, AppController appCtrl ) throws IOException {

        // Charger le fichier FXML pour la page de principale de l'application
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Utils.SRC_URL + "/message_form.fxml"));
        GridPane root = loader.load();


        // Récupérer le contrôleur
        MessageController controller = loader.getController();

        // Initialiser la page
        controller.initPage(userId, adId);

        return root;
    }
}