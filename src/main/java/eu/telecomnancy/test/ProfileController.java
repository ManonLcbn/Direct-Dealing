package eu.telecomnancy.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Random;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

public class ProfileController {

	private User user = new User();

    private static final String RESOURCE_FOLDER = "images/";
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
    private ImageView photoProfile;
    @FXML
    private Button photoButton;
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
            System.out.println("User ID: " + userId);
            if( userId > 0 ) {
                System.out.println("Checkpoint 1");
                try{
                    //Utils.infBox("Profil enregistré", "Enregistrement");
                    System.out.println("Checkpoint 2");
                    AppView page = new AppView();
                    System.out.println("Checkpoint 3");
                	registerButton.getScene().setRoot(page.loadPage(user));
                    System.out.println("Checkpoint 4");
                } catch (IOException e) {
                    e.printStackTrace();
                }           	
            }
            else
            	Utils.infBox("Erreur lors de l'enregistrement", "Enregistrement");
            initPage(userId);
        }
        else {  // Modifie les données d'un utilisateur
        	//int ret = Utils.confirmBox("Les données ont été modifiées. Voulez-vous les enregistrer?", "Enregistrement" );
        	//if( ret == JOptionPane.YES_OPTION ) {
        		jdbcUser.update(user);
        	//}
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

        String path = "src/main/resources/" + RESOURCE_FOLDER + user.getPicture();
        System.out.println(path);
        Image image = new Image(new File (path).toURI().toString());
        photoProfile.setImage(image);
    }
    
	public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
	}

    @FXML
    private void showButton(MouseEvent event) {
        photoButton.setVisible(true);
    }
    @FXML
    private void hideButton(MouseEvent event) {
        photoButton.setVisible(false);
    }

    @FXML
    public void addImage(ActionEvent event) throws SQLException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Charger l'image sélectionnée dans l'ImageView
            copyImageToResources(selectedFile, RESOURCE_FOLDER, selectedFile.getName());
            Image image = new Image(selectedFile.toURI().toString());
            photoProfile.setImage(image);
        }
    }

    public void copyImageToResources(File sourceImage, String resourcesDirectory, String newImageName) {
        // Obtenez le chemin vers votre répertoire de ressources
        String resourcesPath = "src/main/resources/" + resourcesDirectory; // Chemin vers votre répertoire de ressources


        try {
            // Créez le répertoire s'il n'existe pas
            Files.createDirectories(Paths.get(resourcesPath));
            // Copiez l'image vers le répertoire de ressources
            try {
                if (user.getPicture().equals("UserPicture.jpg")) {
                    String generatedString = new Random().ints(48, 123)
                            .filter(i -> (i < 58) || (i > 64 && i < 91) || (i > 96))
                            .limit(14)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    newImageName = generatedString + ".jpg";
                    user.setPicture(newImageName);
                }
                else {
                    newImageName = user.getPicture();
                }

            } catch (NullPointerException e) {
                String generatedString = new Random().ints(48, 123)
                        .filter(i -> (i < 58) || (i > 64 && i < 91) || (i > 96))
                        .limit(14)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                newImageName = generatedString + ".jpg";
                user.setPicture(newImageName);
            }
            Path destinationPath = Paths.get(resourcesPath, newImageName);
            Files.copy(sourceImage.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}