package eu.telecomnancy.test.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.telecomnancy.test.Utils;
import eu.telecomnancy.test.base.Standby;

public class JdbcStandby {
    // Replace below database url, username and password with your actual database credentials
    private static final String SELECT_QUERY = "SELECT COUNT(*) AS total FROM Standby WHERE AdID=?";
    private static final String INSERT_QUERY = "INSERT INTO Standby (UserId,AdId,StartDateUTC,EndDateUTC) VALUES(?,?,?,?)";
    private static final String SELECT_EXIST_QUERY = "SELECT * FROM Standby WHERE UserID=? AND AdID=?";
    

    public int selectCount( int adId ) throws SQLException {
   	
    	int total = 0;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY) ) {
            preparedStatement.setInt(1, adId);
            ResultSet resultSet = preparedStatement.executeQuery();
            total = resultSet.getInt("total");
        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return total;
    }
    
    public void insert( Standby stb ) throws SQLException {
 	
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY) ) {
            preparedStatement.setInt(1, stb.getUserID());       	
            preparedStatement.setInt(2, stb.getAdID());
            long seconds = Utils.DateTimeToUnixTime( stb.getStartDate() );
            preparedStatement.setLong(3, seconds);
            seconds = Utils.DateTimeToUnixTime( stb.getEndDate() );
            preparedStatement.setLong(4, seconds);
            System.out.println(preparedStatement);
            // execute the preparedstatement insert
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
    }
    
    public boolean isExists( Standby stb ) throws SQLException {
    	boolean isInDb = false;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXIST_QUERY) ) {
            preparedStatement.setInt(1, stb.getUserID());
            preparedStatement.setInt(2, stb.getAdID());
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            isInDb = resultSet.next();
            preparedStatement.close();

        } catch (SQLException e) {
            // print SQL exception information
        	Utils.printSQLException(e);
        }
        return isInDb;
    }
}
