package eu.telecomnancy;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import eu.telecomnancy.DAO.JdbcMatCat;
import eu.telecomnancy.DAO.JdbcMessage;
import eu.telecomnancy.DAO.JdbcService;
import eu.telecomnancy.DAO.JdbcStandby;
import eu.telecomnancy.DAO.JdbcUser;
import eu.telecomnancy.base.MatCat;
import eu.telecomnancy.base.Message;
import eu.telecomnancy.base.Service;
import eu.telecomnancy.base.Standby;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
import java.time.LocalDateTime;


public class ServiceRecurrentController {
    private Service service = null;
	private boolean isNewService;
	private int currentUserId;
	private List<MatCat> matCatList;
	private ObservableList<MatCat> matCatObs;
	private AppController appController;
	
	@FXML
	private Label serviceInfo,reservationLabel;
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
	private CheckBox isAvailable;
	@FXML
	private Spinner heure;
	@FXML
	private Spinner minutes;
	@FXML
	private GridPane gridpane;
	@FXML
    private Button addButton, delButton, orderButton, commentButton,accepterReservationButton,refuserReservationButton;
	
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
		
		JdbcUser db_user=new JdbcUser();
		int amount=db_user.selectAmountByID(currentUserId);
        
        if( !service.isIsAvailable() ) {
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
	            if( total >= 1 ) {
		        	ret = Utils.confirmBox("Il y a déjà " + total + " réservations sur cette proposition.\nSouhaitez-vous être "
		        			+ "prévenu dès qu'une disponibilité se présente?", "Proposition non disponible" );
					if( ret == JOptionPane.YES_OPTION ) {
						if (amount-service.getCost()<0){
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
				if (amount-service.getCost()<0){
					Utils.infBox("Vous ne disposez pas d'assez de florains pour réserver cette annonce", "Florains insuffisants");
				}
				else {
					JdbcService db_service=new JdbcService();
					service.setIsAvailable(false);
					db_service.update(service);
					JdbcStandby db_s=new JdbcStandby();
					db_s.insert(new Standby(currentUserId, service.getId(), LocalDate.now()));
					JdbcMessage db_Message=new JdbcMessage();
					db_Message.insert(new Message(0,currentUserId , service.getUserId(), 1, "Votre annonce \""+service.getTitle()+ "\"a été réservée" , LocalDateTime.now()));
					Utils.infBox("Réservation confirmée, l'auteur de l'annonce vous contactera", "Réservation confirmée");
				
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
	
	@FXML
	public void accepterReservation(ActionEvent event) throws SQLException{
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(service.getId());
		db_standby.updateAccepted(service.getId(),firstStandby);
		JdbcUser db_user=new JdbcUser();
		String name=(db_user.selectByID(firstStandby)).getName();
		reservationLabel.setText(name+" utilise votre service");
		accepterReservationButton.setVisible(false);
		refuserReservationButton.setVisible(false);
		JdbcMessage db_Message=new JdbcMessage();
		db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce\""+service.getTitle()+ "\"a été acceptée" , LocalDateTime.now()));
		db_user.updateFlorains(firstStandby, -service.getCost());
		db_user.updateFlorains(service.getUserId(), service.getCost());
		isAvailable.setDisable(false);

	}

	@FXML
	public void refuserReservation(ActionEvent event) throws SQLException{
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(service.getId());
		db_standby.deleteFirst(service.getId(),firstStandby);
		JdbcMessage db_Message=new JdbcMessage();
		db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce\""+service.getTitle()+ "\"a été refusée" ,LocalDateTime.now()));
		if (db_standby.selectCount(service.getId())>=1){
			firstStandby=db_standby.firstStandBy(service.getId());
			JdbcUser db_user=new JdbcUser();
			String name=(db_user.selectByID(firstStandby)).getName();
			reservationLabel.setText(name+" veut réserver votre service");
		}
		else {
			JdbcService db_service=new JdbcService();
			service.setIsAvailable(true);
			db_service.update(service);
			reservationLabel.setText(null);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
		}

	}
	
	@FXML
	public void setAvailable(ActionEvent event) throws SQLException{
		isAvailable.setDisable(true);
		JdbcStandby db_standby=new JdbcStandby();
		int firstStandby=db_standby.firstStandBy(service.getId());
		db_standby.deleteFirst(service.getId(),firstStandby);
		if (db_standby.selectCount(service.getId())>=1){
			firstStandby=db_standby.firstStandBy(service.getId());
			JdbcUser db_user=new JdbcUser();
			String name=(db_user.selectByID(firstStandby)).getName();
			reservationLabel.setText(name+" veut réserver votre matériel");
			JdbcMessage db_Message=new JdbcMessage();
			db_Message.insert(new Message(0,firstStandby,service.getUserId(), 1, "Votre annonce \""+service.getTitle()+ "\" a été réservée" , LocalDateTime.now()));
			db_Message.insert(new Message(0,currentUserId , firstStandby, 1, "Votre réservation pour l'annonce \""+service.getTitle()+ "\" est en cours de traitement" , LocalDateTime.now()));
		
		}
		else {
			JdbcService db_service=new JdbcService();
			service.setIsAvailable(true);
			db_service.update(service);
			reservationLabel.setText(null);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
		}
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
    		addButton.setVisible( false );
    		orderButton.setDisable( false );
			commentButton.setDisable(false);
			delButton.setVisible(false);
			nameIdField.setVisible(false);
			descriptionIdField.setVisible(false);
			costIdField.setVisible(false);
			categories.setVisible(false);
			commentsField.setVisible(false);
			zipcodeField.setVisible(false);
			startDateField.setVisible(false);
			endDateField.setVisible(false);
			days.setVisible(false);
			heure.setVisible(false);
			minutes.setVisible(false);
			reservationLabel.setVisible(false);
			accepterReservationButton.setVisible(false);
			refuserReservationButton.setVisible(false);
			
    	}
    	else if( !this.isNewService ) {
			addButton.setText( "Modifier" );
			delButton.setDisable( false );
			orderButton.setVisible(false);
			commentButton.setVisible(false);
			JdbcStandby db_standby=new JdbcStandby();
			if (db_standby.selectCount(serviceId)>=1){
				int firstStandby=db_standby.firstStandBy(serviceId);
				JdbcUser db_user=new JdbcUser();
				String name=(db_user.selectByID(firstStandby)).getName();
				if (db_standby.firstIsAccepted(serviceId)){
					reservationLabel.setText(name+" utilise votre service");
					accepterReservationButton.setVisible(false);
					refuserReservationButton.setVisible(false);
				}
				else {
					isAvailable.setDisable(true);
					reservationLabel.setText(name+" veut réserver votre service");
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