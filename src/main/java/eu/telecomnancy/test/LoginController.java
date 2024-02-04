package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;

import eu.telecomnancy.test.DAO.JdbcUser;
import eu.telecomnancy.test.base.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class LoginController {

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;
    
    @FXML
    private Hyperlink newProfileLink;

    @FXML
    public void login(ActionEvent event) throws SQLException {
    	
        Window owner = submitButton.getScene().getWindow();

        System.out.println(emailIdField.getText());
        System.out.println(passwordField.getText());

        if (emailIdField.getText().isEmpty()) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                "Please enter a password");
            return;
        }

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        JdbcUser jdbcUser = new JdbcUser();
        User user = jdbcUser.validate(emailId, password);

        if ( user.getId() == 0 ) {
        	Utils.infBox("Entrez un email et un mot de passe valide", "Connexion");
        } else {
            // Ouvre la page principale
            AppView page = new AppView();
            try {
            	submitButton.getScene().setRoot(page.loadPage( user ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void newAccount(ActionEvent event) {
        ProfileView page = new ProfileView(0);
        try {
        	newProfileLink.getScene().setRoot(page.loadPage());
        } catch (IOException e) {
            e.printStackTrace();
        }
         event.consume();
    }
   
    public void initPage() {
    	// DEBUG & TEST
    	emailIdField.setText("super.user@red.fr");
    	passwordField.setText("1234");
    }

}