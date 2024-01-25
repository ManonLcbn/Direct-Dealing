package eu.telecomnancy.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MessageController {

    @FXML
    private GridPane messageGrid;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    public void initPage() {
        System.out.println("TEST") ;
    }

    @FXML
    public void closeMessageWindow() {
        // Fermez la fenêtre de message
        Stage stage = (Stage) messageGrid.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void sendMessage() {
        String message = messageTextField.getText();
        // Faites quelque chose avec le message, par exemple, l'afficher dans la console
        System.out.println("Message envoyé : " + message);
        // Vous pouvez ajouter ici le code pour traiter le message comme l'envoyer à un autre utilisateur, etc.
        // Effacez le texte de la barre de texte
        messageTextField.clear();
    }

}