package eu.telecomnancy.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eu.telecomnancy.Utils;
import eu.telecomnancy.base.Feedback;

public class JdbcFeedback {
    // Replace below database url, username and password with your actual database credentials
    private static final String SELECT_BY_AD_ID_QUERY = "SELECT *, u.Name as Username FROM Feedback f " +
    		"INNER JOIN Users u ON u.ID = f.UserID WHERE AdID=? order BY DateUTC DESC";
    private static final String INSERT_QUERY = "INSERT INTO Feedback (UserId,AdId,DateUTC,Note,Comments) VALUES(?,?,?,?,?)";
    

    public List<Feedback> selectByAdId( int adId ) throws SQLException {
   	
    	List<Feedback> rows = new ArrayList<Feedback>();
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_AD_ID_QUERY) ) {
            preparedStatement.setInt(1, adId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while( resultSet.next() ) {
	        	// public Feedback(int ID, int userId, int adId, LocalDate date, int note, String comments )
	            LocalDate createdDate = Utils.UnixTimeToDateTime(resultSet.getLong("DateUTC"));
	
	        	Feedback row = new Feedback(resultSet.getInt("ID"),
	        			resultSet.getInt("UserID"),
	        			resultSet.getString("Username"),
	        			resultSet.getInt("AdID"),
	        			createdDate,
	        			resultSet.getInt("Note"),
	        			resultSet.getString("Comments"));
	        	rows.add(row);
            }
            
        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return rows;
    }
    
    public void insert( Feedback fb ) throws SQLException {
 	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY) ) {
            preparedStatement.setInt(1, fb.getUserId());       	
            preparedStatement.setInt(2, fb.getAdId());
            long seconds = Utils.DateTimeToUnixTime( fb.getCreatedDate() );
            preparedStatement.setLong(3, seconds);
            preparedStatement.setInt(4, fb.getNote());
            preparedStatement.setString(5, fb.getComments());
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }
    
}

