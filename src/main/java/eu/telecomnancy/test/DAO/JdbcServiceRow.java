package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.ServiceRow;

public class JdbcServiceRow {
	
    private static final String SELECT_ALL_QUERY = "SELECT *,u.Name AS ProfileName FROM Services " +
    		"INNER JOIN Users u ON u.ID = Services.UserID WHERE (NOT u.isDisable) " ;
    private static final String SELECT_BY_KEYWORDS_QUERY = "SELECT *, u.Name AS ProfileName FROM Services AS s " +
    		"INNER JOIN users AS u ON a.UserID = u.id WHERE (NOT u.isDisable) AND " +
    		"(s.title LIKE ? OR s.description LIKE ? OR s.Localization LIKE ?)";
    final String strType[] = { "RECHERCHE", "PROPOSE" };

    public List<ServiceRow> selectAll( ) throws SQLException {

    	List<ServiceRow> rows = new ArrayList<ServiceRow>();
    	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            Statement stmt = connection.createStatement() ) {
            ResultSet resultSet = stmt.executeQuery(SELECT_ALL_QUERY);
            while( resultSet.next() ) {
            	StringBuilder information = new StringBuilder(resultSet.getString("Title"));
            	information.append(" - ");
            	information.append(resultSet.getString("Description"));
            	
            	ServiceRow row = new ServiceRow(resultSet.getInt("ID"),
            			resultSet.getString("Localization"),
            			resultSet.getString("ProfileName"),
            			resultSet.getBoolean("isRequest") ? strType[0] : strType[1],
            			information.toString(),
                        resultSet.getBoolean("isPonctual"));
            	rows.add(row);
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return rows;
    }

    public List<ServiceRow> selectByKeywords(String keywordLine) throws SQLException {

        List<String> keywords = Arrays.asList(keywordLine.split(" ", -1));
        List<ServiceRow> rows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL)) {
            // Directly query the 'Services' table for each keyword
            for (String keyword : keywords) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_KEYWORDS_QUERY);
                String preKeyword = "%" + keyword + "%";
                preparedStatement.setString(1, preKeyword);
                preparedStatement.setString(2, preKeyword);
                preparedStatement.setString(3, keyword + "%");

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    ServiceRow row = new ServiceRow(resultSet.getInt("ID"),
                            resultSet.getString("Localization"),
                            resultSet.getString("ProfileName"),
                            resultSet.getBoolean("isRequest") ? strType[0] : strType[1],
                            resultSet.getString("Title") + " - " + resultSet.getString("Description"),
                            resultSet.getBoolean("isPonctual"));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
        return rows;
    }
}

    
