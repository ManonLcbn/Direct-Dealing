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
import eu.telecomnancy.test.base.AdRow;

public class JdbcAdRow {
	
    private static final String SELECT_ALL_QUERY = "SELECT *,u.Name AS ProfileName FROM Ads " +
    		"INNER JOIN Users u ON u.ID = Ads.UserID WHERE (NOT u.isDisable) " +
    		"AND CASE WHEN Ads.EndDateUTC = 0 THEN 1 ELSE Ads.EndDateUTC > unixepoch() END";
    private static final String SELECT_BY_KEYWORDS_QUERY = "SELECT *, u.Name AS ProfileName FROM ads AS a " +
    		"INNER JOIN users AS u ON a.UserID = u.id WHERE (NOT u.isDisable) AND " +
    		"(a.title LIKE ? OR a.description LIKE ? OR a.Localization LIKE ?)";
    final String strType[] = { "RECHERCHE", "PROPOSE" };

    public List<AdRow> selectAll( ) throws SQLException {

    	List<AdRow> rows = new ArrayList<AdRow>();
    	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            Statement stmt = connection.createStatement() ) {
            ResultSet resultSet = stmt.executeQuery(SELECT_ALL_QUERY);
            while( resultSet.next() ) {
            	StringBuilder information = new StringBuilder(resultSet.getString("Title"));
            	information.append(" - ");
            	information.append(resultSet.getString("Description"));
            	
            	AdRow row = new AdRow(resultSet.getInt("ID"),
            			resultSet.getBoolean("isMaterial"),
            			resultSet.getString("Localization"),
            			resultSet.getString("ProfileName"),
            			resultSet.getBoolean("isRequest") ? strType[0] : strType[1],
            			information.toString());
            	rows.add(row);
            }

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return rows;
    }

    public List<AdRow> selectByKeywords(String keywordLine) throws SQLException {

        List<String> keywords = Arrays.asList(keywordLine.split(" ", -1));
        List<AdRow> rows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL)) {
            // Directly query the 'ads' table for each keyword
            for (String keyword : keywords) {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_KEYWORDS_QUERY);
                String preKeyword = "%" + keyword + "%";
                preparedStatement.setString(1, preKeyword);
                preparedStatement.setString(2, preKeyword);
                preparedStatement.setString(3, keyword + "%");

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    AdRow row = new AdRow(resultSet.getInt("ID"),
                            resultSet.getBoolean("isMaterial"),
                            resultSet.getString("Localization"),
                            resultSet.getString("ProfileName"),
                            resultSet.getBoolean("isRequest") ? strType[0] : strType[1],
                            resultSet.getString("Title") + " - " + resultSet.getString("Description"));
                    rows.add(row);
                }
            }
        } catch (SQLException e) {
            Utils.printSQLException(e);
        }
        return rows;
    }
}
