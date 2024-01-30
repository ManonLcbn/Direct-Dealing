package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import eu.telecomnancy.test.DAO.JdbcMatCat;
import eu.telecomnancy.test.DAO.JdbcService;
import eu.telecomnancy.test.DAO.JdbcStandby;
import eu.telecomnancy.test.base.MatCat;
import eu.telecomnancy.test.base.Service;
import eu.telecomnancy.test.base.Standby;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.NumberStringConverter;

public class ServiceRecurrentController {
    private Service service = null;
	private boolean isNewService;
	private int currentUserId;
	private List<MatCat> matCatList;
	private ObservableList<MatCat> matCatObs;
	private AppController appController;
	
	@FXML
	private Label serviceInfo;
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
	private TextField zipcodeField;
	@FXML
	private TextArea commentsField;
	@FXML
	private RadioButton frequencyRadiobutton;
	@FXML
    private ToggleGroup type1;
	@FXML
	private DatePicker startDateField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private ComboBox<String> days;
	@FXML
	private Spinner heure;
	@FXML
	private Spinner minutes;
	@FXML
	private GridPane gridpane;
	@FXML
    private Button addButton, delButton, orderButton, commentButton;
	
    @FXML
    public void commentAd(ActionEvent event) throws IOException {
    	Stage thirdStage = new Stage();
   		FeedbackView page = new FeedbackView();
	    GridPane root = page.loadPage( currentUserId, service.getId(), appController );
		Scene scene = new Scene(root,400,200);
		scene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
		thirdStage.setTitle("Donnez votre avis");
		thirdStage.setScene(scene);
		thirdStage.show();          		
    }
    
    @FXML
    public void bookService(ActionEvent event) throws SQLException {

        Window owner = addButton.getScene().getWindow();
        
        if( service.isIsAvailable() ) {
        	// Service non disponible
        	int ret = JOptionPane.NO_OPTION;
        	// test si l'utilisateur a deja une reservation
            Standby book = new Standby(currentUserId,service.getId(),LocalDate.now());
            JdbcStandby db_s = new JdbcStandby();
            boolean isExists = db_s.isExists(book);	
            if( isExists ) {
            	Utils.infBox("Vous avez déjà une réservation\nsur cette annonce", "Réservation existante");
            }
            else {
	        	JdbcStandby db = new JdbcStandby();
	        	int total = db.selectCount(service.getId());
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

       	owner.hide();
    }

    @FXML
    public void addService(ActionEvent event) throws SQLException {

        Window owner = addButton.getScene().getWindow();

        JdbcService db_s = new JdbcService();
    	if( this.isNewService ) {
	       	db_s.insert(service);
    	}
    	else {
    		db_s.update(service);
    	}
       	appController.refreshTableView();
       	owner.hide();
    }

    @FXML
    public void delService(ActionEvent event) throws SQLException {

        Window owner = delButton.getScene().getWindow();

    	int ret = Utils.confirmBox("Confirmez-vous l'effacement de cette annonce?", "Effacement" );
    	if( ret == JOptionPane.YES_OPTION ) {
    		JdbcService db = new JdbcService();
    		db.delete(service.getId());
        	appController.refreshTableView();
    	}
    	owner.hide();
    }

	
    @FXML
    public void isPonctual(ActionEvent event) throws IOException{
        Window actualWindow=frequencyRadiobutton.getScene().getWindow();
		actualWindow.hide();
		Stage thirdStage = new Stage();
   		ServicePonctualView page = new ServicePonctualView();
	    GridPane root = page.loadPage( currentUserId, service.getId(), appController );
		Scene scene = new Scene(root,1200,700);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		thirdStage.setTitle("TelecomNancy DirectDealing - Service");
		thirdStage.setScene(scene);
		thirdStage.show();
    }
	

	

    public void initPage( int userId, int serviceId, AppController appController ) throws SQLException {

    	this.currentUserId = userId;
    	this.appController = appController;
    	// cree une nouvelle annonce associe a l'utilisateur en cours
    	if( serviceId == 0 ) {
    		service = new Service( userId );
    		this.isNewService = true;
    	}
    	else {
    		// Lit l'annonce dans la base de donnees
    		JdbcService db_s = new JdbcService();
    		service = db_s.selectByID(serviceId);
    		this.isNewService = false;
    		String info = db_s.readInfoByID(serviceId);
    		serviceInfo.setText(info);
    	}
    	
    	// Rempli la comboxbox contenant les types de categorie
    	JdbcMatCat db = new JdbcMatCat();
    	matCatList = db.select(false);
    	matCatObs = FXCollections.observableArrayList(matCatList);
    	
    	categories.setItems(matCatObs);
    	if( !service.getTitle().isEmpty() )
    		categories.getSelectionModel().select(service.getCategoryId());
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
    	Bindings.bindBidirectional(requestRadiobutton.selectedProperty(), service.isRequestProperty());
    	Bindings.bindBidirectional(nameIdField.textProperty(), service.titleProperty());
    	Bindings.bindBidirectional(costIdField.textProperty(), service.costProperty(), new NumberStringConverter());
    	// Ne fonctionne pas
    	//Bindings.bindBidirectional(categories.getSelectionModel().getSelectedItem().idProperty(), material.categoryIdProperty());
    	//Bindings.bindBidirectional(categories.valueProperty(), material.matcatProperty());
    	
        service.categoryIdProperty().bind(Bindings.createIntegerBinding(() -> {
            MatCat selectedItem = categories.getValue();
            return (selectedItem != null) ? selectedItem.getId() : 0;
        }, categories.valueProperty()));
        
        categories.valueProperty().addListener(new ChangeListener<MatCat>() {
            @Override
            public void changed(ObservableValue ov, MatCat oldData, MatCat newData) {
            	service.costProperty().setValue( newData.getSuggestedCostd() );
            }
          });
        
    	Bindings.bindBidirectional(descriptionIdField.textProperty(), service.descriptionProperty());

    	Bindings.bindBidirectional(zipcodeField.textProperty(), service.localizationProperty());
    	Bindings.bindBidirectional(commentsField.textProperty(), service.commentsProperty());
		

		
    	// Mise à jour de l'etat des boutons
    	if( userId != service.getUserId() ) {
    		addButton.setDisable( true );
    		orderButton.setDisable( false );
    	}
    	else if( !this.isNewService ) {
			addButton.setText( "Modifier" );
			delButton.setDisable( false );
		}
    }
}