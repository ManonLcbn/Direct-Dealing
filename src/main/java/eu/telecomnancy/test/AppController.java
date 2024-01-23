package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.telecomnancy.test.DAO.JdbcAdRow;
import eu.telecomnancy.test.DAO.JdbcFeedback;
import eu.telecomnancy.test.base.AdRow;
import eu.telecomnancy.test.base.Feedback;
import eu.telecomnancy.test.base.User;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AppController {

	private User user;
	private int selectedAdId;
	private JdbcAdRow db_a = new JdbcAdRow();
	private ObservableList<AdRow> adRows = FXCollections.observableArrayList();
	private Property<ObservableList<AdRow>> adRowsProperty = new SimpleObjectProperty<>(adRows); 
	private JdbcFeedback db_f = new JdbcFeedback();
	private ObservableList<Feedback> feedbackRows = FXCollections.observableArrayList();
	private Property<ObservableList<Feedback>> feedbackRowsProperty = new SimpleObjectProperty<>(feedbackRows); 


    @FXML
    private Button viewProfileButton;
    @FXML
    private Button aboutButton;

    @FXML
    private ToggleGroup filter1;
    @FXML
    private TableView<AdRow> adsTableView;
    @FXML
    private TableColumn<AdRow,String> adRow1, adRow2, adRow3, adRow4;
    @FXML
    private TextField keywords;
    @FXML
    private TableView<Feedback> feedbackTableView;
    @FXML
    private TableColumn<Feedback,String> fbRow1, fbRow2, fbRow3;
    
    @FXML
    public void viewProfile(ActionEvent event) {
    	Stage secondaryStage = new Stage();
        try {
        	ProfileView page = new ProfileView( user.getId() );
	        GridPane root = page.loadPage();
			Scene scene = new Scene(root,550,500);
			
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			secondaryStage.setTitle("TelecomNancy DirectDealing - Profile");
			secondaryStage.setScene(scene);
			secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    private void openMaterialForm( boolean isAutoSelect ) {
    	Stage secondaryStage = new Stage();
        try {
        	GenericView page;
        	String Title = "TelecomNancy DirectDealing";
        	
        	if( filter1.getToggles().get(1).isSelected() || isAutoSelect ) {
        		page = new MaterialView();
        		Title += " - Material";
        	}
        	else {
        		page = new ServicePonctualView();
        		Title += " - Service";
        	}
        	
	        GridPane root = page.loadPage(user.getId(), selectedAdId, this);
			Scene scene = new Scene(root,1000,700);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			secondaryStage.setTitle(Title);
			secondaryStage.setScene(scene);
			secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void addDeal(ActionEvent event) {
    	selectedAdId = 0;
    	openMaterialForm(false);
    	event.consume();
    }

    @FXML
    public void delDeal(ActionEvent event) {
    	event.consume();
    }

    @FXML
    public void aboutDeal(ActionEvent event) {
    	AdRow selectedRow = adsTableView.getSelectionModel().getSelectedItem();
    	boolean isValidAd = ( selectedRow.isMaterial() );
    	openMaterialForm(isValidAd);
    	event.consume();
    }
  
    public void refreshTableView() throws SQLException {
    	List<AdRow> rowList = db_a.selectAll();
    	adRows.setAll(rowList);
    	//List<Feedback> fbRowList = db_f.selectByAdId(selectedAdId);
		//feedbackRows.setAll(fbRowList);
    	aboutButton.setDisable(true);
    }
    
    public void initPage( User user ) throws SQLException {
    	this.user = user;
    	
    	List<AdRow> rowList = db_a.selectAll();
    	adRows.setAll(rowList);

        adsTableView.setPlaceholder(new Label("Aucune annonce selon les critères renseignés"));
        
        adRow1.setCellValueFactory( new PropertyValueFactory<AdRow,String>("Localization"));
        adRow2.setCellValueFactory( new PropertyValueFactory<AdRow,String>("ProfileName"));
        adRow3.setCellValueFactory( new PropertyValueFactory<AdRow,String>("Type"));
        adRow4.setCellValueFactory( new PropertyValueFactory<AdRow,String>("Info"));
        
        adsTableView.setItems(adRows);
        
        adsTableView.setRowFactory( a -> {
            TableRow<AdRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	if( !row.isEmpty() ) {
	                AdRow rowData = row.getItem();
	                selectedAdId = rowData.getAdId();
	                // Complete la table des avis utilisateur
					try {
				    	List<Feedback> fbRowList = db_f.selectByAdId(selectedAdId);
			    		feedbackRows.setAll(fbRowList);
					} catch (SQLException e) {
						e.printStackTrace();
					}
	                
	                if (event.getClickCount() == 1) {
	                    aboutButton.setDisable(false);
	                }
	                else if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	boolean isValidAd = rowData.isMaterial();
	                	openMaterialForm(isValidAd);
	                }
            	}
            	else {
            		aboutButton.setDisable(true);
            		selectedAdId = 0;
            	}
            });
            return row ;
        });
        
        feedbackTableView.setPlaceholder(new Label("Aucun avis pour cette annonce"));
        
        fbRow1.setCellValueFactory( new PropertyValueFactory<Feedback,String>("note"));
        fbRow2.setCellValueFactory( new PropertyValueFactory<Feedback,String>("username"));
        fbRow3.setCellValueFactory( new PropertyValueFactory<Feedback,String>("comments"));

    	keywords.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String keywordLine = keywords.getText();
	        	try {
	        		List<AdRow> rList;
		        	if( keywordLine.isEmpty() )
		        		rList = db_a.selectAll();
		        	else
		        		rList = db_a.selectByKeywords( keywordLine );
	        		adRows.setAll(rList);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
               
    	Bindings.bindBidirectional(adsTableView.itemsProperty(), adRowsProperty);
    	Bindings.bindBidirectional(feedbackTableView.itemsProperty(), feedbackRowsProperty);

    }
    
}
