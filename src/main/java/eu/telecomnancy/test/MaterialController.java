package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

public class MaterialController {

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
	private TextArea descriptionIdField;
	@FXML
	private TextField costIdField;
	@FXML
	private ComboBox<MatCat> categories;
	@FXML
	private CheckBox isAvailable;
	
	@FXML
	private TextField zipcodeField;
	@FXML
	private TextArea commentsField;
	@FXML
	private DatePicker startdateField;
	@FXML
	private DatePicker enddateField;
	@FXML
	private TextField durationField;
	@FXML
	private ToggleGroup type1;
	@FXML
	private GridPane gridpane;



	@FXML
    private Button addButton, delButton, orderButton, commentButton;
	
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

    	// Mise � jour de l'etat des boutons
    	if( userId != ad.getUserId() ) {
    		addButton.setDisable( true );
    		orderButton.setDisable( false );
    	}
    	else if( !this.isNewAd ) {
			addButton.setText( "Modifier" );
			delButton.setDisable( false );
		}
    }
}
