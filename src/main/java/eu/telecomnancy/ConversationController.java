package eu.telecomnancy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import eu.telecomnancy.DAO.JdbcAd;
import eu.telecomnancy.DAO.JdbcMessage;
import eu.telecomnancy.base.Message;

public class ConversationController {

    private int userID;
    private AppController appCtrl;

    @FXML
    private GridPane conversationGridPane;

    public void initPage(int userId, AppController appCtrl) {
        this.userID = userId;
        this.appCtrl = appCtrl;

        // Charger et afficher les conversations avec aperçu du dernier message
        loadConversations();
    }

    private void loadConversations() {
        conversationGridPane.getChildren().clear(); // Clear existing content

        try {
            // Obtenez la liste des utilisateurs avec qui l'utilisateur a échangé des messages
            List<Integer> usersWithMessages = JdbcMessage.getUsersWithMessages(userID);

            System.out.println("usersWithMessages contient :") ;
            for(int i=0; i<usersWithMessages.size(); i++){
                System.out.println(usersWithMessages.get(i)) ;
            }
            System.out.println("Voilà !") ;

            int rowIndex = 0;
            for (int otherUserId : usersWithMessages) {
                // Obtenez le dernier message de la conversation
                System.out.println(otherUserId) ;
                Message lastMessage = JdbcMessage.getLastMessageBetweenUsers(userID, otherUserId);

                System.out.println(lastMessage.getBody()) ;

                if (lastMessage != null) {
                    // Créez des Labels pour afficher le nom de l'utilisateur et le dernier message
                    String NomEnGras ;
                    if (lastMessage.getSender() == userID) {
                        NomEnGras = lastMessage.getRecipientName();
                    } else {
                        NomEnGras = lastMessage.getSenderName();
                    }
                    Label userNameLabel = new Label(NomEnGras);
                    userNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14pt;");
                    userNameLabel.getStyleClass().add("label-hover");

                    final int finalRowIndex = rowIndex;

                    // Ajouter un événement de survol sur le nom de l'utilisateur
                    userNameLabel.setOnMouseEntered(event -> handleMouseEnter(finalRowIndex));
                    userNameLabel.setOnMouseExited(event -> handleMouseExit(finalRowIndex));
                    conversationGridPane.add(userNameLabel, 0, rowIndex);

                    // Ajouter un événement de clic sur le nom de l'utilisateur
                    userNameLabel.setOnMouseClicked(event -> handleSendMessage(otherUserId));

                    if(lastMessage.getSender() == userID) {
                        lastMessage.setBody("Vous : " + lastMessage.getBody());
                    } else {
                        lastMessage.setBody(lastMessage.getSenderName() + " : " + lastMessage.getBody());
                    }

                    if(lastMessage.getBody().length() > 300) {
                        lastMessage.setBody(lastMessage.getBody().substring(0, 300) + "...");
                    }
                    Label lastMessageLabel = new Label(lastMessage.getBody());
                    lastMessageLabel.getStyleClass().add("message-hover");
                    // Ajouter un événement de survol sur le dernier message
                    lastMessageLabel.setOnMouseEntered(event -> handleMouseEnter(finalRowIndex + 1));
                    lastMessageLabel.setOnMouseExited(event -> handleMouseExit(finalRowIndex + 1));
                    conversationGridPane.add(lastMessageLabel, 0, rowIndex + 1);


                    // Ajouter un événement de clic sur le dernier message
                    lastMessageLabel.setOnMouseClicked(event -> handleSendMessage(otherUserId));

                    rowIndex += 2; // Incrémentez deux fois pour laisser de l'espace entre les conversations
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez les erreurs SQL de manière appropriée
        }
    }

    private void handleMouseEnter(int rowIndex) {
        conversationGridPane.getChildren().get(rowIndex).getStyleClass().add("label-hover");
        conversationGridPane.getChildren().get(rowIndex + 1).getStyleClass().add("message-hover");
    }
    
    private void handleMouseExit(int rowIndex) {
        conversationGridPane.getChildren().get(rowIndex).getStyleClass().remove("label-hover");
        conversationGridPane.getChildren().get(rowIndex + 1).getStyleClass().remove("message-hover");
    }

    private void handleSendMessage(int recipientId) {
        // Implémentez ici la logique pour envoyer un message au destinataire (recipientId)
        System.out.println("Sending a message to user with ID: " + recipientId);

        Stage messageStage = new Stage();
		MessageView messageView = new MessageView();
        try{
            int int1 = recipientId;
			GridPane messageRoot = messageView.loadPage(userID, int1, appCtrl);

		// Créez une nouvelle fenêtre pour afficher le message
		Scene messageScene = new Scene(messageRoot, 400, 200);
		messageScene.getStylesheets().add(getClass().getResource(Utils.SRC_URL + "/application.css").toExternalForm());
		messageStage.setTitle("Envoyer un message");
		messageStage.setScene(messageScene);
		messageStage.show();
        }
        catch(IOException e) {
            e.printStackTrace();}
    }

    @FXML
    private void handleSendMessage(ActionEvent event) {
        // Implémentez ici la logique pour envoyer un message en général (à tous les destinataires, par exemple)
        System.out.println("Sending a general message...");
    }
}
