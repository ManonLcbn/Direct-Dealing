package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.Message;

public class JdbcMessage {

    private static final String INSERT_MESSAGE_QUERY = "INSERT INTO Messages (senderUserId, receiverUserId, subject, messageText, messageDate) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_MESSAGES_QUERY = "SELECT * FROM Messages WHERE (senderUserId = ? AND receiverUserId = ?) OR (senderUserId = ? AND receiverUserId = ?) ORDER BY messageDate DESC";

    public void insertMessage(Message message) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE_QUERY)) {
            preparedStatement.setInt(1, message.getSenderUserId());
            preparedStatement.setInt(2, message.getReceiverUserId());
            preparedStatement.setInt(3, message.getSubject());
            preparedStatement.setString(4, message.getMessageText());
            preparedStatement.setInt(5, message.getMessageDate());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
    }

    public List<Message> getMessages(int userId1, int userId2) throws SQLException {
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MESSAGES_QUERY)) {
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.setInt(3, userId2);
            preparedStatement.setInt(4, userId1);

            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("messageId");
                int senderUserId = resultSet.getInt("senderUserId");
                int receiverUserId = resultSet.getInt("receiverUserId");
                int subject = resultSet.getInt("subject");
                String messageText = resultSet.getString("messageText");
                int messageDate = resultSet.getInt("messageDate");

                Message message = new Message(messageId, senderUserId, receiverUserId, subject, messageText, messageDate);
                messages.add(message);
            }
        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return messages;
    }
}
