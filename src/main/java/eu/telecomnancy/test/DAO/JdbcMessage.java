package eu.telecomnancy.test.DAO ;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.* ;

public class JdbcMessage {
    private static final String INSERT_QUERY = "INSERT INTO Messages (SenderID, RecipientID, Subject, Body, DateUTC) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_BETWEEN_USERS_QUERY =
            "SELECT * FROM Messages WHERE (SenderID = ? AND RecipientID = ?) OR (SenderID = ? AND RecipientID = ?) ORDER BY DateUTC";
    private static final String SELECT_MAX_ID_QUERY = "SELECT MAX(id) FROM Message";

    public int insert(Message message) throws SQLException {
        int messageId = 0;

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, message.getSender());
            preparedStatement.setInt(2, message.getRecipient());
            preparedStatement.setInt(3, message.getSubject());
            preparedStatement.setString(4, message.getBody());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(message.getDateUTC()));

            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                messageId = rs.getInt(1);
                message.setID(messageId);
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }

        return messageId;
    }

    public static List<Message> getMessagesBetweenUsers(int user1ID, int user2ID) throws SQLException {
        List<Message> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BETWEEN_USERS_QUERY)) {
            preparedStatement.setInt(1, user1ID);
            preparedStatement.setInt(2, user2ID);
            preparedStatement.setInt(3, user2ID);
            preparedStatement.setInt(4, user1ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int senderID = resultSet.getInt("SenderID");
                int recipientID = resultSet.getInt("RecipientID");
                int subject = resultSet.getInt("Subject");
                String body = resultSet.getString("Body");
                LocalDateTime dateUTC = resultSet.getTimestamp("DateUTC").toLocalDateTime();

                Message message = new Message(id, senderID, recipientID, subject, body, dateUTC);
                messages.add(message);
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }

        return messages;
    }

    public int selectMaxId() throws SQLException {
        int maxId = 0;

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MAX_ID_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }

        return maxId;
    }
}