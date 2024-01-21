package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class ProfileView {
	
		private int userID;
		
		ProfileView( int id ) {
			this.userID = id;;
		}

        public GridPane loadPage() throws IOException {
 
        // Charger le fichier FXML pour la page de création/modification du compte utilisateur
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile_form.fxml"));
        GridPane root = loader.load();

        // Récupérer le contrôleur
        ProfileController controller = loader.getController();

        // Initialiser la page 
        try {
			controller.initPage( userID );
		} catch (SQLException e) {
			e.printStackTrace();
		}

        return root;

        }

}
