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
    private static final String SELECT_FIRST_QUERY="SELECT * FROM Standby WHERE AdID=? LIMIT 1";
    private static final String DELETE_FIRST_QUERY="DELETE FROM Standby WHERE AdID=? AND UserID=?";
    private static final String SELECT_ACCEPTED_FIRST_QUERY="SELECT Accepted FROM Standby WHERE AdID=? LIMIT 1";
    private static final String UPDATE_ACCEPTED_QUERY="UPDATE Standby SET Accepted=1 WHERE AdID=? AND UserID=?";

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

    public int firstStandBy(int adId) throws SQLException{
        int first = 0;
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FIRST_QUERY) ) {
            preparedStatement.setInt(1, adId);
            ResultSet resultSet = preparedStatement.executeQuery();
            first = resultSet.getInt("UserID");

        } catch (SQLException e) {
            // print SQL exception information
            Utils.printSQLException(e);
        }
        return first;
    }

    public void deleteFirst(int adId, int userID) throws SQLException{
        try (Connection connection=DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement=connection.prepareStatement(DELETE_FIRST_QUERY)){
                preparedStatement.setInt(1,adId);
                preparedStatement.setInt(2,userID);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            catch (SQLException e){
                Utils.printSQLException(e);
            }

    }

    public boolean firstIsAccepted(int adId) throws SQLException{
        int accepted=0;
        try (Connection connection=DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement=connection.prepareStatement(SELECT_ACCEPTED_FIRST_QUERY)){
                preparedStatement.setInt(1,adId);
                ResultSet resultSet=preparedStatement.executeQuery();
                accepted=resultSet.getInt("Accepted");
                preparedStatement.close();
            }
            catch (SQLException e) {
                Utils.printSQLException(e);
            }
        if (accepted==1){
            return true;
        }
        else {return false;}
    }

    public void updateAccepted(int adId,int UserID) throws SQLException{
        try (Connection connection = DriverManager.getConnection(Utils.DATABASE_URL);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCEPTED_QUERY) ) {
            preparedStatement.setInt(1, adId); 
            preparedStatement.setInt(2,UserID);      	
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
        	Utils.printSQLException(e);
        }
    }
}

