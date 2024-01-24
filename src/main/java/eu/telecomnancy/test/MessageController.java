package eu.telecomnancy.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import eu.telecomnancy.test.DAO.JdbcMessage;
import eu.telecomnancy.test.base.Message;

public class MessageController {

    @FXML
    private ListView<String> messageListView;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendMessageButton;

    private JdbcMessage jdbcMessage;

    // Assurez-vous d'initialiser jdbcMessage dans le constructeur ou via l'injection de dépendances

    @FXML
    public void initialize() {
        // Initialisation, si nécessaire
        jdbcMessage = new JdbcMessage(); // Vous pouvez initialiser autrement selon votre architecture
    }

    @FXML
    public void sendMessage() {
        // Logique pour envoyer un message
        String messageText = messageTextField.getText();
        if (!messageText.isEmpty()) {
            try {
                // Obtenez les ID des utilisateurs appropriés (senderId et receiverId)
                int senderId = 1; // ID de l'utilisateur qui envoie le message (user1 par exemple)
                int receiverId = 2; // ID de l'utilisateur qui reçoit le message (user2 par exemple)

                // Obtenez la date actuelle
                long currentDateMillis = System.currentTimeMillis();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateStr = dateFormat.format(new Date(currentDateMillis));
                
                // Vous pouvez également demander le sujet du message via un autre champ dans l'interface utilisateur
                int subject = 1;

                // Insérer le message dans la base de données
                Message message = new Message(0, senderId, receiverId, subject, messageText, (int) currentDateMillis);
                jdbcMessage.insertMessage(message);

                // Mise à jour de la liste des messages dans l'interface utilisateur
                messageListView.getItems().add("Vous (" + currentDateStr + "): " + messageText);
                messageTextField.clear();
            } catch (SQLException e) {
                e.printStackTrace(); // Gérez l'exception selon vos besoins
            }
        }
    }

    // Mettez en place d'autres méthodes nécessaires en fonction de vos besoins
}