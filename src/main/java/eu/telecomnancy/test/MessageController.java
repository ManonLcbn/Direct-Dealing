package eu.telecomnancy.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import eu.telecomnancy.test.base.* ;
import eu.telecomnancy.test.DAO.* ;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


public class MessageController {

    private int senderUserID ;
    private int receiverUserID ;
    private List<Message> messages ;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private GridPane messageGrid;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    public void initPage(int userId, int adId) {
        System.out.println("TEST") ;
        JdbcAd test = new JdbcAd() ;
        int i = 1 ;
        try{
            int int1 = test.selectUserIdByID(adId) ;
            this.senderUserID = userId ;
            this.receiverUserID = int1 ;
        }
        catch(SQLException e) {
            e.printStackTrace();}

        try{
            List<Message> messages = JdbcMessage.getMessagesBetweenUsers(senderUserID, receiverUserID);
            updateMessageTextArea(messages);
        }
        catch(SQLException e) {
            e.printStackTrace();}

    }

    // Méthode pour mettre à jour le contenu du TextArea avec les messages
    private void updateMessageTextArea(List<Message> messages) {
        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            // Construisez la représentation de chaîne du message selon vos besoins
            String messageString = buildMessageString(message);
            sb.append(messageString).append("\n");
        }
        messageTextArea.setText(sb.toString());
    }

    // Méthode pour construire une représentation de chaîne de message
    private String buildMessageString(Message message) {
        // À adapter en fonction de votre structure de message
        return message.getSenderName() + ": " + message.getBody();
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

        // Obtention de la date et de l'heure actuelles

        LocalDateTime currentDateTime = LocalDateTime.now();

        Message messageInstance = new Message(1, this.senderUserID, this.receiverUserID, 1, message, currentDateTime);
        System.out.println("ON EST LA !") ;
        JdbcMessage test = new JdbcMessage();
        System.out.println("ON EST LA 2 !") ;
        try {
            test.insert(messageInstance) ;
            System.out.println("ON EST LA 3 !") ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("envoyeur : " + this.senderUserID) ;
        System.out.println("receveur : " + this.receiverUserID) ;
        printMessagesConsole(this.senderUserID,this.receiverUserID);

        // Après l'envoi, mettez à jour le TextArea avec les nouveaux messages
        try {
            List<Message> updatedMessages = JdbcMessage.getMessagesBetweenUsers(senderUserID, receiverUserID);
            updateMessageTextArea(updatedMessages);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loadMessages() {
        JdbcMessage jdbcMessage = new JdbcMessage();
        try {
            List<Message> messages = jdbcMessage.getMessagesBetweenUsers(senderUserID, receiverUserID);
            displayMessages(messages);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur, afficher un message à l'utilisateur, etc.
        }
    }

    private void displayMessages(List<Message> messages) {
        StringBuilder messageDisplay = new StringBuilder();
        for (Message message : messages) {
            messageDisplay.append("From: ").append(message.getSender()).append("\n");
            messageDisplay.append("Subject: ").append(message.getSubject()).append("\n");
            messageDisplay.append("Date: ").append(message.getDateUTC()).append("\n");
            messageDisplay.append("Body: ").append(message.getBody()).append("\n\n");
        }
        messageTextArea.setText(messageDisplay.toString());
    }

    private void printMessagesConsole(int user1,int user2){

        JdbcMessage test = new JdbcMessage();
        try {
            List<Message> list = test.getMessagesBetweenUsers(user1, user2);
            if(list.isEmpty()){System.out.println("LA LISTE EST VIDE !!");}
            for(Message message : list){
                System.out.println(message.getBody());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}