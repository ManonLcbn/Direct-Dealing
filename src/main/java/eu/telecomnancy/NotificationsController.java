package eu.telecomnancy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import eu.telecomnancy.base.* ;
import eu.telecomnancy.DAO.* ;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationsController {

    private int UserID ;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private GridPane messageGrid;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    public void initPage(int userId) {
        System.out.println("TEST") ;
        this.UserID = userId ;

    }
    
}