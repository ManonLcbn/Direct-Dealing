package eu.telecomnancy;

import java.sql.SQLException;

import eu.telecomnancy.DAO.JdbcStandby;
import eu.telecomnancy.base.Standby;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class StbLimitController {
	
	private Standby booking;
	
	@FXML
	private Button confirmButton, cancelButton;
	
	@FXML
	private DatePicker endDateBooking;
	
	@FXML
    public void confirm(ActionEvent event) throws SQLException {
		JdbcStandby db = new JdbcStandby();
		db.insert(booking);	
		confirmButton.getScene().getWindow().hide();
	}
	
	@FXML
    public void cancel(ActionEvent event) {
		cancelButton.getScene().getWindow().hide();
	}

    public void initPage( Standby booking ) {
    	this.booking = booking;
    	
    	Bindings.bindBidirectional(endDateBooking.valueProperty(), booking.endDateProperty());
    }

}