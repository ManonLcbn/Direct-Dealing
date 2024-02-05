package eu.telecomnancy;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import eu.telecomnancy.base.Message;
import eu.telecomnancy.DAO.JdbcMessage;

import java.sql.SQLException;
import java.util.List;

import javafx.scene.layout.GridPane;

public class ConversationController {

    private int userID;

    @FXML
    private GridPane conversationGridPane;

    public void initPage(int userId) {
        this.userID = userId;

        // Charger et afficher les conversations avec aperçu du dernier message
        loadConversations();
    }

    private void loadConversations() {
        conversationGridPane.getChildren().clear(); // Clear existing content

        try {
            // Obtenez la liste des utilisateurs avec qui l'utilisateur a échangé des messages
            List<Integer> usersWithMessages = JdbcMessage.getUsersWithMessages(userID);

            int rowIndex = 0;
            for (int otherUserId : usersWithMessages) {
                // Obtenez le dernier message de la conversation
                Message lastMessage = JdbcMessage.getLastMessageBetweenUsers(userID, otherUserId);

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
                    conversationGridPane.add(userNameLabel, 0, rowIndex);

                    if(lastMessage.getSender() == userID) {
                    	lastMessage.setBody("Vous : " + lastMessage.getBody());
                    } else {
                    	lastMessage.setBody(lastMessage.getSenderName() + " : " + lastMessage.getBody());
                    }

                    if(lastMessage.getBody().length() > 40) {
                    	lastMessage.setBody(lastMessage.getBody().substring(0, 40) + "...");
                    }
                    Label lastMessageLabel = new Label(lastMessage.getBody());
                    conversationGridPane.add(lastMessageLabel, 0, rowIndex + 1);

                    rowIndex += 2; // Incrémentez deux fois pour laisser de l'espace entre les conversations
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez les erreurs SQL de manière appropriée
        }
    }
}
