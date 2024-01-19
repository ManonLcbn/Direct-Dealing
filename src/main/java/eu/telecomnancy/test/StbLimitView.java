package eu.telecomnancy.test;

import java.io.IOException;

import eu.telecomnancy.test.base.Standby;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class StbLimitView {
	
    public GridPane loadPage( Standby booking ) throws IOException {

    // Charger le fichier FXML pour la page de cr�ation/modification du compte utilisateur
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/standby_limit_form.fxml"));
    GridPane root = loader.load();

    // Récupérer le contrôleur
    StbLimitController controller = loader.getController();

    // Initialiser la page 
    controller.initPage(booking);

    return root;

    }

}
