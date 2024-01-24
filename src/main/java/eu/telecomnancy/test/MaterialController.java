package eu.telecomnancy.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.swing.JOptionPane;

import eu.telecomnancy.test.DAO.JdbcAd;
import eu.telecomnancy.test.DAO.JdbcMatCat;
import eu.telecomnancy.test.DAO.JdbcStandby;
import eu.telecomnancy.test.base.Ad;
import eu.telecomnancy.test.base.MatCat;
import eu.telecomnancy.test.base.Standby;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;


public class MaterialController {

	private static final String RESOURCE_FOLDER = "images/";
	private Ad ad = null;
	private boolean isNewAd;
	private int currentUserId;
	private List<MatCat> matCatList;
	private ObservableList<MatCat> matCatObs;
	private AppController appController;
	
	@FXML
	private Label adInfo;
	@FXML
	private RadioButton requestRadiobutton;
	@FXML
	private TextField nameIdField;
	@FXML
	private Text nameIdFieldText;
	@FXML
	private TextArea descriptionIdField;
	@FXML
	private Text descriptionText;
	@FXML
	private TextField costIdField;
	@FXML
	private Text costText;
	@FXML
	private Label florainsText;
	@FXML
	private ComboBox<MatCat> categories;
	@FXML
	private Text categoriesText;
	@FXML
	private CheckBox isAvailable;
	@FXML
	private ImageView productImage;
	@FXML
	private TextField zipcodeField;
	@FXML
	private Text codePostalText;
	@FXML
	private TextArea commentsField;
	@FXML
	private Text commentairesText;
	@FXML
	private DatePicker startdateField;
	@FXML
	private Text startDateText;
	@FXML
	private DatePicker enddateField;
	@FXML
	private Text endDateText;
	@FXML
	private TextField durationField;
	@FXML
	private Text nombreDeJoursText;
	@FXML
	private Label joursLabel;
	private String picture;
	@FXML
	private ToggleGroup type1;
	@FXML
	private GridPane gridpane;
	@FXML
	public HBox radioButtonHbox;



	@FXML
    private Button addButton, delButton, orderButton, commentButton, addImageButton;
	
    @FXML
    public void commentAd(ActionEvent event) throws IOException {
    	Stage thirdStage = new Stage();
   		FeedbackView page = new FeedbackView();
	    GridPane root = page.loadPage( currentUserId, ad.getId(), appController );
		Scene scene = new Scene(root,400,200);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		thirdStage.setTitle("Donnez votre avis");
		thirdStage.setScene(scene);
		thirdStage.show();          		
    }
    
