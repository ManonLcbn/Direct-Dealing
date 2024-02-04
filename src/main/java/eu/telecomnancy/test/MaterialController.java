package eu.telecomnancy.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import eu.telecomnancy.test.DAO.*;
import eu.telecomnancy.test.base.*;
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
import java.time.LocalDateTime;



public class MaterialController {

	private static final String RESOURCE_FOLDER = "images/";
	private Ad ad = null;
	private boolean isNewAd;
	private int currentUserId;
	private List<MatCat> matCatList;
	private ObservableList<MatCat> matCatObs;
	private AppController appController;
	
	@FXML
	private Label adInfo,reservationLabel;
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
    private Button addButton, delButton, orderButton, commentButton, addImageButton,accepterReservationButton,refuserReservationButton;

	@FXML
	public void openMessageConversation() throws IOException {
		System.out.println("Tentative d'envoyer un message !");

		Stage messageStage = new Stage();
		MessageView messageView = new MessageView();
		GridPane messageRoot = messageView.loadPage(currentUserId, ad.getId(), appController);

		// Créez une nouvelle fenêtre pour afficher le message
		Scene messageScene = new Scene(messageRoot, 400, 200);
		messageScene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
		messageStage.setTitle("Envoyer un message");
		messageStage.setScene(messageScene);
		messageStage.show();
	}
	
    @FXML
    public void commentAd(ActionEvent event) throws IOException {
    	Stage thirdStage = new Stage();
   		FeedbackView page = new FeedbackView();
	    GridPane root = page.loadPage( currentUserId, ad.getId(), appController );
		Scene scene = new Scene(root,400,200);
		scene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
		thirdStage.setTitle("Donnez votre avis");
		thirdStage.setScene(scene);
		thirdStage.show();          		
    }
    
