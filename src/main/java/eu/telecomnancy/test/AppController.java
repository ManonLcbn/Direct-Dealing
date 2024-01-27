package eu.telecomnancy.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.telecomnancy.test.DAO.JdbcAdRow;
import eu.telecomnancy.test.DAO.JdbcFeedback;
import eu.telecomnancy.test.DAO.JdbcServiceRow;
import eu.telecomnancy.test.base.AdRow;
import eu.telecomnancy.test.base.ServiceRow;
import eu.telecomnancy.test.base.AnnonceRow;
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
	private JdbcServiceRow db_s=new JdbcServiceRow();
	private ObservableList<AnnonceRow> annonceRows = FXCollections.observableArrayList();
	private Property<ObservableList<AnnonceRow>> annonceRowsProperty = new SimpleObjectProperty<>(annonceRows); 
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
    private TableView<AnnonceRow> annoncesTableView;
    @FXML
    private TableColumn<AnnonceRow,String> annonceRow1, annonceRow2, annonceRow3, annonceRow4;
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
			Scene scene = new Scene(root,750,650);
			
			scene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
			secondaryStage.setTitle("TelecomNancy DirectDealing - Profile");
			secondaryStage.setScene(scene);
			secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    private void openMaterialForm( boolean isAutoSelect,boolean isRecurrent ) {
    	Stage secondaryStage = new Stage();
        try {
        	GenericView page;
        	String Title = "TelecomNancy DirectDealing";
        	
        	if( filter1.getToggles().get(1).isSelected() || isAutoSelect ) {
        		page = new MaterialView();
        		Title += " - Material";
        	}
        	else {
				if (isRecurrent){
					page=new ServiceRecurrentView();
				}
				else {
					page = new ServicePonctualView();
				}
        		Title += " - Service";
        	}
        	
	        GridPane root = page.loadPage(user.getId(), selectedAdId, this);
			Scene scene = new Scene(root,1200,700);
			scene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
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
    	openMaterialForm(false,false);
    	event.consume();
    }

    @FXML
    public void delDeal(ActionEvent event) {
    	event.consume();
    }

    @FXML
    public void aboutDeal(ActionEvent event) {
    	AnnonceRow selectedRow = annoncesTableView.getSelectionModel().getSelectedItem();
    	boolean isValidAd = ( selectedRow.isMaterial() );
		boolean isRecurrent=false;
		if (!isValidAd && !(((ServiceRow)selectedRow).isPonctual())){
			isRecurrent=true;
		}
    	openMaterialForm(isValidAd,isRecurrent);
    	event.consume();
    }
  
    public void refreshTableView() throws SQLException {
    	List<AdRow> rowList = db_a.selectAll();
    	annonceRows.setAll(rowList);
    	//List<Feedback> fbRowList = db_f.selectByAdId(selectedAdId);
		//feedbackRows.setAll(fbRowList);
    	aboutButton.setDisable(true);
    }

	@FXML
	public void showServices(ActionEvent event) throws SQLException{
		List<ServiceRow> rowList=db_s.selectAll();
		annonceRows.setAll(rowList);
		aboutButton.setDisable(true);
	}

	@FXML
	public void showAds(ActionEvent event) throws SQLException{
		List<AdRow> rowList=db_a.selectAll();
		annonceRows.setAll(rowList);
		aboutButton.setDisable(true);
	}
	
    
    public void initPage( User user ) throws SQLException {
    	this.user = user;
    	
    	List<AdRow> rowList = db_a.selectAll();
    	annonceRows.setAll(rowList);

        annoncesTableView.setPlaceholder(new Label("Aucune annonce selon les critères renseignés"));
        
        annonceRow1.setCellValueFactory( new PropertyValueFactory<AnnonceRow,String>("Localization"));
        annonceRow2.setCellValueFactory( new PropertyValueFactory<AnnonceRow,String>("ProfileName"));
        annonceRow3.setCellValueFactory( new PropertyValueFactory<AnnonceRow,String>("Type"));
        annonceRow4.setCellValueFactory( new PropertyValueFactory<AnnonceRow,String>("Info"));
        
        annoncesTableView.setItems(annonceRows);
        
        annoncesTableView.setRowFactory( a -> {
            TableRow<AnnonceRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
            	if( !row.isEmpty() ) {
	                AnnonceRow rowData = row.getItem();
	                selectedAdId = (rowData).getId();
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
	                	boolean isRecurrent=false;
						if (!isValidAd && !(((ServiceRow)rowData).isPonctual())){
							isRecurrent=true;
						}
						openMaterialForm(isValidAd,isRecurrent);
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
	        		annonceRows.setAll(rList);
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
               
    	Bindings.bindBidirectional(annoncesTableView.itemsProperty(), annonceRowsProperty);
    	Bindings.bindBidirectional(feedbackTableView.itemsProperty(), feedbackRowsProperty);

    }
    
}