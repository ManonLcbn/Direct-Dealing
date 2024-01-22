package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class ServiceRecurrentView implements GenericView{
    public GridPane loadPage(int userId, int adId, AppController appController ) throws IOException {
    
    // Charger le fichier FXML pour la page de principale de l'application
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicerecurrent_form.fxml"));
    GridPane root = loader.load();


    // Récupérer le contrôleur
    ServiceRecurrentController controller = loader.getController();

    // Initialiser la page 
    try {
		controller.initPage( userId, adId, appController );
	} catch (SQLException e) {
		e.printStackTrace();
	}

    return root;
    }
}