    @FXML
    public void bookMaterial(ActionEvent event) throws SQLException {

        Window owner = addButton.getScene().getWindow();
		JdbcUser db_user=new JdbcUser();
		int amount=db_user.selectAmountByID(currentUserId);
        
        if( !ad.isIsAvailable() ) {
        	// Service non disponible
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
	            if( total >= 1 ) {
		        	ret = Utils.confirmBox("Il y a déjà " + total + " réservations sur cette proposition.\nSouhaitez-vous être "
		        			+ "prévenu dès qu'une disponibilité se présente?", "Proposition non disponible" );
					if( ret == JOptionPane.YES_OPTION ) {
						if (amount-ad.getCost()<0){
							Utils.infBox("Vous ne disposez pas d'assez de florains pour réserver cette annonce", "Florains insuffisants");
						}
						else {
							Stage thirdStage = new Stage();
							try {
								StbLimitView page = new StbLimitView();
								GridPane root = page.loadPage(book);
								Scene scene = new Scene(root,400,200);
								scene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
								thirdStage.setTitle("Date limite de la demande");
								thirdStage.setScene(scene);
								thirdStage.show();          		
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					}
	        	}
	        	
	        	
            }
        	
        }
		else {
			
			int ret = Utils.confirmBox("Souhaitez-vous réserver ce service ?", "Proposition disponible" );
			if( ret == JOptionPane.YES_OPTION ) {
				if (amount-ad.getCost()<0){
					Utils.infBox("Vous ne disposez pas d'assez de florains pour réserver cette annonce", "Florains insuffisants");
				}
				else {
					JdbcAd db_ad=new JdbcAd();
					ad.setIsAvailable(false);
					db_ad.update(ad);
					JdbcStandby db_s=new JdbcStandby();
					db_s.insert(new Standby(currentUserId, ad.getId(), LocalDate.now()));
					JdbcMessage db_Message=new JdbcMessage();
					db_Message.insert(new Message(0,currentUserId , ad.getUserId(), 1, "Votre annonce \""+ad.getTitle()+ "\"a été réservée" , LocalDateTime.now()));
					Utils.infBox("Réservation confirmée, l'auteur de l'annonce vous contactera", "Réservation confirmée");
				
				}
			}
		}
		
       	owner.hide();
    }

    @FXML
    public void addMaterial(ActionEvent event) throws SQLException {

		if (addButton.getText().equals("Signaler")) {
			int signaleur = currentUserId;
			int signale = ad.getUserId();
			int annonce = ad.getId();
			JdbcSignalement db = new JdbcSignalement();
			db.insert(new Signalement(0, "Annonce " + annonce +  " de l'utilisateur " + signale + " signalée par " + signaleur));
		}
		else {
			Window owner = addButton.getScene().getWindow();

			JdbcAd db_a = new JdbcAd();
			if (this.isNewAd) {
				db_a.insert(ad);
			} else {
				db_a.update(ad);
			}
			appController.refreshTableView();
			owner.hide();
		}
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
					String generatedString = new Random().ints(48, 123)
							.filter(i -> (i < 58) || (i > 64 && i < 91) || (i > 96))
							.limit(16)
							.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
							.toString();
					newImageName = generatedString + ".jpg";
					ad.setPicture(newImageName);
				}
				else {
					newImageName = ad.getPicture();
				}

			} catch (NullPointerException e) {
				String generatedString = new Random().ints(48, 123)
						.filter(i -> (i < 58) || (i > 64 && i < 91) || (i > 96))
						.limit(16)
						.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
						.toString();
				newImageName = generatedString + ".jpg";
				ad.setPicture(newImageName);
			}
			Path destinationPath = Paths.get(resourcesPath, newImageName);
			Files.copy(sourceImage.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void accepterReservation(ActionEvent event) throws SQLException{
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(ad.getId());
		db_standby.updateAccepted(ad.getId(),firstStandby);
		JdbcUser db_user=new JdbcUser();
		String name=(db_user.selectByID(firstStandby)).getName();
		reservationLabel.setText(name+" utilise votre matériel");
		accepterReservationButton.setVisible(false);
		refuserReservationButton.setVisible(false);
		JdbcMessage db_Message=new JdbcMessage();
		db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce\""+ad.getTitle()+ "\"a été acceptée" , LocalDateTime.now()));
		db_user.updateFlorains(firstStandby, -ad.getCost());
		db_user.updateFlorains(ad.getUserId(), ad.getCost());
		isAvailable.setDisable(false);

	}

	@FXML
	public void refuserReservation(ActionEvent event) throws SQLException{
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(ad.getId());
		db_standby.deleteFirst(ad.getId(),firstStandby);
		JdbcMessage db_Message=new JdbcMessage();
		db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce\""+ad.getTitle()+ "\"a été refusée" , LocalDateTime.now()));
		if (db_standby.selectCount(ad.getId())>=1){
			firstStandby=db_standby.firstStandBy(ad.getId());
			JdbcUser db_user=new JdbcUser();
			String name=(db_user.selectByID(firstStandby)).getName();
			reservationLabel.setText(name+" veut réserver votre matériel");
		}
		else {
			JdbcAd db_ad=new JdbcAd();
			ad.setIsAvailable(true);
			db_ad.update(ad);
			reservationLabel.setText(null);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
		}

	}

	@FXML
	public void setAvailable(ActionEvent event) throws SQLException{
		isAvailable.setDisable(true);
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(ad.getId());
		db_standby.deleteFirst(ad.getId(),firstStandby);
		if (db_standby.selectCount(ad.getId())>=1){
			firstStandby=db_standby.firstStandBy(ad.getId());
			JdbcUser db_user=new JdbcUser();
			String name=(db_user.selectByID(firstStandby)).getName();
			reservationLabel.setText(name+" veut réserver votre matériel");
			JdbcMessage db_Message=new JdbcMessage();
			db_Message.insert(new Message(0,firstStandby,ad.getUserId(), 1, "Votre annonce \""+ad.getTitle()+ "\" a été réservée" , LocalDateTime.now()));
			db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce \""+ad.getTitle()+ "\" est en cours de traitement" , LocalDateTime.now()));
		
		}
		else {
			JdbcAd db_ad=new JdbcAd();
			ad.setIsAvailable(true);
			db_ad.update(ad);
			reservationLabel.setText(null);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
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

    		addButton.setText("Signaler");
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
			reservationLabel.setVisible(false);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
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
			JdbcStandby db_standby=new JdbcStandby();
			if (db_standby.selectCount(adId)>=1){
				int firstStandby=db_standby.firstStandBy(adId);
				JdbcUser db_user=new JdbcUser();
				String name=(db_user.selectByID(firstStandby)).getName();
				if (db_standby.firstIsAccepted(adId)){
					reservationLabel.setText(name+" utilise votre matériel");
					accepterReservationButton.setVisible(false);
					refuserReservationButton.setVisible(false);
				}
				else {
					isAvailable.setDisable(true);
					reservationLabel.setText(name+" veut réserver votre matériel");
				}
			}
			else {
				isAvailable.setDisable(true);
				reservationLabel.setText(null);
				accepterReservationButton.setVisible(false);
				refuserReservationButton.setVisible(false);
			}
		}
		else {
			isAvailable.setDisable(true);
			reservationLabel.setVisible(false);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
		}
    }
}