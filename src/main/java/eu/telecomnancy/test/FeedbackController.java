package eu.telecomnancy.test;

import java.sql.SQLException;

import eu.telecomnancy.test.DAO.JdbcFeedback;
import eu.telecomnancy.test.base.Feedback;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FeedbackController {
	
	private AppController appController;
	
	private Feedback myNote = new Feedback();
	@FXML
	private Button confirmButton, cancelButton;
	@FXML
	private ChoiceBox<Integer> note;
	@FXML
	private TextArea comments;
		
	@FXML
    public void confirm(ActionEvent event) throws SQLException {
		JdbcFeedback db = new JdbcFeedback();
		db.insert(myNote);
		appController.refreshTableView();
		confirmButton.getScene().getWindow().hide();
	}
	
	@FXML
    public void cancel(ActionEvent event) {
		cancelButton.getScene().getWindow().hide();
	}

    public void initPage( int userId, int adId, AppController appController ) {
    	
    	this.appController = appController;
    	myNote.setUserId(userId);
    	myNote.setAdId(adId);
    	note.setItems(FXCollections.observableArrayList( 1, 2, 3, 4, 5));
        myNote.noteProperty().bind(note.valueProperty());
    	Bindings.bindBidirectional(comments.textProperty(), myNote.commentsProperty());
    }

}
