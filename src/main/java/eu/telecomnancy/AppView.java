package eu.telecomnancy;

import java.io.IOException;
import java.sql.SQLException;

import eu.telecomnancy.base.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class AppView {

	public GridPane loadPage( User usr ) throws IOException, SQLException {
    
    // Charger le fichier FXML pour la page de principale de l'application
    FXMLLoader loader = new FXMLLoader(getClass().getResource(Utils.SRC_URL + "/app_form.fxml"));
    GridPane root = loader.load();

    // Récupérer le contrôleur
    AppController controller = loader.getController();

    // Initialiser la page 
    controller.initPage( usr );

    return root;

}


}