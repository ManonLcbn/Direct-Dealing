package eu.telecomnancy;

import java.io.IOException;

import eu.telecomnancy.base.Standby;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class StbLimitView {
	
    public GridPane loadPage( Standby booking ) throws IOException {

    // Charger le fichier FXML pour la page de cr�ation/modification du compte utilisateur
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Utils.SRC_URL + "/standby_limit_form.fxml"));
    GridPane root = loader.load();

    // Récupérer le contrôleur
    StbLimitController controller = loader.getController();

    // Initialiser la page 
    controller.initPage(booking);

    return root;

    }

}