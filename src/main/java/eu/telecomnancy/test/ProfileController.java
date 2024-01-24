package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import eu.telecomnancy.test.DAO.JdbcUser;
import eu.telecomnancy.test.base.User;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

public class ProfileController {

	private User user = new User();

    @FXML
    private Label titleLabel;
    @FXML
    private TextField nameIdField;
    @FXML
    private TextField emailIdField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label famountField;
    @FXML
    private CheckBox isDisableCheckbox;
    @FXML
    private VBox monthCheckboxes;
    @FXML
    private CheckBox cbMonth1;
    @FXML
    private CheckBox cbMonth2;
    @FXML
    private CheckBox cbMonth3;
    @FXML
    private CheckBox cbMonth4;
    @FXML
    private CheckBox cbMonth5;
    @FXML
    private CheckBox cbMonth6;
    @FXML
    private CheckBox cbMonth7;



    @FXML
    private Button registerButton;
    
    private boolean newProfile;
        
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
        	    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    @FXML
    public void register(ActionEvent event) throws SQLException {

        Window owner = registerButton.getScene().getWindow();

        if (nameIdField.getText().isEmpty()) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Erreur de données",
                "Un nom d'utilisateur est requis");
            return;
        }

        if ( !validate(emailIdField.getText()) ) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Erreur de données",
                "Un email valide est requis");
            return;
        }
        
        if (passwordField.getText().isEmpty()) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Erreur de données",
                "Un mot de passe est requis");
            return;
        }

        JdbcUser jdbcUser = new JdbcUser();
        if( newProfile && jdbcUser.isExists(emailIdField.getText())) {
        	Utils.showAlert(Alert.AlertType.ERROR, owner, "Erreur de données",
                    "Cette adresse email est déjà enregistrée");
                return;
        }
        
        // Mois disponibles
        String availability = user.getAvailability();
        char availabilityArray[] = availability.toCharArray();
        for( int i = 1; i <= 7; i++ ) {
        	String controlName = "#cbMonth" + i;
        	CheckBox cb = (CheckBox) monthCheckboxes.lookup(controlName);
        	availabilityArray[i-1] = ( cb.isSelected() ? 'x' : '.');
        }
        user.setAvailability(availabilityArray);

        if( newProfile ) { // Ajout nouvel utilisateur
            int userId = jdbcUser.insert(user);
            if( userId > 0 ) {
            	Utils.infBox("Profil enregistré", "Enregistrement");
            	AppView page = new AppView();
                try {
                	registerButton.getScene().setRoot(page.loadPage(user));
                } catch (IOException e) {
                    e.printStackTrace();
                }           	
            }
            else
            	Utils.infBox("Erreur lors de l'enregistrement", "Enregistrement");
        }
        else {  // Modifie les données d'un utilisateur
        	int ret = Utils.confirmBox("Les données ont été modifiées. Voulez-vous les enregistrer?", "Enregistrement" );
        	if( ret == JOptionPane.YES_OPTION ) {
        		jdbcUser.update(user);
        	}
        	owner.hide();
        }
    }

    public void initPage( int id ) throws SQLException {
    	this.newProfile = (id <= 0);
        
    	
    	if( newProfile ) {
    		titleLabel.setText("Nouvel utilisateur");
            // Cocher toutes les cases
            for( int i = 1; i <= 7; i++ ) {
            	String controlName = "#cbMonth" + i;
            	CheckBox cb = (CheckBox) monthCheckboxes.lookup(controlName);
            	cb.setSelected( true );
            }
    	}
    	else {
    		titleLabel.setText("Mon compte utilisateur");
            JdbcUser jdbcUser = new JdbcUser();
            user = jdbcUser.selectByID( id );
            
            // Mettre � jour les cases a cocher
            char availabilityArray[] = user.getAvailability().toCharArray();
            for( int i = 1; i <= 7; i++ ) {
            	String controlName = "#cbMonth" + i;
            	CheckBox cb = (CheckBox) monthCheckboxes.lookup(controlName);
            	cb.setSelected( availabilityArray[i-1] == 'x' );
            }
    	}
    	// Permet une mise a jour auto des variables
    	Bindings.bindBidirectional(nameIdField.textProperty(), user.nameProperty());
    	Bindings.bindBidirectional(emailIdField.textProperty(), user.emailProperty());
    	Bindings.bindBidirectional(passwordField.textProperty(), user.passwordProperty());
    	Bindings.bindBidirectional(isDisableCheckbox.selectedProperty(), user.isDisableProperty());
    	Bindings.bindBidirectional(famountField.textProperty(), user.famountProperty(), new NumberStringConverter());

    }
    
	public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
	}
}