    @FXML
    public void bookMaterial(ActionEvent event) throws SQLException {

        Window owner = addButton.getScene().getWindow();
        
        if( ad.isIsAvailable() ) {
        	// Materiel non disponible
        	int ret = JOptionPane.NO_OPTION;
        	// test si l'utilisateur a deja une reservation
            Standby book = new Standby(currentUserId,ad.getId(),LocalDate.now());
            JdbcStandby db_s = new JdbcStandby();
            boolean isExists = db_s.isExists(book);	
            if( isExists ) {
            	Utils.infBox("Vous avez déjà une réservation\nsur cette annonce", "Réservation existante");
            }
            else {
	        	JdbcStandby db = new JdbcStandby();
	        	int total = db.selectCount(ad.getId());
	            if( total > 1 ) {
		        	ret = Utils.confirmBox("Il y a déjà " + total + " demandes en attente sur cette proposition.\nSouhaitez-vous être "
		        			+ "prévu des qu'une disponibilité se présente?", "Proposition non disponible" );
	        	}
	        	else {
		        	ret = Utils.confirmBox("La proposition n'est plus disponible. Souhaitez-vous être\n"
		        			+ "prévu des qu'il sera nouveau disponible ?", "Proposition non disponible" );
	        	}
	        	if( ret == JOptionPane.YES_OPTION ) {
	            	Stage thirdStage = new Stage();
	                try {
	               		StbLimitView page = new StbLimitView();
	         	        GridPane root = page.loadPage(book);
	        			Scene scene = new Scene(root,400,200);
	        			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
	        			thirdStage.setTitle("Date limite de la demande");
	        			thirdStage.setScene(scene);
	        			thirdStage.show();          		
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	        	}
            }
        	
        }

       	owner.hide();
    }

    @FXML
    public void addMaterial(ActionEvent event) throws SQLException {

        Window owner = addButton.getScene().getWindow();

        JdbcAd db_a = new JdbcAd();
    	if( this.isNewAd ) {
	       	db_a.insert(ad);
    	}
    	else {
    		db_a.update(ad);
    	}
       	appController.refreshTableView();
       	owner.hide();
    }

    @FXML
    public void delMaterial(ActionEvent event) throws SQLException {

        Window owner = delButton.getScene().getWindow();

    	int ret = Utils.confirmBox("Confirmez-vous l'effacement de cette annonce?", "Effacement" );
    	if( ret == JOptionPane.YES_OPTION ) {
    		JdbcAd db = new JdbcAd();
    		db.delete(ad.getId());
        	appController.refreshTableView();
    	}
    	owner.hide();
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
			productImage.setImage(image);
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
				if (ad.getPicture().equals("DefaultPicture.jpg")) {
					System.out.println(ad.getId());
					byte[] array = new byte[7]; // length is bounded by 7
					new Random().nextBytes(array);
					String generatedString = new String(array, StandardCharsets.UTF_8);
					newImageName = generatedString + ".jpg";
					ad.setPicture(newImageName);
				}
				else {
					newImageName = ad.getPicture();
				}

			} catch (NullPointerException e) {
				byte[] array = new byte[7]; // length is bounded by 7
				new Random().nextBytes(array);
				String generatedString = new String(array, StandardCharsets.UTF_8);
				newImageName = generatedString + ".jpg";
				ad.setPicture(newImageName);
			}
			Path destinationPath = Paths.get(resourcesPath, newImageName);
			Files.copy(sourceImage.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void initPage( int userId, int adId, AppController appController ) throws SQLException {

    	this.currentUserId = userId;
    	this.appController = appController;
    	// cree une nouvelle annonce associe a l'utilisateur en cours
    	if( adId == 0 ) {
    		ad = new Ad( userId, true );
    		this.isNewAd = true;
    	}
    	else {
    		// Lit l'annonce dans la base de donnees
    		JdbcAd db_a = new JdbcAd();
    		ad = db_a.selectByID(adId);
    		this.isNewAd = false;
    		String info = db_a.readInfoByID(adId);
    		adInfo.setText(info);
    	}
    	
    	// Rempli la comboxbox contenant les types de categorie
    	JdbcMatCat db = new JdbcMatCat();
    	matCatList = db.select(true);
    	matCatObs = FXCollections.observableArrayList(matCatList);
    	
    	categories.setItems(matCatObs);
    	if( !ad.getTitle().isEmpty() )
    		categories.getSelectionModel().select(ad.getCategoryId());
    	/*categories.setConverter(new StringConverter<MatCat>() {
    	    @Override
    	    public String toString(MatCat object) {
    	       return object.nameProperty().get();
    	    }

    	    @Override
    	    public MatCat fromString(String id) {
                return matCatList.stream()
                        .filter(item -> Integer.toString(item.idProperty().get()).equals(id))
                        .collect(Collectors.toList()).get(0);
    	    }
    	});*/
    	
    	// Permet une mise a jour auto des variables
    	Bindings.bindBidirectional(requestRadiobutton.selectedProperty(), ad.isRequestProperty());
    	Bindings.bindBidirectional(nameIdField.textProperty(), ad.titleProperty());
    	Bindings.bindBidirectional(costIdField.textProperty(), ad.costProperty(), new NumberStringConverter());
    	// Ne fonctionne pas
    	//Bindings.bindBidirectional(categories.getSelectionModel().getSelectedItem().idProperty(), material.categoryIdProperty());
    	//Bindings.bindBidirectional(categories.valueProperty(), material.matcatProperty());
    	
        ad.categoryIdProperty().bind(Bindings.createIntegerBinding(() -> {
            MatCat selectedItem = categories.getValue();
            return (selectedItem != null) ? selectedItem.getId() : 0;
        }, categories.valueProperty()));
        
        categories.valueProperty().addListener(new ChangeListener<MatCat>() {
            @Override
            public void changed(ObservableValue ov, MatCat oldData, MatCat newData) {
            	ad.costProperty().setValue( newData.getSuggestedCostd() );
            }
          });
        
    	Bindings.bindBidirectional(descriptionIdField.textProperty(), ad.descriptionProperty());
    	Bindings.bindBidirectional(isAvailable.selectedProperty(), ad.isAvailableProperty());

    	Bindings.bindBidirectional(zipcodeField.textProperty(), ad.localizationProperty());
    	Bindings.bindBidirectional(commentsField.textProperty(), ad.commentsProperty());
    	Bindings.bindBidirectional(durationField.textProperty(), ad.durationInDayProperty(), new NumberStringConverter());
    	Bindings.bindBidirectional(startdateField.valueProperty(), ad.startDateProperty());
    	Bindings.bindBidirectional(enddateField.valueProperty(), ad.endDateProperty());

		// Affiche l'image associee a l'annonce
		String path = "src/main/resources/" + RESOURCE_FOLDER + ad.getPicture();
		System.out.println(path);
		productImage.setImage(new Image(new File(path).toURI().toString()));
		if (productImage.getImage() == null) {
			System.out.println("Image non trouvée");
		}
		else {
			System.out.println(productImage.getImage().toString());
		}

    	// Mise � jour de l'etat des boutons
    	if( userId != ad.getUserId() ) {
			// L'utilisateur n'est pas le proprietaire de l'annonce

    		addButton.setVisible( false );
    		orderButton.setDisable( false );
			commentButton.setDisable( false );
			addImageButton.setVisible(false);
			delButton.setVisible(false);
			nameIdFieldText.setText(nameIdField.getText());
			nameIdField.setVisible(false);
			nameIdFieldText.setVisible(true);
			descriptionText.setText(descriptionIdField.getText());
			descriptionIdField.setVisible(false);
			descriptionText.setVisible(true);
			costText.setText(costIdField.getText() + " Florains");
			costIdField.setVisible(false);
			costText.setVisible(true);
			florainsText.setVisible(false);
			if (!(categories.getSelectionModel().getSelectedItem().getName() == null)){
				categoriesText.setText(categories.getSelectionModel().getSelectedItem().getName());
			}
			else {
				categoriesText.setText("Pas de catégorie");
			}
			categories.setVisible(false);
			categoriesText.setVisible(true);
			isAvailable.setDisable(true);
			if (!(commentsField.getText().isEmpty())){
				commentairesText.setText(commentsField.getText());
			}
			else {
				commentairesText.setText("Pas de commentaires");
			}
			commentsField.setVisible(false);
			commentairesText.setVisible(true);
			if (!(zipcodeField.getText().isEmpty())){
				codePostalText.setText(zipcodeField.getText());
			}
			else {
				codePostalText.setText("Pas de code postal");
			}
			zipcodeField.setVisible(false);
			codePostalText.setVisible(true);
			if (!(durationField.getText().isEmpty())){
				nombreDeJoursText.setText(durationField.getText() + " jours");
			}
			else {
				nombreDeJoursText.setText("Pas de durée");
			}
			durationField.setVisible(false);
			joursLabel.setVisible(false);
			nombreDeJoursText.setVisible(true);
			if (!(startdateField.getValue() == null)){
				startDateText.setText(startdateField.getValue().toString());
			}
			else {
				startDateText.setText("Pas de date de début");
			}
			startdateField.setVisible(false);
			startDateText.setVisible(true);
			if (!(enddateField.getValue() == null)){
				endDateText.setText(enddateField.getValue().toString());
			}
			else {
				endDateText.setText("Pas de date de fin");
			}
			enddateField.setVisible(false);
			endDateText.setVisible(true);
			radioButtonHbox.setVisible(false);
    	}
    	else if( !this.isNewAd ) {
			// L'utilisateur est le proprietaire de l'annonce
			addButton.setText("Modifier");
			delButton.setDisable(false);
			orderButton.setVisible(false);
			commentButton.setVisible(false);
			nameIdFieldText.setVisible(false);
			categoriesText.setVisible(false);
			costText.setVisible(false);
			descriptionText.setVisible(false);
			codePostalText.setVisible(false);
			startDateText.setVisible(false);
			endDateText.setVisible(false);
			nombreDeJoursText.setVisible(false);
			commentairesText.setVisible(false);
		}
    }
}
